package net.eman3600.dndreams.items.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public interface AirSwingItem {
    void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, Entity hit);

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
}
