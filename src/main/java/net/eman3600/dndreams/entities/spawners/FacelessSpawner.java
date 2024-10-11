package net.eman3600.dndreams.entities.spawners;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.misc.ShadeRiftEntity;
import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.events.torment.FacelessEvent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.Spawner;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FacelessSpawner implements Spawner {

    private int cooldown = 0;
    private static final float PLAYER_DISTANCE = 6;

    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {

        --cooldown;
        if (cooldown > 0) return 0;

        Random random = world.random;
        cooldown += (30 + random.nextInt(16)) * 20;

        int spawns = 0;

        for (ServerPlayerEntity player : world.getPlayers()) {

            if (player.isSpectator()) continue;

            TormentComponent component = EntityComponents.TORMENT.getNullable(player);

            if (component == null || FacelessEntity.daylightAt(player.world, player.getBlockPos())) continue;

            if (component.getAttunedSanity() < 85 && component.getFacelessCooldown() <= 0 && component.getFacelessEntity() == null && player.world.random.nextFloat() < component.getFacelessPrevalence()) {

                spawns += component.triggerFacelessEvent();

                if (spawns > 0) {

                    component.addTension(-5);
                } else {

                    component.addTension(1);
                }
            } else {
                component.addTension(1);
            }
        }

        return spawns;
    }

    @Nullable
    public static BlockPos findSpawnFrom(ServerWorld world, Random random, BlockPos center) {
        BlockPos choice = center.add(random.nextBetween(-24, 24), random.nextBetween(-10, 10), random.nextBetween(-24, 24));

        return findValidSpawn(world, choice, 40);
    }

    @Nullable
    private static BlockPos findValidSpawn(ServerWorld world, BlockPos choice, int iterationsLeft) {

        if (choice.getY() >= world.getTopY() || iterationsLeft <= 0) {
            return null;
        } else if (!SpawnHelper.isClearForSpawn(world, choice, world.getBlockState(choice), world.getFluidState(choice), ModEntities.SHADE_RIFT) || !ShadeRiftEntity.isValidNaturalSpawn(ModEntities.SHADE_RIFT, world, SpawnReason.NATURAL, choice, world.random)) {
            return findValidSpawn(world, choice.add(0, world.isAir(choice) ? -1 : 1, 0), iterationsLeft - 1);
        }

        return choice;
    }
}
