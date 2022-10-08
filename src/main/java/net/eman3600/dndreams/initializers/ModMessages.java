package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.networking.packet_s2c.DragonFlashPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.impl.screenhandler.client.ClientNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {

    public static final Identifier DRAGON_FLASH_ID = new Identifier(Initializer.MODID, "dragon_flash");

    public static void registerC2SPackets() {

    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DRAGON_FLASH_ID, DragonFlashPacket::receive);
    }
}
