package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.initializers.ModRituals;
import net.eman3600.dndreams.items.WaystoneItem;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WaystoneRitual extends AbstractRitual {


    public WaystoneRitual(int cost, Ring... rings) {
        super(cost, rings);
    }

    @Override
    public void onCast(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        Vec3d vec = Vec3d.ofCenter(pos);

        ItemStack stack = WaystoneItem.boundAt(pos);

        if (this == ModRituals.WAYSTONE_SIMPLE) {
            Inventory inv = blockEntity.getInv();

            stack.setDamage(inv.getStack(0).getDamage());
        }

        ItemEntity entity = new ItemEntity(world, vec.x, vec.y + 1, vec.z, stack);

        world.spawnEntity(entity);
    }
}
