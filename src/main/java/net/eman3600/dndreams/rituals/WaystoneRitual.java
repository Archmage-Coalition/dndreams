package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WaystoneRitual extends AbstractRitual {
    public WaystoneRitual() {
        super(250, new Ring(Ring.INNER_RING, CandleTuning.END));
    }

    @Override
    public void onCast(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        Vec3d vec = Vec3d.ofCenter(pos);
    }
}
