package net.eman3600.dndreams.items.charge;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractChargeItem extends Item {
    public AbstractChargeItem(Settings settings) {
        super(settings);
    }

    public abstract ItemStack charge(ItemStack stack, int amount);
    public abstract boolean canAffordCharge(ItemStack stack, int amount);
    public abstract ItemStack discharge(ItemStack stack, int amount);
    public abstract int getCharge(ItemStack stack);

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.dndreams.attuned_shard.tooltip", "§d" + getCharge(stack)));
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x8923D6;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }
}
