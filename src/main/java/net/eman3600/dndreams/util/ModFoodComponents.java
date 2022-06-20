package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent NIGHTMARE_FUEL = (new FoodComponent.Builder()).hunger(6).saturationModifier(0.6F)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 300, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.IMPENDING, 600, 0), 1.0F)
            .alwaysEdible().build();
}
