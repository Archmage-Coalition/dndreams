package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.items.interfaces.PowerCostItem;
import net.eman3600.dndreams.items.interfaces.SanityCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightstringBow extends MagicBow implements PowerCostItem, SanityCostItem {
    public LightstringBow(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getProjectile() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    public float pullTime() {
        return 10.0F;
    }

    @Override
    public float getBasePowerCost() {
        return 0.5f;
    }



    @Override
    protected boolean canAfford(PlayerEntity player, ItemStack stack) {
        return canAffordPower(player, stack);
    }

    @Override
    protected void payAmmo(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            spendSanity(player, stack);
            spendPower(player, stack);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipPower(world, stack));
    }

    @Override
    public float getBaseSanityCost() {
        return 0.25f;
    }

    @Override
    public boolean isPermanent(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isOptional(ItemStack stack) {
        return true;
    }
}
