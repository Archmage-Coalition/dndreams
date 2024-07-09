package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.cardinal_components.MusicTrackerComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModSoundEvents {

    public static final SoundEvent MUSIC_DREAD_MOON = registerSound("music.dread_moon");
    public static final MusicSound PLAYER_DREAD_MOON = new MusicSound(MUSIC_DREAD_MOON, 0, 0, true);
    public static final SoundEvent MUSIC_INSANITY = registerSound("music.insanity");
    public static final MusicSound PLAYER_INSANITY = new MusicSound(MUSIC_INSANITY, 0, 0, true);

    public static final SoundEvent RECORD_STORM = registerSound("record.storm");
    public static final SoundEvent RECORD_MIRE_MENTAL = registerSound("record.mire_mental");


    private static SoundEvent registerSound(String id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(new Identifier(MODID, id)));
    }

    public static void registerSounds() {

    }

    public static void registerSoundtrack() {

        // Dread Moon
        MusicTrackerComponent.registerTrack(PLAYER_DREAD_MOON, player -> {
            if (player.world != null) {
                BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(player.world);

                return component.isBloodMoon();
            }

            return false;
        }, player -> false);

        // Insanity
        MusicTrackerComponent.registerTrack(PLAYER_INSANITY, player -> {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return torment.getAttunedSanity() < 25;

        }, player -> {
            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return torment.getAttunedSanity() < 25 || torment.getNightmareHaze() > 0;

        });
    }
}
