package net.eman3600.dndreams.items.magic_sword;

import net.eman3600.dndreams.entities.projectiles.CrownedSlashEntity;
import net.eman3600.dndreams.initializers.ModEnchantments;
import net.eman3600.dndreams.items.enchantments.AliasedEnchantment;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrownedEdgeItem extends SwordItem implements AirSwingItem, ManaCostItem, MagicDamageItem {
    private final int magicDamage;

    public CrownedEdgeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, int magicDamage, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.magicDamage = magicDamage;
    }


    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, Entity hit) {
        if (canAffordMana(user, stack) && user.getAttackCooldownProgress(0.5f) > 0.9f) {
            spendMana(user, stack);

            if (hit == null) {
                stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, user.getSoundCategory(), 1.0f, 1.5f);

            CrownedSlashEntity slash = new CrownedSlashEntity(user, world);
            world.spawnEntity(slash);
            slash.initFromStack(stack);
        }
    }

    @Override
    public int getBaseManaCost() {
        return 5;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipMana(stack));
        tooltip.add(getTooltipMagicDamage(stack));
    }


    @Override
    public float getBaseMagicDamage() {
        return magicDamage;
    }


    public static class CrownedEnchantment extends AliasedEnchantment {
        public CrownedEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
            super(weight, maxLevel, slotTypes);
        }

        @Override
        public boolean isAcceptableItem(ItemStack stack) {
            return stack.getItem() instanceof CrownedEdgeItem;
        }
    }
}
