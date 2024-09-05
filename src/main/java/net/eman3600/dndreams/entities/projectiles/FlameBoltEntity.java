package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.entities.WaterIgnorant;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.eman3600.dndreams.Initializer.MODID;

public class FlameBoltEntity extends ThrownEntity implements WaterIgnorant {

    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/flame_bolt.png");

    public FlameBoltEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    public FlameBoltEntity(LivingEntity owner, World world) {
        super(ModEntities.FLAME_BOLT, owner, world);
    }

    public FlameBoltEntity(World world, double x, double y, double z) {
        super(ModEntities.FLAME_BOLT, x, y, z, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        if (entity == getOwner()) return;

        entity.setOnFireFor(4);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
        BlockState state = FireBlock.getState(world, pos);

        if (getOwner() instanceof PlayerEntity player && world.getBlockState(pos).canReplace(new ItemPlacementContext(new ItemUsageContext(player, player.getActiveHand(), blockHitResult))) && world.getFluidState(pos).isEmpty() && state.canPlaceAt(world, pos)) {
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1, 1);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
        }

        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.055f;
    }
}
