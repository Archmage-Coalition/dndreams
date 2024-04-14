package net.eman3600.dndreams.entities.projectiles;

import net.eman3600.dndreams.entities.WaterIgnorant;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

public class GlowBoltEntity extends ThrownEntity implements WaterIgnorant {

    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/glow_bolt.png");

    public GlowBoltEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowBoltEntity(LivingEntity owner, World world) {
        super(ModEntities.GLOW_BOLT, owner, world);
    }

    public GlowBoltEntity(World world, double x, double y, double z) {
        super(ModEntities.GLOW_BOLT, x, y, z, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
            BlockPos blockPos = blockHitResult.getBlockPos();
        }

        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);

            if (hitResult instanceof BlockHitResult blockHit && getOwner() instanceof PlayerEntity player) {
                BlockPos pos = blockHit.getBlockPos().offset(blockHit.getSide());

                if (world.getBlockState(pos).canReplace(new ItemPlacementContext(new ItemUsageContext(player, player.getActiveHand(), blockHit))) && world.getFluidState(pos).isEmpty()) {
                    world.setBlockState(pos, ModBlocks.SHINE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, SoundCategory.BLOCKS, 1, 1);
                }
            } else if (hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {

                target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
            }

            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }
}
