package net.eman3600.dndreams.blocks;

import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldView;

public class CloudOreBlock extends OreBlock {

    public CloudOreBlock(Settings settings) {
        super(settings);
    }

    public CloudOreBlock(Settings settings, IntProvider experience) {
        super(settings, experience);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (world instanceof ChunkRegion region) {
            ServerWorld server = region.toServerWorld();
            return (server.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY && super.canPlaceAt(state, world, pos));
        }

        return super.canPlaceAt(state, world, pos);
    }
}
