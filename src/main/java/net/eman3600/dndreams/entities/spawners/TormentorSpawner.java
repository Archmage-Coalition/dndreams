package net.eman3600.dndreams.entities.spawners;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.Spawner;
import org.jetbrains.annotations.Nullable;

public class TormentorSpawner implements Spawner {

    private int cooldown = 0;
    private static final float PLAYER_DISTANCE = 8;

    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {

        --cooldown;
        if (cooldown > 0) return 0;

        Random random = world.random;
        cooldown += (10 + random.nextInt(10)) * 20;

        int spawns = 0;
        for (PlayerEntity player: world.getPlayers()) {
            if (player.isSpectator()) continue;

            TormentComponent component = EntityComponents.TORMENT.getNullable(player);

            if (component == null) continue;

            BlockPos playerPos = player.getBlockPos();

            if (component.getMaxTormentors() > 0) {

                int j = random.nextBetween(1, component.getMaxTormentors());

                for (int i = 0; i < j; i++) {

                    BlockPos attemptPos = findSpawnFrom(world, world.random, playerPos);

                    if (attemptPos != null && TormentComponent.canSpawnTormentor(world, attemptPos) && world.getNonSpectatingEntities(PlayerEntity.class, new Box(attemptPos.getX() - PLAYER_DISTANCE, attemptPos.getY() - PLAYER_DISTANCE, attemptPos.getZ() - PLAYER_DISTANCE, attemptPos.getX() + PLAYER_DISTANCE, attemptPos.getY() + PLAYER_DISTANCE, attemptPos.getZ() + PLAYER_DISTANCE)).size() <= 0) {

                        TormentorEntity tormentor = ModEntities.TORMENTOR.create(world);
                        tormentor.refreshPositionAndAngles(attemptPos, 0, 0);
                        tormentor.initialize(world, world.getLocalDifficulty(attemptPos), SpawnReason.NATURAL, null, null);
                        world.spawnEntityAndPassengers(tormentor);
                        spawns++;
                    }
                }
            }
        }

        return spawns;
    }

    @Nullable
    private BlockPos findSpawnFrom(ServerWorld world, Random random, BlockPos center) {
        BlockPos choice = center.add(random.nextBetween(-90, 90), random.nextBetween(-40, 40), random.nextBetween(-90, 90));

        return findValidSpawn(world, choice, 40);
    }

    @Nullable
    private BlockPos findValidSpawn(ServerWorld world, BlockPos choice, int iterationsLeft) {

        if (choice.getY() >= world.getTopY() || iterationsLeft <= 0) {
            cooldown -= 80;
            return null;
        } else if (!SpawnHelper.isClearForSpawn(world, choice, world.getBlockState(choice), world.getFluidState(choice), ModEntities.TORMENTOR) || !TormentorEntity.isValidNaturalSpawn(ModEntities.TORMENTOR, world, SpawnReason.NATURAL, choice, world.random)) {
            return findValidSpawn(world, choice.add(0, world.isAir(choice) ? -1 : 1, 0), iterationsLeft - 1);
        }

        return choice;
    }
}
