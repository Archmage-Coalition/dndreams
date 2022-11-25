package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class AmethystSourceRitual extends Ritual {
    public AmethystSourceRitual() {
        super(1500, new Ring(Ring.INNER_RING, CandleTuning.OVERWORLD), new Ring(Ring.MIDDLE_RING, CandleTuning.END), new Ring(Ring.OUTER_RING));
    }

    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity) {

        BlockState state = world.getBlockState(pos.down());
        if (state.isOf(Blocks.AMETHYST_BLOCK)) {
            world.setBlockState(pos.down(), Blocks.BUDDING_AMETHYST.getDefaultState());
            return true;
        }

        return false;
    }
}
