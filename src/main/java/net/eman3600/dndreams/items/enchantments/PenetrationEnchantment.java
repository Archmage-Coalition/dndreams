package net.eman3600.dndreams.items.enchantments;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

public class PenetrationEnchantment extends AliasedEnchantment {

    public PenetrationEnchantment(Rarity weight, int maxLevel, EquipmentSlot[] slotTypes) {
        super(weight, maxLevel, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof DivineWeaponItem;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity entity && level > 0) {

            int i = 40 + (user.getRandom().nextInt(60) * level);
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SMOTE, i, 0));
        }

        super.onTargetDamaged(user, target, level);
    }

    @Override
    public int getMinPower(int level) {
        return 12 + (level - 1) * 14;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 13;
    }
}
