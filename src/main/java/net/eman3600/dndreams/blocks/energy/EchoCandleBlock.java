package net.eman3600.dndreams.blocks.energy;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class EchoCandleBlock extends RitualCandleBlock implements BlockEntityProvider {
    public EchoCandleBlock(ParticleEffect particle, Settings settings) {
        super(particle, settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
