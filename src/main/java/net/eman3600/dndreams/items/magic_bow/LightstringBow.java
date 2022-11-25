package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.items.interfaces.PowerCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightstringBow extends ManastringBow implements PowerCostItem {
    public LightstringBow(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getProjectile() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    @Override
    public int getBaseManaCost() {
        return 5;
    }

    public float pullProgressDivisor() {
        return 10.0F;
    }

    @Override
    public float getBasePowerCost() {
        return 0.5f;
    }



    @Override
    protected boolean canAfford(PlayerEntity player, ItemStack stack) {
        return super.canAfford(player, stack) && canAffordPower(player, stack);
    }

    @Override
    protected void payAmmo(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            spendMana(player, stack);
            spendPower(player, stack);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(getTooltipPower(world, stack));
    }
}
