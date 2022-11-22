package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.networking.packet_c2s.AirSwingPacket;
import net.eman3600.dndreams.networking.packet_s2c.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {

    public static final Identifier AIR_SWING_ID = new Identifier(Initializer.MODID, "air_swing");

    public static final Identifier DRAGON_FLASH_ID = new Identifier(Initializer.MODID, "dragon_flash");
    public static final Identifier CROWNED_SLASH_ID = new Identifier(Initializer.MODID, "crowned_slash");
    public static final Identifier CROWNED_BEAM_ID = new Identifier(Initializer.MODID, "crowned_beam");
    public static final Identifier ANCIENT_PORTAL_SOUND_ID = new Identifier(Initializer.MODID, "ancient_portal_sound");
    public static final Identifier ENERGY_PARTICLE_ID = new Identifier(Initializer.MODID, "energy_particle");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(AIR_SWING_ID, AirSwingPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DRAGON_FLASH_ID, DragonFlashPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(CROWNED_SLASH_ID, CrownedSlashPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(CROWNED_BEAM_ID, CrownedBeamPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ANCIENT_PORTAL_SOUND_ID, AncientPortalSoundPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_PARTICLE_ID, EnergyParticlePacket::receive);
    }
}
