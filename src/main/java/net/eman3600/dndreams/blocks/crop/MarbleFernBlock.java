package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class MarbleFernBlock extends FernBlock {
    public MarbleFernBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(ModTags.MARBLES);
    }
}
