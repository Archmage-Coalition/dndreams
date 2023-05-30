package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManastringBowItem extends MagicBowItem implements ManaCostItem {
    public ManastringBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getProjectile() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public int getBaseManaCost() {
        return 7;
    }

    protected boolean canAfford(PlayerEntity player, ItemStack stack) {
        return canAffordMana(player, stack);
    }

    protected void payAmmo(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            spendMana(player, stack);
        }
    }

    public float pullTime() {
        return 20.0F;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipMana(stack));
    }
}
