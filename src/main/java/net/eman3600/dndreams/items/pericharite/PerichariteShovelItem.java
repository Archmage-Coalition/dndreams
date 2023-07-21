package net.eman3600.dndreams.items.pericharite;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;

public class PerichariteShovelItem extends ShovelItem {

    public PerichariteShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return this.miningSpeed;
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return true;
    }
}
