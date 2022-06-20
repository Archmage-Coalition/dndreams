package net.eman3600.dndreams.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import org.jetbrains.annotations.NotNull;

public class Excalibur extends SwordItem {
    private static final float HEAL_AMOUNT = 1.0F;

    public Excalibur(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    private float getExcaliburDamage(LivingEntity attacker) {
        return (float)attacker.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) + getAttackDamage();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Excalibur heals the attacker an amount relative to the weapon's damage and the attacker's base attack
        float multiplier;
        try {
            multiplier = target.getDamageTracker().getMostRecentDamage().getDamage() / getExcaliburDamage(attacker);
        } catch (Exception e) {
            multiplier = 2.0F;
        }

        if (multiplier > 0)
            attacker.heal(HEAL_AMOUNT * multiplier);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
