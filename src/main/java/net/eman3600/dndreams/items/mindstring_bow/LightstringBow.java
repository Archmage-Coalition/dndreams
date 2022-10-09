package net.eman3600.dndreams.items.mindstring_bow;

import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.items.interfaces.PowerCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightstringBow extends MindstringBow implements PowerCostItem {
    public LightstringBow(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getProjectile() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    @Override
    public int getManaCost() {
        return 7;
    }

    public float pullProgressDivisor() {
        return 10.0F;
    }

    @Override
    public float getPowerCost() {
        return 0.5f;
    }

    @Override
    protected boolean canAfford(PlayerEntity player) {
        return super.canAfford(player) && canAffordPower(player);
    }

    @Override
    protected void payAmmo(PlayerEntity player) {
        if (player != null) {
            EntityComponents.MANA.get(player).useMana(getManaCost());
            EntityComponents.INFUSION.get(player).usePower(getPowerCost());
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(getTooltipPower(world));
    }
}
