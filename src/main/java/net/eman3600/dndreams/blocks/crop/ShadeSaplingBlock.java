package net.eman3600.dndreams.blocks.crop;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.world.feature.tree.sapling_generator.ShadeSaplingGenerator;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;

public class ShadeSaplingBlock extends AzaleaBlock {

    private static final ShadeSaplingGenerator GENERATOR = new ShadeSaplingGenerator();

    public ShadeSaplingBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(ModBlocks.SHADE_MOSS) || super.canPlantOnTop(floor, world, pos);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        GENERATOR.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
    }
}
