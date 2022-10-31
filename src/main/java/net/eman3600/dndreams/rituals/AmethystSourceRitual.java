package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AmethystSourceRitual extends AbstractRitual {
    public AmethystSourceRitual() {
        super(200, new Ring(Ring.INNER_RING), new Ring(Ring.MIDDLE_RING));
    }

    @Override
    public void onCast(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {

        Vec3d vec = Vec3d.ofCenter(pos);

        ItemStack stack = new ItemStack(Items.BUDDING_AMETHYST);

        ItemEntity entity = new ItemEntity(world, vec.x, vec.y + 1, vec.z, stack);

        world.spawnEntity(entity);
    }
}
