package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.entities.ai.TormentorMeleeAttackGoal;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
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

public class TormentorEntity extends HostileEntity implements IAnimatable, SanityEntity {

    public static TrackedData<Boolean> WOVEN = DataTracker.registerData(TormentorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Boolean> CORPOREAL = DataTracker.registerData(TormentorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public TormentorEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public TormentorEntity(World world) {
        super(ModEntities.TORMENTOR, world);
    }

    public TormentorEntity(World world, boolean woven) {
        this(world);

        getDataTracker().set(WOVEN, woven);
        getDataTracker().set(CORPOREAL, woven);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new TormentorMeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));


        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, target -> {
            float sanity = getSanity((PlayerEntity) target);

            return sanity <= 25 || isWoven();
        }));
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
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0d)
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

                if (getSanity(player) > 40 && !isWoven()) {
                    setTarget(null);
                    setCorporeal(false);
                } else {
                    getTorment(player).damageSanity(2);
                }
            }
        }
    }

    private <E extends IAnimatable> PlayState idlePlayPredicate(AnimationEvent<E> event) {
        AnimationBuilder builder = new AnimationBuilder();



        if (event.isMoving()) {
            builder.addAnimation("move", EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
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
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "idle", 0, this::idlePlayPredicate));
        animationData.addAnimationController(new AnimationController<>(this, "attack", 0, this::attackPlayPredicate));
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
    }

    @Override
    public boolean canView(PlayerEntity player) {
        return isWoven() || getSanity(player) <= 50;
    }

    @Override
    public boolean canHit() {
        return isCorporeal();
    }

    @Override
    public float renderedOpacity(PlayerEntity player) {
        return isWoven() ? .85f : isCorporeal() ? MathHelper.clamp(1f - (getSanity(player))/150f + .1f, .1f, .85f) : MathHelper.clamp((1f - (getSanity(player))/50f) * .75f + .1f, .1f, .75f);
    }

    @Override
    public float renderedClarity(PlayerEntity player) {
        return !isAlive() || hurtTime > 0 ? 1f : isCorporeal() ? .2f : MathHelper.clamp((1f - (getSanity(player))/50f) * .1f, 0, .1f);
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
            getTorment(player).lowerSanity(-12.5f);
        }

        super.onDeath(damageSource);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);
        if (bl && this.getMainHandStack().isEmpty() && target instanceof LivingEntity) {
            ((LivingEntity)target).addStatusEffect(new StatusEffectInstance(ModStatusEffects.HAUNTED, 25, 0, false, false, false), this);
        }
        return bl;
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends TormentorEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return !world.getBlockState(pos.down()).isAir();
    }
}
