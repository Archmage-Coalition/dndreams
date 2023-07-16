package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ShadePlantBlock extends PlantBlock {
    public ShadePlantBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(ModBlocks.SHADE_MOSS);
    }
}
