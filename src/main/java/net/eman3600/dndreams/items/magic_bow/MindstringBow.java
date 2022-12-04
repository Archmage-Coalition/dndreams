package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.items.interfaces.SanityCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MindstringBow extends MagicBow implements SanityCostItem {
    public MindstringBow(Settings settings) {
        super(settings);
    }

    public ItemStack getProjectile() {
        return new ItemStack(Items.ARROW);
    }

    protected boolean canAfford(PlayerEntity player, ItemStack stack) {
        return canAffordSanity(player, stack);
    }

    protected void payAmmo(PlayerEntity player, ItemStack stack) {
        if (player != null) {
            spendSanity(player, stack);
        }
    }

    public float pullTime() {
        return 15f;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey(stack) + ".tooltip"));
    }

    @Override
    public float getBaseSanityCost() {
        return .75f;
    }

    @Override
    public boolean isPermanent(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isOptional(ItemStack stack) {
        return false;
    }
}
