package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

import java.util.HashMap;
import java.util.Map;

public class ModFoodComponents {

    public static final Map<FoodComponent, Float> FOODS_TO_SANITY = new HashMap<>();




    public static final FoodComponent NIGHTMARE_FUEL = registerSanityFood(new FoodComponent.Builder().hunger(12).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.LOOMING, 600, 0, false, false, true), 1.0F)
            .alwaysEdible().build(), -20);
    public static final FoodComponent SUCCULENT_APPLE = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.DREAMY, 400, 0), 1.0F)
            .alwaysEdible().build(), 16);
    public static final FoodComponent DRAGONFRUIT = registerSanityFood(new FoodComponent.Builder().hunger(12).saturationModifier(2.5F)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0), 1.0F)
            .alwaysEdible().build(), -4);
    public static final FoodComponent POISON_APPLE = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(0.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.SUPPRESSED, 400, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build(), -16);
    public static final FoodComponent WITHER_BUD = registerSanityFood(new FoodComponent.Builder().hunger(2).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 300, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build(), -16);
    public static final FoodComponent RAVAGED_FLESH = registerSanityFood(new FoodComponent.Builder().hunger(4).saturationModifier(0.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.LOOMING, 3600, 3), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build(), -16);
    public static final FoodComponent LOTUS_FLOWER = registerSanityFood(new FoodComponent.Builder().hunger(5).saturationModifier(0.8F)
            .build(), 3);
    public static final FoodComponent STAR_FRUIT = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(1.0F)
            .build(), 8);



    public static FoodComponent cakewood(float planks) {
        return registerSanityFood(new FoodComponent.Builder().hunger((int)(3 * planks)).saturationModifier(0.6f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int)(80 * planks)), 1.0f)
                .alwaysEdible()
                .build(), planks * 2);
    }

    public static FoodComponent registerSanityFood(FoodComponent component, float sanity) {
        FOODS_TO_SANITY.put(component, sanity);
        return component;
    }
}
