package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import static net.eman3600.dndreams.Initializer.MODID;

public class DreamSheepEntity extends AnimalEntity implements Shearable, SanityEntity {

    public static final TrackedData<Boolean> SHEARED = DataTracker.registerData(DreamSheepEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> CORPOREAL = DataTracker.registerData(DreamSheepEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final Identifier SHEARED_LOOT = new Identifier(MODID, "entities/dream_sheep_sheared");
    private int searchTicks = 20;
    private final TargetPredicate insanityTarget = TargetPredicate.createNonAttackable().setPredicate((entity) -> entity instanceof PlayerEntity player && getSanity(player) < 25);

    public DreamSheepEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(SHEARED, false);
        dataTracker.startTracking(CORPOREAL, false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public boolean isSheared() {
        return dataTracker.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        dataTracker.set(SHEARED, sheared);
    }

    public void setCorporeal(boolean corporeal) {
        dataTracker.set(CORPOREAL, corporeal);
    }

    @Override
    protected Identifier getLootTableId() {
        if (isSheared()) return SHEARED_LOOT;
        return super.getLootTableId();
    }

    @Nullable
    @Override
    public DreamSheepEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return ModEntities.DREAM_SHEEP.create(serverWorld);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player2, Hand hand) {
        ItemStack itemStack = player2.getStackInHand(hand);
        if (itemStack.isOf(Items.SHEARS)) {
            if (!this.world.isClient && this.isShearable()) {
                this.sheared(SoundCategory.PLAYERS);
                this.emitGameEvent(GameEvent.SHEAR, player2);
                itemStack.damage(1, player2, player -> player.sendToolBreakStatus(hand));
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }
        return super.interactMob(player2, hand);
    }

    @Override
    public void tick() {
        super.tick();

        if (world instanceof ServerWorld serverWorld) {

            PlayerEntity player = serverWorld.getClosestPlayer(insanityTarget, this);
            setCorporeal(player != null && squaredDistanceTo(player) < 1024);

            if (player != null) {

                searchTicks = 10;
            } else if (searchTicks-- < 0) {
                discard();
                return;
            }
        }
    }

    @Override
    public void sheared(SoundCategory shearedSoundCategory) {
        this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0f, 1.0f);
        this.setSheared(true);
        int i = 2 + this.random.nextInt(4);
        for (int j = 0; j < i; ++j) {
            ItemEntity itemEntity = this.dropItem(ModItems.DREAM_POWDER, 1);
            if (itemEntity == null) continue;
            itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
        }
    }

    @Override
    public boolean isShearable() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    public static DefaultAttributeContainer.Builder createDreamSheepAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Sheared", isSheared());
        nbt.putBoolean("Corporeal", isCorporeal());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Sheared")) setSheared(nbt.getBoolean("Sheared"));
        if (nbt.contains("Corporeal")) setSheared(nbt.getBoolean("Corporeal"));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15f, 1.0f);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.95f * dimensions.height;
    }

    public float getNeckAngle(float delta) {
        return 0;
    }

    public float getHeadAngle(float delta) {
        return this.getPitch() * ((float)Math.PI / 180);
    }

    @Override
    public boolean isWoven() {
        return false;
    }

    @Override
    public boolean isCorporeal() {
        return dataTracker.get(CORPOREAL);
    }

    @Override
    public boolean canView(PlayerEntity player) {
        return getSanity(player) < 25;
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        return !canView(player);
    }

    @Override
    public float renderedOpacity(PlayerEntity player) {
        return 1;
    }

    @Override
    public float renderedClarity(PlayerEntity player) {
        return 1;
    }

    @Override
    public boolean canHit() {
        return isCorporeal();
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

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos test = pos.down();
        return !world.getBlockState(test).isAir() && world.getFluidState(test).isEmpty();
    }
}
