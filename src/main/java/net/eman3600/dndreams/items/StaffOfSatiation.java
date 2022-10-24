package net.eman3600.dndreams.items;

import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.items.interfaces.PowerCostItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StaffOfSatiation extends Item implements ManaCostItem, PowerCostItem {

    public StaffOfSatiation(Item.Settings settings) {
        super(settings);
        
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    @Override
    public int getBaseManaCost() {
        return 25;
    }

    @Override
    public float getBasePowerCost() {
        return 10;
    }
}
