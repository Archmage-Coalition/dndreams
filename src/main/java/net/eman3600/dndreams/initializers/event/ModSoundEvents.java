package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.cardinal_components.MusicTrackerComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureKeys;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModSoundEvents {

    public static final SoundEvent MUSIC_DREAD_MOON = registerSound("music.dread_moon");
    public static final MusicSound PLAYER_DREAD_MOON = new MusicSound(MUSIC_DREAD_MOON, 0, 0, true);
    public static final SoundEvent MUSIC_INSANITY = registerSound("music.insanity");
    public static final MusicSound PLAYER_INSANITY = new MusicSound(MUSIC_INSANITY, 0, 0, true);
    public static final SoundEvent MUSIC_NIGHTSTORM = registerSound("music.nightstorm");
    public static final MusicSound PLAYER_NIGHTSTORM = new MusicSound(MUSIC_NIGHTSTORM, 0, 0, true);
    public static final SoundEvent MUSIC_FORTRESS = registerSound("music.fortress");
    public static final MusicSound PLAYER_FORTRESS = new MusicSound(MUSIC_FORTRESS, 0, 0, true);

    public static final SoundEvent RECORD_STORM = registerSound("record.storm");
    public static final SoundEvent RECORD_MIRE_MENTAL = registerSound("record.mire_mental");


    private static SoundEvent registerSound(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(MODID, id)));
    }

    public static void registerSounds() {

    }

    public static void registerSoundtrack() {

        // Nightmare Storm
        MusicTrackerComponent.registerTrack(PLAYER_NIGHTSTORM, player -> {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return player.getWorld().getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY && torment.isInStorm();

        }, player -> {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return torment.isInStorm() || player.getWorld().getRainGradient(0f) > .6f;

        });

        // Dread Moon
        MusicTrackerComponent.registerTrack(PLAYER_DREAD_MOON, player -> {
            if (player.world != null) {
                BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(player.getWorld());

                return component.isBloodMoon() && player.getWorld().getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY;
            }

            return false;
        }, player -> false);

        // Insanity
        MusicTrackerComponent.registerTrack(PLAYER_INSANITY, player -> {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return torment.getAttunedSanity() < 25 && player.getWorld().getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY && player.getWorld().getRegistryKey() != ModDimensions.HAVEN_DIMENSION_KEY;

        }, player -> {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return torment.getAttunedSanity() < 25 || torment.getNightmareHaze() > 0;

        });

        // Nether Fortress
        MusicTrackerComponent.registerTrack(PLAYER_FORTRESS, player -> {
            return byFortress((ServerWorld) player.world, player.getBlockPos(), 30);

        }, player -> {
            return byFortress((ServerWorld) player.world, player.getBlockPos(), 100);

        });
    }

    /**
     * Gets whether a position is located inside or near a fortress
     * @param world the position's world
     * @param pos the position
     * @param range the maximum distance to a fortress
     * @return whether the position is within range of a fortress
     */
    public static boolean byFortress(ServerWorld world, BlockPos pos, int range) {
        if (world.getRegistryKey() != World.NETHER) return false;
        BlockPos posStructure = world.locateStructure(ModTags.FORTRESS, pos, range + 20, false);

        return (posStructure != null && posStructure.isWithinDistance(pos, range)) || world.getStructureAccessor().getStructureContaining(pos, StructureKeys.FORTRESS).hasChildren();
    }
}
