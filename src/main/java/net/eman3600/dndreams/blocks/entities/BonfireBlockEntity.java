package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.BonfireBlock;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import static net.eman3600.dndreams.blocks.energy.BonfireBlock.STRONG;

public class BonfireBlockEntity extends BlockEntity {

    private int delayTicks = 60;

    public BonfireBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BONFIRE_ENTITY, pos, state);
    }






    public static void tick(World world, BlockPos blockPos, BlockState blockState, BonfireBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {

        if (getCachedState().get(STRONG)) {
            delayTicks--;
            markDirty();

            if (delayTicks <= 0) {
                world.setBlockState(pos, getCachedState().with(STRONG, false));
            }
        }
    }



    @Environment(EnvType.CLIENT)
    public static void tickClient(World world, BlockPos blockPos, BlockState blockState, BonfireBlockEntity entity) {
        try {
            ClientWorld client = (ClientWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tickClient(client);
        } catch (ClassCastException ignored) {}
    }

    @Environment(EnvType.CLIENT)
    private void tickClient(ClientWorld world) {
        int i;
        Random random = world.random;
        if (random.nextFloat() < (getCachedState().get(STRONG) ? 0.11f: 0.03f)) {
            for (i = 0; i < random.nextInt(2) + 2; ++i) {
                CampfireBlock.spawnSmokeParticle(world, pos, getCachedState().get(STRONG), false);
            }
        }
    }




    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("DelayTicks", delayTicks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.delayTicks = nbt.getInt("DelayTicks");
    }
}
