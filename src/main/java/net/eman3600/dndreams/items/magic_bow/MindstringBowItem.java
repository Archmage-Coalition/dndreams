package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.entities.projectiles.MindstrungArrowEntity;
import net.eman3600.dndreams.items.interfaces.SanityCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MindstringBowItem extends MagicBowItem implements SanityCostItem {
    public MindstringBowItem(Settings settings) {
        super(settings);
    }

    protected boolean canAfford(PlayerEntity player, ItemStack stack) {
        return canAffordSanity(player, stack);
    }

    protected void payAmmo(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            spendSanity(player, stack);
        }
    }

    public int pullTime() {
        return 10;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
        tooltip.add(getTooltipSanity(stack));
    }

    @Override
    public float getBaseSanityCost() {
        return .5f;
    }

    @Override
    public boolean isSanityOptional(ItemStack stack) {
        return true;
    }

    public PersistentProjectileEntity createDefaultArrow(World world, ItemStack stack, LivingEntity shooter) {

        return new MindstrungArrowEntity(world, shooter);
    }
}
