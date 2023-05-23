package net.eman3600.dndreams.entities.spawners;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.Spawner;
import org.jetbrains.annotations.Nullable;

public class TormentorSpawner implements Spawner {

    private int cooldown = 0;

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

            if (component.getAttunedSanity() <= 50) {

                int j = random.nextBetween(1, component.getMaxTormentors());

                for (int i = 0; i < j; i++) {

                    BlockPos attemptPos = findSpawnFrom(world, world.random, playerPos);

                    if (attemptPos != null && TormentComponent.canSpawnTormentor(world, attemptPos)) {

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
        BlockPos choice = center.add(random.nextBetween(-120, 120), random.nextBetween(-40, 40), random.nextBetween(-120, 120));

        return findValidSpawn(world, choice, choice.getY() + 40);
    }

    @Nullable
    private BlockPos findValidSpawn(ServerWorld world, BlockPos choice, int maxHeight) {

        if (choice.getY() >= world.getTopY() || maxHeight < choice.getY()) {
            return null;
        } else if (!SpawnHelper.isClearForSpawn(world, choice, world.getBlockState(choice), world.getFluidState(choice), ModEntities.TORMENTOR)) {
            return findValidSpawn(world, choice.add(0, 1, 0), maxHeight);
        }

        return choice;
    }
}
