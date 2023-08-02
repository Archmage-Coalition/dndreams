package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.util.SightUtil;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class FacelessEntity extends HostileEntity implements IAnimatable, SanityEntity {

    public static TrackedData<Boolean> WOVEN = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Boolean> CORPOREAL = DataTracker.registerData(FacelessEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public FacelessEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public FacelessEntity(World world) {
        super(ModEntities.FACELESS, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(WOVEN, false);
        this.getDataTracker().startTracking(CORPOREAL, false);
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

        if (isCorporeal() && world instanceof ServerWorld serverWorld) {

            for (ServerPlayerEntity player: serverWorld.getPlayers()) {

                TormentComponent torment = getTorment(player);

                if (player.isCreative() || player.isSpectator() || torment.isTruthActive()) continue;

                if (SightUtil.inView(player, this)) {
                    torment.setFearDrowning();
                }
            }
        }
    }

    @Override
    public boolean isWoven() {
        return dataTracker.get(WOVEN);
    }

    @Override
    public boolean isCorporeal() {
        return dataTracker.get(CORPOREAL) || isWoven();
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

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataTracker tracker = getDataTracker();

        nbt.putBoolean(WOVEN_KEY, tracker.get(WOVEN));
        nbt.putBoolean(CORPOREAL_KEY, tracker.get(CORPOREAL));
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
    }

    @Override
    public boolean canHit() {
        return isCorporeal();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || damageSource == DamageSource.DROWN || (!isCorporeal() && !damageSource.isOutOfWorld());
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

    public enum FacelessPhase {

    }
}
