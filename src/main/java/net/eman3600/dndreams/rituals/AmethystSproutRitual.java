package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class AmethystSproutRitual extends Ritual {
    public AmethystSproutRitual() {
        super(1000, new Ring(Ring.INNER_RING, CandleTuning.OVERWORLD), new Ring(Ring.MIDDLE_RING, CandleTuning.END));
    }

    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        pos = pos.down();

        BlockState state = world.getBlockState(pos);
        if (state.isOf(Blocks.BUDDING_AMETHYST)) {
            boolean bl = false;

            for (Direction dir: Direction.values()) {
                BlockPos budPos = pos.offset(dir);
                BlockState budState = world.getBlockState(budPos);

                if (budState.isAir() || budState.isOf(Blocks.WATER) || budState.isOf(Blocks.SMALL_AMETHYST_BUD) || budState.isOf(Blocks.MEDIUM_AMETHYST_BUD) || budState.isOf(Blocks.LARGE_AMETHYST_BUD)) {
                    Optional<Boolean> waterlogProperty;
                    boolean waterlogged = budState.isOf(Blocks.WATER) || ((waterlogProperty = budState.getOrEmpty(Properties.WATERLOGGED)).isPresent() && waterlogProperty.get());

                    world.setBlockState(budPos, Blocks.AMETHYST_CLUSTER.getDefaultState().with(Properties.FACING, dir).with(Properties.WATERLOGGED, waterlogged));
                    bl = true;
                }
            }

            return bl;
        }

        return false;
    }
}
