package net.eman3600.dndreams.items.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public interface AirSwingItem {
    void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @Nullable Entity hit);

    static Vec3d rayZVector(float yaw, float pitch) {
        Vec3d rot = new Vec3d(0, 0, 0.1d);
        rot = rot.rotateX((float)Math.toRadians(-pitch));
        rot = rot.add(0, 0, 1);
        rot = rot.rotateY((float)Math.toRadians(-yaw));

        return rot;
    }

    static Vec3d rayXVector(float yaw, float pitch) {
        return rayZVector(yaw + 90, pitch);
    }

    @Nullable
    static EntityHitResult castWithDistance(@NotNull Entity caster, double distance, Predicate<Entity> predicate) {

        Vec3d pos = caster.getCameraPosVec(0);
        Vec3d angle = caster.getRotationVec(1.0f).multiply(distance);
        Vec3d extent = pos.add(angle.x, angle.y, angle.z);
        Box box = caster.getBoundingBox().stretch(angle).expand(1.0, 1.0, 1.0);

        return ProjectileUtil.raycast(caster, pos, extent, box, predicate, distance * distance);
    }
}
