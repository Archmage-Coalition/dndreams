package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class HavenSaplingBlock extends SaplingBlock {
    public HavenSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(ModTags.HAVEN_DIRT);
    }
}
