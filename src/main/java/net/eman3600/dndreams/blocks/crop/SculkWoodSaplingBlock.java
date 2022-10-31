package net.eman3600.dndreams.blocks.crop;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class SculkWoodSaplingBlock extends SaplingBlock {

    /**
     * Access widened by fabric-transitive-access-wideners-v1 to accessible
     * Access widened by architectury to accessible
     *
     * @param generator
     * @param settings
     */
    public SculkWoodSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.SCULK);
    }
}
