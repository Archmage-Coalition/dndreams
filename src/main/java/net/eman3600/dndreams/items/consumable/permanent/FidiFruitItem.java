package net.eman3600.dndreams.items.consumable.permanent;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FidiFruitItem extends Item {
    public FidiFruitItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        EntityComponents.REVIVE.maybeGet(user).ifPresent(revive -> {
            if (revive.isEnabled()) {
                user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.DRAGONSOUL, 12000, 0));
                revive.setRecharging(true);
            } else {
                revive.setEnabled(true);
            }
        });

        return super.finishUsing(stack, world, user);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 48;
    }
}
