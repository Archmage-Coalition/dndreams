package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.MusicTrackerComponentI;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.MusicSound;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MusicTrackerComponent implements MusicTrackerComponentI {

    private final PlayerEntity player;
    private int track = -1;
    private static final List<MusicPredicate> musicPredicates = new ArrayList<>();
    private boolean dirty = false;

    public MusicTrackerComponent(PlayerEntity player) {

        this.player = player;
    }

    @Override
    public void serverTick() {

        if (player.world.getTime() % 20 == 0) {
            if (track >= 0 && !(musicPredicates.get(track).continueCriterian.test(player) || musicPredicates.get(track).startCriterian.test(player))) {

                setTrack(-1);
            }

            for (int i = 0; i < (track < 0 ? musicPredicates.size() : track); i++) {

                if (musicPredicates.get(i).startCriterian.test(player)) {
                    track = i;
                    markDirty();
                    break;
                }
            }
        }

        if (dirty) {
            dirty = false;
            EntityComponents.MUSIC_TRACKER.sync(player);
        }
    }

    private void setTrack(int track) {
        this.track = track;
        markDirty();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        track = tag.getInt("track");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("track", track);
    }

    public void markDirty() {
        dirty = true;
    }

    /**
     * Registers a track for use by the custom music engine.
     * Note that tracks are registered in descending order of priority.
     */
    public static void registerTrack(MusicSound musicTrack, Predicate<PlayerEntity> startCriterian, Predicate<PlayerEntity> continueCriterian) {

        musicPredicates.add(new MusicPredicate(musicTrack, startCriterian, continueCriterian));
    }


    private static class MusicPredicate {

        private final MusicSound musicTrack;
        private final Predicate<PlayerEntity> startCriterian;
        private final Predicate<PlayerEntity> continueCriterian;

        private MusicPredicate(MusicSound musicTrack, Predicate<PlayerEntity> startCriterian, Predicate<PlayerEntity> continueCriterian) {
            this.musicTrack = musicTrack;
            this.startCriterian = startCriterian;
            this.continueCriterian = continueCriterian;
        }
    }
}
