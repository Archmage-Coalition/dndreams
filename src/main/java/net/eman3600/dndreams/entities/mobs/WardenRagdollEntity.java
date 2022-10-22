package net.eman3600.dndreams.entities.mobs;

import net.eman3600.dndreams.initializers.ModEntities;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class WardenRagdollEntity extends MobEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private int stage = 0;
    private int leechTicks = 0;
    private BlockPos portalCenter = new BlockPos(getPos());


    public WardenRagdollEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);

        setInvulnerable(true);
    }

    public WardenRagdollEntity(World world) {
        super(ModEntities.WARDEN_RAGDOLL_ENTITY_TYPE, world);

        setInvulnerable(true);
    }


    private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event) {
        boolean bl = event.getController().getAnimationState().equals(AnimationState.Stopped);

        event.getController().markNeedsReload();

        if (stage == 0) {
            event.getController().setAnimation(buildAnimation("snatch", false));
            stage++;
        } else if (stage == 1 && bl) {
            event.getController().setAnimation(buildAnimation("flail", true));
        } else if (stage == 2) {
            event.getController().setAnimation(buildAnimation("charge", false));
            stage++;
        } else if (stage == 3 && bl) {
            event.getController().setAnimation(buildAnimation("sap", true));
        } else if (stage == 4 && bl) {
            event.getController().setAnimation(buildAnimation("drop", true));
        } else if (stage == 5) {
            event.getController().setAnimation(buildAnimation("collapse", false));
        }

        return PlayState.CONTINUE;
    }

    private static AnimationBuilder buildAnimation(String name, boolean loop) {
        return new AnimationBuilder().addAnimation(name, loop);
    }

    public void initializeFromWarden(WardenEntity warden, BlockPos center) {
        portalCenter = center;

        setPose(warden.getPose());

        setVelocity(0, 0, 0);
    }



    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    protected void initGoals() {

    }

    public static DefaultAttributeContainer.Builder createWardenRagdollAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0d);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WARDEN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WARDEN_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WARDEN_AMBIENT;
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        setVelocity(0, 0, 0);
        this.setNoGravity(true);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("stage", stage);
        nbt.putInt("leech_ticks", leechTicks);
        nbt.put("portal_center", toNbtList(portalCenter.getX(), portalCenter.getY(), portalCenter.getZ()));

        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        NbtList posList = nbt.getList("return_pos", 3);

        stage = nbt.getInt("stage");
        leechTicks = nbt.getInt("leech_ticks");
        portalCenter = new BlockPos(posList.getInt(0), posList.getInt(1), posList.getInt(2));
    }

    private NbtList toNbtList(int... values) {
        NbtList nbtList = new NbtList();
        int[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int d = var3[var5];
            nbtList.add(NbtInt.of(d));
        }

        return nbtList;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }
}
