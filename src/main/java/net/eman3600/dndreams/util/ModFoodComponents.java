package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

import java.util.HashMap;
import java.util.Map;

public class ModFoodComponents {

    public static final Map<FoodComponent, Float> FOODS_TO_SANITY = new HashMap<>();




    public static final FoodComponent NIGHTMARE_FUEL = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(1.0F)
            .alwaysEdible().build(), -25);
    public static final FoodComponent RAW_FROG = registerSanityFood(new FoodComponent.Builder().hunger(2).saturationModifier(0.3F)
            .build(), -6);
    public static final FoodComponent COOKED_FROG = registerSanityFood(new FoodComponent.Builder().hunger(7).saturationModifier(0.8F)
            .build(), 4.5f);
    public static final FoodComponent GOLD_FRUIT = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(2.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0), 1.0F)
            .alwaysEdible().build(), 6);
    public static final FoodComponent SUCCULENT_APPLE = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.DREAMY, 400, 0), 1.0F)
            .alwaysEdible().build(), 8);
    public static final FoodComponent CAKE_APPLE = registerSanityFood(new FoodComponent.Builder().hunger(14).saturationModifier(0.75F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.DREAMY, 2400, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2400, 0), 1.0F)
            .alwaysEdible().build(), 15);
    public static final FoodComponent CAKEWOOD_LEAVES = registerSanityFood(new FoodComponent.Builder().hunger(1).saturationModifier(0.5F)
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 160, 0), 1.0F)
            .alwaysEdible().snack().build(), 1.5f);
    public static final FoodComponent DRAGONFRUIT = registerSanityFood(new FoodComponent.Builder().hunger(9).saturationModifier(2.5F)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 140, 0), 1.0F)
            .alwaysEdible().build(), 3);
    public static final FoodComponent POISON_APPLE = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(0.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 300, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build(), -16);
    public static final FoodComponent WITHER_BUD = registerSanityFood(new FoodComponent.Builder().hunger(2).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 300, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build(), -16);
    public static final FoodComponent RAVAGED_FLESH = registerSanityFood(new FoodComponent.Builder().hunger(4).saturationModifier(0.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().meat().build(), -16);
    public static final FoodComponent LOTUS_FLOWER = registerSanityFood(new FoodComponent.Builder().hunger(3).saturationModifier(0.6f)
            .build(), 1.5f);
    public static final FoodComponent STAR_FRUIT = registerSanityFood(new FoodComponent.Builder().hunger(6).saturationModifier(1.0F)
            .build(), 6);



    public static FoodComponent cakewood(float planks) {
        return registerSanityFood(new FoodComponent.Builder().hunger((int)(3 * planks)).saturationModifier(0.6f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int)(80 * planks)), 1.0f)
                .alwaysEdible()
                .build(), planks * 1.5f);
    }

    public static FoodComponent registerSanityFood(FoodComponent component, float sanity) {
        FOODS_TO_SANITY.put(component, sanity);
        return component;
    }
}
