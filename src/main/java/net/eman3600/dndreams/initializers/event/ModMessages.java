package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.networking.packet_c2s.*;
import net.eman3600.dndreams.networking.packet_s2c.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModMessages {

    public static final Identifier AIR_SWING_ID = new Identifier(MODID, "air_swing");
    public static final Identifier DODGE_ID = new Identifier(MODID, "dodge");
    public static final Identifier AIR_JUMP_ID = new Identifier(MODID, "air_jump");
    public static final Identifier GALE_BOOST_ID = new Identifier(MODID, "gale_boost");
    public static final Identifier ASCEND_ID = new Identifier(MODID, "ascend");

    public static final Identifier DRAGON_FLASH_ID = new Identifier(MODID, "dragon_flash");
    public static final Identifier CROWNED_SLASH_ID = new Identifier(MODID, "crowned_slash");
    public static final Identifier CROWNED_BEAM_ID = new Identifier(MODID, "crowned_beam");
    public static final Identifier TESLA_SLASH_ID = new Identifier(MODID, "tesla_slash");
    public static final Identifier CLOUD_SLASH_ID = new Identifier(MODID, "cloud_slash");
    public static final Identifier HEMORRHAGE_SCAR_ID = new Identifier(MODID, "hemorrhage_scar");
    public static final Identifier HEMORRHAGE_SLASH_ID = new Identifier(MODID, "hemorrhage_slash");
    public static final Identifier ANCIENT_PORTAL_SOUND_ID = new Identifier(MODID, "ancient_portal_sound");
    public static final Identifier ENERGY_PARTICLE_ID = new Identifier(MODID, "energy_particle");
    public static final Identifier MOTION_UPDATE_ID = new Identifier(MODID, "motion_update");
    public static final Identifier DREAM_SHIFT_ID = new Identifier(MODID, "dream_shift");
    public static final Identifier BLOODY_LASER_ID = new Identifier(MODID, "bloody_laser");
    public static final Identifier GOLDEN_LAND_ID = new Identifier(MODID, "golden_land");
    public static final Identifier MANAGOLD_FLASH_ID = new Identifier(MODID, "managold_flash");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(AIR_SWING_ID, AirSwingPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DODGE_ID, DodgePacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(AIR_JUMP_ID, AirJumpPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(ASCEND_ID, AscendPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(GALE_BOOST_ID, GaleBoostPacket::receive);
    }

    @Environment(EnvType.CLIENT)
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DRAGON_FLASH_ID, DragonFlashPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(CROWNED_SLASH_ID, DelayedS2CPacket.create(CrownedSlashPacket::receive, CrownedSlashPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(CROWNED_BEAM_ID, DelayedS2CPacket.create(CrownedBeamPacket::receive, CrownedBeamPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(TESLA_SLASH_ID, DelayedS2CPacket.create(TeslaSlashPacket::receive, TeslaSlashPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(CLOUD_SLASH_ID, DelayedS2CPacket.create(CloudSlashPacket::receive, CloudSlashPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(HEMORRHAGE_SCAR_ID, DelayedS2CPacket.create(HemorrhageScarPacket::receive, HemorrhageScarPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(HEMORRHAGE_SLASH_ID, DelayedS2CPacket.create(HemorrhageSlashPacket::receive, HemorrhageSlashPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(ANCIENT_PORTAL_SOUND_ID, AncientPortalSoundPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_PARTICLE_ID, DelayedS2CPacket.create(EnergyParticlePacket::receive, EnergyParticlePacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(MOTION_UPDATE_ID, MotionUpdatePacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DREAM_SHIFT_ID, DreamShiftPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(BLOODY_LASER_ID, DelayedS2CPacket.create(BloodyLaserPacket::receive, BloodyLaserPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(GOLDEN_LAND_ID, DelayedS2CPacket.create(GoldenLandPacket::receive, GoldenLandPacket::pack));
        ClientPlayNetworking.registerGlobalReceiver(MANAGOLD_FLASH_ID, DelayedS2CPacket.create(ManagoldFlashPacket::receive, ManagoldFlashPacket::pack));
    }
}
