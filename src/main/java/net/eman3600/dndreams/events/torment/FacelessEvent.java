package net.eman3600.dndreams.events.torment;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.entities.spawners.FacelessSpawner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public interface FacelessEvent {

    FacelessEvent OBSERVATION = new FacelessEvent() {

        private static final int PLAYER_DISTANCE = 6;

        @Override
        public int attempt(PlayerEntity player, ServerWorld world, TormentComponent torment) {


            for (int i = 0; i < 10; i++) {

                BlockPos attemptPos = FacelessSpawner.findSpawnFrom(world, world.random, player.getBlockPos());

                if (attemptPos != null && !FacelessEntity.daylightAt(world, attemptPos) && world.getNonSpectatingEntities(PlayerEntity.class, new Box(attemptPos.getX() - PLAYER_DISTANCE, attemptPos.getY() - PLAYER_DISTANCE, attemptPos.getZ() - PLAYER_DISTANCE, attemptPos.getX() + PLAYER_DISTANCE, attemptPos.getY() + PLAYER_DISTANCE, attemptPos.getZ() + PLAYER_DISTANCE)).size() <= 0) {

                    FacelessEntity faceless = new FacelessEntity(world, player, "observation");

                    faceless.refreshPositionAndAngles(attemptPos, 0, 0);
                    world.spawnEntityAndPassengers(faceless);
                    torment.setFacelessEntity(faceless);

                    return 1;
                }
            }

            return 0;
        }

        @Override
        public boolean canOccur(float sanity) {
            return sanity < 85 && sanity >= 5;
        }
    };

    FacelessEvent ENGAGEMENT = new FacelessEvent() {

        private static final int PLAYER_DISTANCE = 6;

        @Override
        public int attempt(PlayerEntity player, ServerWorld world, TormentComponent torment) {

            for (int i = 0; i < 12; i++) {

                BlockPos attemptPos = FacelessSpawner.findSpawnFrom(world, world.random, player.getBlockPos());

                if (attemptPos != null && !FacelessEntity.daylightAt(world, attemptPos) && world.getNonSpectatingEntities(PlayerEntity.class, new Box(attemptPos.getX() - PLAYER_DISTANCE, attemptPos.getY() - PLAYER_DISTANCE, attemptPos.getZ() - PLAYER_DISTANCE, attemptPos.getX() + PLAYER_DISTANCE, attemptPos.getY() + PLAYER_DISTANCE, attemptPos.getZ() + PLAYER_DISTANCE)).size() <= 0) {

                    FacelessEntity faceless = new FacelessEntity(world, player, "engagement");

                    faceless.refreshPositionAndAngles(attemptPos, 0, 0);
                    world.spawnEntityAndPassengers(faceless);
                    torment.setFacelessEntity(faceless);

                    return 1;
                }
            }

            return 0;
        }

        @Override
        public boolean canOccur(float sanity) {
            return sanity < 5;
        }
    };







    /**
     * Attempts to start an Acharos event
     * @param player the victim player
     * @param world the world in which the event occurs
     * @param torment the victim's torment component
     * @return the number of entities spawned
     */
    int attempt(PlayerEntity player, ServerWorld world, TormentComponent torment);

    boolean canOccur(float sanity);
}
