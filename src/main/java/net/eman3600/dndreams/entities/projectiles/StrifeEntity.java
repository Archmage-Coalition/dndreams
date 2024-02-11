package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.entities.WaterIgnorant;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class StrifeEntity extends ThrownEntity implements WaterIgnorant {

    public StrifeEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    public StrifeEntity(LivingEntity owner, World world) {
        super(ModEntities.STRIFE, owner, world);
    }

    public StrifeEntity(World world, double x, double y, double z) {
        super(ModEntities.STRIFE, x, y, z, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        BlockPos pos;
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
            pos = ((EntityHitResult)hitResult).getEntity().getBlockPos();
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
            pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
        } else return;

        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);

            if (hitResult instanceof BlockHitResult blockHit && getOwner() instanceof PlayerEntity player) {

                if (!world.getBlockState(pos.down()).isAir()) {

                    for (int i = 0; i < 3; i++) {
                        BlockPos pos2 = pos.add(0, i, 0);

                        if (world.getBlockState(pos2).canReplace(new ItemPlacementContext(new ItemUsageContext(player, player.getActiveHand(), blockHit))) && world.getFluidState(pos2).isEmpty()) {
                            world.setBlockState(pos2, ModBlocks.STRIFE_FIRE.getDefaultState(), Block.NOTIFY_LISTENERS);
                        } else {
                            break;
                        }
                    }

                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 1, 1);
                }
            }

            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        entityHitResult.getEntity().damage(DamageSourceAccess.fire(this, getOwner()), 4);
        entityHitResult.getEntity().setOnFireFor(4);
        if (entityHitResult.getEntity() instanceof LivingEntity entity) {
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.HEARTBLEED, 140));
        }
    }
}
