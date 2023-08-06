package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.ai.TormentorMeleeAttackGoal;
import net.eman3600.dndreams.entities.ai.TormentorRangedAttackGoal;
import net.eman3600.dndreams.entities.ai.TormentorTacticsGoal;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TormentorEntity extends HostileEntity implements IAnimatable, SanityEntity, RangedAttackMob {
    public static final Item BOW_ITEM = ModItems.MINDSTRING_BOW;

    private boolean updateRanged = false;

    private final TormentorMeleeAttackGoal meleeGoal = new TormentorMeleeAttackGoal(this, 1.0, false);
    private final TormentorRangedAttackGoal rangedGoal = new TormentorRangedAttackGoal(this, 1, 20, 20, BOW_ITEM);
    private final TormentorTacticsGoal tacticsGoal = new TormentorTacticsGoal(this);

    public static TrackedData<Boolean> WOVEN = DataTracker.registerData(TormentorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Boolean> CORPOREAL = DataTracker.registerData(TormentorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public TormentorEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        updateAttackType();
        goalSelector.add(0, tacticsGoal);
        this.experiencePoints = 12;
    }

    public TormentorEntity(World world) {
        this(ModEntities.TORMENTOR, world);
    }

    public TormentorEntity(World world, boolean woven) {
        this(world);

        getDataTracker().set(WOVEN, woven);
        getDataTracker().set(CORPOREAL, woven);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 4.0f, 1.0, 1.2, a -> a instanceof PlayerEntity player && getSanity(player) >= 25 && !isCorporeal()));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, OcelotEntity.class, 6.0f, 1.0, 1.2));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, CatEntity.class, 6.0f, 1.0, 1.2));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));


        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, target -> {
            float sanity = getSanity((PlayerEntity) target);

            return sanity < 25 || isWoven();
        }));
    }

    public void updateAttackType() {

        if (world == null || world.isClient()) {
            return;
        }

        goalSelector.remove(meleeGoal);
        goalSelector.remove(rangedGoal);

        ItemStack stack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, BOW_ITEM));
        if (stack.isOf(BOW_ITEM)) {
            rangedGoal.setAttackInterval(20);
            goalSelector.add(2, rangedGoal);
        } else {
            goalSelector.add(2, meleeGoal);
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(WOVEN, false);
        this.getDataTracker().startTracking(CORPOREAL, false);
    }

    public static DefaultAttributeContainer.Builder createTormentorAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 70.0d)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0d)
                .add(EntityAttributes.GENERIC_ARMOR, 8.0d)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 28.0d);
    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isClient()) {
            if (getTarget() != null && !isCorporeal()) {

                setCorporeal(true);
            } else if (getTarget() == null && isCorporeal() && !isWoven()) {

                setCorporeal(false);
            }

            if (getTarget() instanceof PlayerEntity player) {

                if (getSanity(player) >= 45 && !isWoven()) {
                    setTarget(null);
                    setCorporeal(false);
                } else {
                    getTorment(player).markHaze();
                }
            }
        }

        if (updateRanged) {
            updateRanged = false;

            rangedGoal.setRanged(!isRanged());
        }
    }

    private <E extends IAnimatable> PlayState idlePlayPredicate(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();


        if (isRanged()) {

            builder.addAnimation("bow", EDefaultLoopTypes.LOOP);

            event.getController().setAnimation(builder);
        } else if (event.isMoving()) {
            builder.addAnimation("move", EDefaultLoopTypes.LOOP);
        } else {
            builder.addAnimation("idle_arms", EDefaultLoopTypes.LOOP);
        }


        event.getController().setAnimation(builder);
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPlayPredicate(AnimationEvent<E> event) {

        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            AnimationBuilder builder = new AnimationBuilder();
            builder.addAnimation("melee", EDefaultLoopTypes.PLAY_ONCE);

            event.getController().markNeedsReload();
            event.getController().setAnimation(builder);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle", 5, this::idlePlayPredicate));
        data.addAnimationController(new AnimationController<>(this, "attack", 2, this::attackPlayPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
        updateAttackType();
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        super.equipStack(slot, stack);
        if (!world.isClient()) updateAttackType();
    }

    @Override
    public boolean isWoven() {
        return getDataTracker().get(WOVEN);
    }

    @Override
    public boolean isCorporeal() {
        return getDataTracker().get(CORPOREAL) || isWoven();
    }

    public void setCorporeal(boolean corporeal) {
        getDataTracker().set(CORPOREAL, corporeal || isWoven());

        if (!corporeal && isRanged()) {
            setRanged(false);
        }
    }

    @Override
    public boolean canView(PlayerEntity player) {
        return isWoven() || getSanity(player) < 25 || (isCorporeal() && getSanity(player) < 45);
    }

    @Override
    public boolean canHit() {
        return isCorporeal();
    }

    @Override
    public float renderedOpacity(PlayerEntity player) {
        return getTorment(player).isTruthActive() ? 1f : isWoven() ? .5f : isCorporeal() ? MathHelper.clamp((1f - (getSanity(player))/150f) * .5f + .1f, .1f, .5f) : MathHelper.clamp((1f - (getSanity(player))/25f) * .3f + .1f, .1f, .3f);
    }

    @Override
    public float renderedClarity(PlayerEntity player) {
        return !isAlive() || getTorment(player).isTruthActive() || hurtTime > 0 ? 1f : isCorporeal() ? .2f : MathHelper.clamp((1f - (getSanity(player))/25f) * .1f, 0, .1f);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || damageSource == DamageSource.DROWN || (!isCorporeal() && !damageSource.isOutOfWorld() && !((DamageSourceAccess)damageSource).isTransethereal());
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        return !canView(player);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!isCorporeal()) return;

        super.playStepSound(pos, state);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (damageSource.getAttacker() instanceof PlayerEntity player) {
            TormentComponent torment = getTorment(player);
            if (!torment.isAttuned()) torment.lowerSanity(-12.5f);
        }

        for (TormentorEntity entity: tacticsGoal.getNearbyTormentors()) {
            if (entity.getTarget() != null) {
                entity.tacticsGoal.updateTactics();
            }
        }

        super.onDeath(damageSource);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends TormentorEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return !world.getBlockState(pos.down()).isAir();
    }

    @Override
    public boolean isPushable() {
        return isCorporeal();
    }

    @Override
    protected void pushAway(Entity entity) {
        if (isCorporeal()) super.pushAway(entity);
    }

    @Override
    public boolean occludeVibrationSignals() {
        return true;
    }

    public boolean isRanged() {
        return this.getMainHandStack().isOf(BOW_ITEM);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemStack = new ItemStack(Items.ARROW);
        PersistentProjectileEntity persistentProjectileEntity = ProjectileUtil.createArrowProjectile(this, itemStack, pullProgress);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        persistentProjectileEntity.setVelocity(d, e + g * (double)0.2f, f, 1.6f, 14 - this.world.getDifficulty().getId() * 4);
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(persistentProjectileEntity);
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        this.updateAttackType();

        this.handDropChances[0] = -2f;
        this.handDropChances[1] = -2f;

        return data;
    }

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == BOW_ITEM;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.3f;
    }

    public void setRanged(boolean ranged) {
        updateRanged = ranged != isRanged();
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }
}
