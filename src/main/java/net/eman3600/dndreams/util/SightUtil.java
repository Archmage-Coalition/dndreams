package net.eman3600.dndreams.util;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class SightUtil {

    private static final float PLAYER_HEAD_SIZE = 0.15f;

    public static boolean inView(LivingEntity viewer, LivingEntity target) {

        if (viewer.hasStatusEffect(StatusEffects.BLINDNESS)) return false;

        if (directViewBlocked(viewer, target)) return false;

        return inViewCone(viewer, target) && canSee(viewer, target);
    }

    public static boolean inViewCone(LivingEntity viewer, LivingEntity target) {
        Vec3d targetPos = target.getPos();
        Vec3d viewerAngle = viewer.getRotationVector();

        Vec3d direction = viewer.getPos().relativize(targetPos).normalize();
        float limit = getViewDistance(viewer);
        if (direction.lengthSquared() > limit * limit) return false;
        //direction = new Vec3d(direction.x, 0, direction.z);
        return direction.dotProduct(viewerAngle) > 0;
    }

    public static float getViewDistance(LivingEntity viewer) {

        return viewer.hasStatusEffect(StatusEffects.DARKNESS) ? 14 : 64;
    }

    public static boolean canSee(LivingEntity viewer, LivingEntity target) {
        if (viewer instanceof PlayerEntity player) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            if (torment.getShroud() >= TormentComponent.MAX_SHROUD && target.world.getLightLevel(target.getBlockPos()) <= 0 && !viewer.hasStatusEffect(ModStatusEffects.HAUNTED)) {
                return false;
            }

            if (target.isInvisibleTo(player)) return false;
        }

        return !target.hasStatusEffect(StatusEffects.INVISIBILITY);
    }

    public static boolean directViewBlocked(LivingEntity viewer, LivingEntity target) {

        Vec3d viewerEyes = viewer.getEyePos();
        Vec3d targetEyes = target.getEyePos();

        return isLineBlocked(viewer, viewer.world, viewerEyes, targetEyes);
    }

    public static boolean viewBlocked(LivingEntity viewer, LivingEntity target) {

        Vec3d[] viewerCorners = new Vec3d[8];
        int i = 0;

        Box viewerBounds = viewer.getBoundingBox();
        Box targetBounds = target.getBoundingBox();

        if (viewer instanceof PlayerEntity player) {

            Vec3d eyePos = player.getEyePos();

            for (int j = -1; j < 2; j += 2) {
                for (int k = -1; k < 2; k += 2) {
                    for (int l = -1; l < 2; l += 2) {

                        viewerCorners[i] = eyePos.add(PLAYER_HEAD_SIZE * j, PLAYER_HEAD_SIZE * k, PLAYER_HEAD_SIZE * l);
                        i++;
                    }
                }
            }
        } else {

            for (int j = 0; j < 2; j ++) {
                for (int k = 0; k < 2; k ++) {
                    for (int l = 0; l < 2; l ++) {

                        viewerCorners[i] = new Vec3d(j == 0 ? viewerBounds.minX : viewerBounds.maxX, k == 0 ? viewerBounds.minY : viewerBounds.maxY, l == 0 ? viewerBounds.minZ : viewerBounds.maxZ);
                        i++;
                    }
                }
            }
        }

        i = 0;
        Vec3d[] targetCorners = new Vec3d[8];

        for (int j = 0; j < 2; j ++) {
            for (int k = 0; k < 2; k ++) {
                for (int l = 0; l < 2; l ++) {

                    targetCorners[i] = new Vec3d(j == 0 ? targetBounds.minX : targetBounds.maxX, k == 0 ? targetBounds.minY : targetBounds.maxY, l == 0 ? targetBounds.minZ : targetBounds.maxZ);
                    i++;
                }
            }
        }

        for (i = 0; i < 8; i++) {

            if (isLineBlocked(viewer, viewer.world, viewerCorners[i], targetCorners[i])) continue;

            return false;
        }


        return true;
    }

    public static boolean isLineBlocked(LivingEntity entity, World world, Vec3d start, Vec3d target) {

        int i = 200;
        while (start.squaredDistanceTo(target) > 0.01 && i-- > 0) {

            BlockHitResult hit = world.raycast(new RaycastContext(start, target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));

            if (hit != null) {

                BlockState state = world.getBlockState(hit.getBlockPos());

                if (blocksVision(state, world, hit.getBlockPos())) return true;

                start = Vec3d.ofCenter(hit.getBlockPos());
                start = start.add(start.relativize(target).normalize());
            } else {
                break;
            }
        }

        return false;
    }

    public static boolean blocksVision(BlockState state, World world, BlockPos pos) {

        return state.getCollisionShape(world, pos) != VoxelShapes.empty() && !(state.getBlock() instanceof AbstractGlassBlock || state.getBlock() instanceof PaneBlock);
    }
}
