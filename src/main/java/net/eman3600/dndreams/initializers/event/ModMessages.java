package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.networking.packet_c2s.AirJumpPacket;
import net.eman3600.dndreams.networking.packet_c2s.AirSwingPacket;
import net.eman3600.dndreams.networking.packet_c2s.DodgePacket;
import net.eman3600.dndreams.networking.packet_s2c.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModMessages {

    public static final Identifier AIR_SWING_ID = new Identifier(MODID, "air_swing");
    public static final Identifier DODGE_ID = new Identifier(MODID, "dodge");
    public static final Identifier AIR_JUMP_ID = new Identifier(MODID, "air_jump");

    public static final Identifier DRAGON_FLASH_ID = new Identifier(MODID, "dragon_flash");
    public static final Identifier CROWNED_SLASH_ID = new Identifier(MODID, "crowned_slash");
    public static final Identifier CROWNED_BEAM_ID = new Identifier(MODID, "crowned_beam");
    public static final Identifier TESLA_SLASH_ID = new Identifier(MODID, "tesla_slash");
    public static final Identifier ANCIENT_PORTAL_SOUND_ID = new Identifier(MODID, "ancient_portal_sound");
    public static final Identifier ENERGY_PARTICLE_ID = new Identifier(MODID, "energy_particle");
    public static final Identifier MOTION_UPDATE_ID = new Identifier(MODID, "motion_update");
    public static final Identifier DREAM_SHIFT_ID = new Identifier(MODID, "dream_shift");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(AIR_SWING_ID, AirSwingPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DODGE_ID, DodgePacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(AIR_JUMP_ID, AirJumpPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DRAGON_FLASH_ID, DragonFlashPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(CROWNED_SLASH_ID, CrownedSlashPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(CROWNED_BEAM_ID, CrownedBeamPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(TESLA_SLASH_ID, TeslaSlashPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ANCIENT_PORTAL_SOUND_ID, AncientPortalSoundPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_PARTICLE_ID, EnergyParticlePacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(MOTION_UPDATE_ID, MotionUpdatePacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DREAM_SHIFT_ID, DreamShiftPacket::receive);
    }
}
