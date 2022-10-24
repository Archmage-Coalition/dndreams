package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.World;

public class DreamSaplingBlock extends SaplingBlock {
    public DreamSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        try {
            if (world instanceof World level)
                return floor.isOf(ModBlocks.DREAM_GRASS) || level.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY;
            else if (world instanceof ChunkRegion region) {
                ServerWorld server = region.toServerWorld();
                return floor.isOf(ModBlocks.DREAM_GRASS) || server.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY;
            }


            //System.out.println(world);
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
