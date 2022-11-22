package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AmethystSourceRitual extends AbstractRitual {
    public AmethystSourceRitual() {
        super(1500, new Ring(Ring.INNER_RING, CandleTuning.OVERWORLD), new Ring(Ring.MIDDLE_RING, CandleTuning.END), new Ring(Ring.OUTER_RING));
    }

    @Override
    public boolean onCast(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {

        BlockState state = world.getBlockState(pos.down());
        if (state.isOf(Blocks.AMETHYST_BLOCK)) {
            world.setBlockState(pos.down(), Blocks.BUDDING_AMETHYST.getDefaultState());
            return true;
        }

        return false;
    }
}
