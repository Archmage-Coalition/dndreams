package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent NIGHTMARE_FUEL = (new FoodComponent.Builder()).hunger(9).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 300, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.SUPPRESSED, 130, 0), 1.0F)
            .alwaysEdible().build();
    public static final FoodComponent SUCCULENT_APPLE = (new FoodComponent.Builder()).hunger(6).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.DREAMY, 400, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.SPIRIT_WARD, 400, 0), 1.0F)
            .alwaysEdible().build();
    public static final FoodComponent DRAGONFRUIT = (new FoodComponent.Builder()).hunger(12).saturationModifier(2.5F)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0), 1.0F)
            .alwaysEdible().build();
    public static final FoodComponent POISON_APPLE = (new FoodComponent.Builder()).hunger(6).saturationModifier(0.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.SUPPRESSED, 400, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.AETHER, 600, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build();
    public static final FoodComponent WITHER_BUD = (new FoodComponent.Builder()).hunger(2).saturationModifier(1.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 300, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build();
    public static final FoodComponent RAVAGED_FLESH = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.0F)
            .statusEffect(new StatusEffectInstance(ModStatusEffects.IMPENDING, 3600, 3), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0F)
            .alwaysEdible().build();
    public static final FoodComponent LOTUS_FLOWER = (new FoodComponent.Builder()).hunger(5).saturationModifier(0.8F)
            .build();
    public static final FoodComponent STAR_FRUIT = (new FoodComponent.Builder()).hunger(6).saturationModifier(1.0F)
            .build();
    public static FoodComponent cakewood(float planks) {
        return (new FoodComponent.Builder()).hunger((int)(3 * planks)).saturationModifier(0.6f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int)(80 * planks)), 1.0f)
                .alwaysEdible()
                .build();
    }
}
