package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.CosmicFountainBlock;
import net.eman3600.dndreams.blocks.energy.CosmicFountainPoleBlock;
import net.eman3600.dndreams.blocks.portal.CosmicPortalBlock;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CosmicFountainPoleBlockEntity extends BlockEntity {
    private int ticks = 0;
    private int piece = 0;

    public CosmicFountainPoleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COSMIC_FOUNTAIN_POLE_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, CosmicFountainPoleBlockEntity entity) {
        entity.tick(world);
    }

    private void tick(World world) {
        if (piece == 0) {
            return;
        } else if (getCachedState().get(CosmicFountainPoleBlock.FUNCTIONAL)) {
            searchForCore(world);
        }

        ticks++;

        if (ticks >= 60) {
            ticks = 0;

            BlockPos corePos = pos.up(piece);
            BlockState state = world.getBlockState(corePos);

            if (state.getBlock() != ModBlocks.COSMIC_FOUNTAIN || !state.get(CosmicFountainBlock.FUNCTIONAL)) {
                disable(world);
            }
        }
    }

    public void disable(World world) {
        ticks = 0;
        piece = 0;
        world.setBlockState(pos, getCachedState().with(CosmicFountainPoleBlock.FUNCTIONAL, false));

        ((CosmicPortalBlock) ModBlocks.COSMIC_PORTAL).scatterBreak(world, pos);
    }

    public void searchForCore(World world) {
        BlockPos vec = pos.up();
        int i = 1;

        while (world.getBlockState(vec).getBlock() == ModBlocks.COSMIC_FOUNTAIN_POLE) {
            vec = vec.up();
            i++;

            if (world.isOutOfHeightLimit(vec)) {
                disable(world);
                return;
            }
        }

        if (world.getBlockState(vec).getBlock() == ModBlocks.COSMIC_FOUNTAIN) {
            enable(world, world.getBlockState(vec), i);
        } else {
            disable(world);
        }
    }

    public void enable(World world, BlockState fountain, int i) {
        piece = i;
        ticks = 0;

        world.setBlockState(pos, getCachedState().with(CosmicFountainPoleBlock.FUNCTIONAL, true).with(CosmicFountainPoleBlock.FACING, fountain.get(CosmicFountainBlock.FACING)));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("ticks", ticks);
        nbt.putInt("piece", piece);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        piece = nbt.getInt("piece");
    }
}
