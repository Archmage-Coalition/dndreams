package net.eman3600.dndreams.networking.packet_c2s;

import net.eman3600.dndreams.cardinal_components.ShockComponent;
import net.eman3600.dndreams.entities.projectiles.TeslaSlashEntity;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class AirSwingPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packet, PacketSender sender) {
        if (player.getMainHandStack().getItem() instanceof AirSwingItem item) {
            item.swingItem(player, Hand.MAIN_HAND, player.getWorld(), player.getMainHandStack(), null);
        }

        if (EntityComponents.SHOCK.isProvidedBy(player)) {
            World world = player.getWorld();
            ShockComponent shock = EntityComponents.SHOCK.get(player);
            if (shock.hasShock() && player.getAttackCooldownProgress(0.5f) > 0.9f) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, player.getSoundCategory(), 1.0f, 2.5f);

                TeslaSlashEntity slash = new TeslaSlashEntity(player, world, shock.dischargeShock((float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
                world.spawnEntity(slash);
            }
        }
    }
}
