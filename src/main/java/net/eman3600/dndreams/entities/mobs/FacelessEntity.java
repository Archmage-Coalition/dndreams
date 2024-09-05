package net.eman3600.dndreams.entities.mobs;

import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.ai.faceless.FacelessStalkingGoal;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.util.SightUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FacelessEntity extends HostileEntity implements IAnimatable, SanityEntity {

    @Nullable
    private UUID victimUuid;
    @Nullable
    private PlayerEntity victimEntity;
    private int searchTicks = 10;

    public static TrackedData<Boolean> WOVEN = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Boolean> CORPOREAL = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Boolean> HAS_EYES = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<String> PHASE = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.STRING);
    public static TrackedData<Integer> HIDDEN_TIME = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Integer> RANDOM_TIME = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public FacelessStalkingGoal stalkingGoal;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public FacelessEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public FacelessEntity(World world) {
        super(ModEntities.FACELESS, world);
    }

    public FacelessEntity(World world, PlayerEntity victim) {
        super(ModEntities.FACELESS, world);
        setVictim(victim);
    }

    public void setVictim(@Nullable Entity entity) {
        if (entity instanceof PlayerEntity player) {
            this.victimUuid = entity.getUuid();
            this.victimEntity = player;
        }
    }

    @Nullable
    public PlayerEntity getVictim() {
        if (this.victimEntity != null && !this.victimEntity.isRemoved()) {
            return this.victimEntity;
        }
        if (this.victimUuid != null && this.world instanceof ServerWorld) {
            this.victimEntity = ((ServerWorld)this.world).getEntity(this.victimUuid) instanceof PlayerEntity player ? player : null;
            return this.victimEntity;
        }
        return null;
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        stalkingGoal = new FacelessStalkingGoal(this);

        refreshGoals();
    }

    public void refreshGoals() {

        goalSelector.clear();
        targetSelector.clear();

        FacelessPhase phase = getPhase();

        if (phase != FacelessPhase.ASSAULTING) {
            goalSelector.add(4, stalkingGoal);
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(WOVEN, false);
        this.getDataTracker().startTracking(CORPOREAL, true);
        this.getDataTracker().startTracking(HAS_EYES, true);
        this.getDataTracker().startTracking(PHASE, "haunting");
        this.getDataTracker().startTracking(HIDDEN_TIME, -1);
        this.getDataTracker().startTracking(RANDOM_TIME, 0);
    }

    public static DefaultAttributeContainer.Builder createFacelessAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0d)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0d)
                .add(EntityAttributes.GENERIC_ARMOR, 10.0d)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 28.0d);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle", 5, this::idlePlayPredicate));
    }

    private <E extends IAnimatable> PlayState idlePlayPredicate(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();


//        if (event.isMoving()) {
//            builder.addAnimation("move", ILoopType.EDefaultLoopTypes.LOOP);
//        } else {
//            builder.addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
//        }
        builder.addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);


        event.getController().setAnimation(builder);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {
        super.tick();

        if (world instanceof ServerWorld serverWorld) {

            PlayerEntity victim = getVictim();

            if (victim == null) {
                if (searchTicks-- < 0) {
                    discard();
                }
            } else {
                searchTicks = 10;

                if (squaredDistanceTo(victim) < 1024) setDespawnCounter(0);

                if (getSanity(victim) >= 85 || (isCorporeal() && daylightAt(serverWorld, getBlockPos())) || daylightAt(serverWorld, victim.getBlockPos()) || (age % 200 == 0 && isOutdated(victim))) {
                    discard();
                    return;
                }

                if (isCorporeal()) {

                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {

                        TormentComponent torment = getTorment(player);
                        boolean hasFearlessShades = TrinketsApi.getTrinketComponent(player)
                                .get().isEquipped(ModItems.FEARLESS_SHADES);

                        if (player.isCreative() || player.isSpectator() || torment.isTruthActive() || hasFearlessShades) continue;

                        if (SightUtil.inView(player, this)) {
                            torment.setFearDrowning();
                        }
                    }
                } else {

                    if (getHiddenTime() == -1) {
                        setHiddenTime(getHiddenLength() / 2);
                        randomizeTime();
                    } else if (getHiddenTime() > getHiddenLength()) {
                        setHiddenTime(0);
                        randomizeTime();
                    } else {
                        tickHidden();
                    }
                }
            }


        }
    }

    public boolean isOutdated(PlayerEntity victim) {
        return victim == null || getTorment(victim).getFacelessEntity() != this;
    }

    @Override
    public boolean isWoven() {
        return dataTracker.get(WOVEN);
    }

    public void setWoven(boolean woven) {
        dataTracker.set(WOVEN, woven);
    }

    @Override
    public boolean isCorporeal() {
        return dataTracker.get(CORPOREAL) || isWoven();
    }

    public void setCorporeal(boolean corporeal) {
        dataTracker.set(CORPOREAL, corporeal);
    }

    @Override
    public boolean canView(PlayerEntity player) {
        return isCorporeal() && getSanity(player) < 65;
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        return !canView(player);
    }

    @Override
    public float renderedOpacity(PlayerEntity player) {
        return getTorment(player).isTruthActive() ? 1f : 0.9f;
    }

    @Override
    public float renderedClarity(PlayerEntity player) {
        return getTorment(player).isTruthActive() ? 1f : 0.1f;
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends FacelessEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return !world.getBlockState(pos.down()).isAir();
    }

    public static boolean daylightAt(World world, BlockPos pos) {

        return world.getLightLevel(LightType.SKY, pos) >= 12 && world.isDay();
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        setWoven(true);

        PlayerEntity player;
        if ((player = getVictim()) != null) {

            TormentComponent torment = getTorment(player);

            torment.lowerSanity(-50);
            torment.setFacelessCooldown(24000);
        }

        super.onDeath(damageSource);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataTracker tracker = getDataTracker();

        nbt.putBoolean(WOVEN_KEY, tracker.get(WOVEN));
        nbt.putBoolean(CORPOREAL_KEY, tracker.get(CORPOREAL));
        nbt.putBoolean("HasEyes", tracker.get(HAS_EYES));
        nbt.putInt("HiddenTime", tracker.get(HIDDEN_TIME));
        nbt.putInt("RandomTime", tracker.get(RANDOM_TIME));
        nbt.putString("Phase", tracker.get(PHASE));

        if (this.victimUuid != null) {
            nbt.putUuid("Victim", this.victimUuid);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        DataTracker tracker = getDataTracker();

        if (nbt.contains(WOVEN_KEY)) {
            tracker.set(WOVEN, nbt.getBoolean(WOVEN_KEY));
        }
        if (nbt.contains(CORPOREAL_KEY)) {
            tracker.set(CORPOREAL, nbt.getBoolean(CORPOREAL_KEY));
        }
        tracker.set(HAS_EYES, nbt.getBoolean("HasEyes"));
        if (nbt.contains("Phase")) {
            tracker.set(PHASE, nbt.getString("Phase"));
        }
        if (nbt.contains("HiddenTime")) {
            tracker.set(HIDDEN_TIME, nbt.getInt("HiddenTime"));
        }
        if (nbt.contains("RandomTime")) {
            tracker.set(RANDOM_TIME, nbt.getInt("RandomTime"));
        }
        if (nbt.containsUuid("Victim")) {
            this.victimUuid = nbt.getUuid("Victim");
        }
    }

    @Override
    public boolean canHit() {
        return isCorporeal();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || damageSource == DamageSource.DROWN || (!isCorporeal() && !damageSource.isOutOfWorld());
    }

    public void setPhase(FacelessPhase phase) {
        dataTracker.set(PHASE, phase.id);
        refreshGoals();
    }

    public FacelessPhase getPhase() {
        return FacelessPhase.getPhase(dataTracker.get(PHASE));
    }

    public void setHiddenTime(int ticks) {
        dataTracker.set(HIDDEN_TIME, ticks);
    }

    public void tickHidden() {
        setHiddenTime(getHiddenTime() + 1);
    }

    public int getHiddenTime() {
        return dataTracker.get(HIDDEN_TIME);
    }

    public void randomizeTime() {
        dataTracker.set(RANDOM_TIME, world.random.nextBetween(-60, 60));
    }

    public int getRandomTime(int multiplier) {
        return dataTracker.get(RANDOM_TIME) * multiplier;
    }

    public int getHiddenLength() {
        PlayerEntity victim = getVictim();

        if (victim == null) return Integer.MAX_VALUE;
        float sanity = getSanity(victim);
        FacelessPhase phase = getPhase();

        return (int) (phase == FacelessPhase.HAUNTING ? 1200 * (4 + (sanity - 25) * 16) + getRandomTime(40) : phase == FacelessPhase.STALKING ? 300 * (2 + (sanity - 5) * 3) + getRandomTime(5) : 200);
    }

    public boolean teleportRandomly() {
        if (this.world.isClient() || !this.isAlive()) {
            return false;
        }
        double d = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        double e = this.getY() + (double)(this.random.nextInt(64) - 32);
        double f = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d, e, f);
    }

    public boolean teleportTo(Entity entity) {
        Vec3d vec3d = new Vec3d(this.getX() - entity.getX(), this.getBodyY(0.5) - entity.getEyeY(), this.getZ() - entity.getZ());
        vec3d = vec3d.normalize();
        double e = this.getX() + (this.random.nextDouble() - 0.5) * 8.0 - vec3d.x * 16.0;
        double f = this.getY() + (double)(this.random.nextInt(16) - 8) - vec3d.y * 16.0;
        double g = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - vec3d.z * 16.0;
        return this.teleportTo(e, f, g);
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.world.getBottomY() && !this.world.getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.world.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        return this.teleport(x, y, z, false);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PHANTOM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!isCorporeal()) return;

        super.playStepSound(pos, state);
    }

    public enum FacelessPhase implements StringIdentifiable {
        HAUNTING("haunting"),
        STALKING("stalking"),
        ENGAGING("engaging"),
        ASSAULTING("assaulting");

        private final String id;
        private static final Map<String, FacelessPhase> phases = new HashMap<>();

        FacelessPhase(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }

        @Nonnull
        public static FacelessPhase getPhase(String id) {
            return phases.getOrDefault(id, HAUNTING);
        }

        static {
            for (FacelessPhase phase: values()) phases.put(phase.id, phase);
        }
    }
}
