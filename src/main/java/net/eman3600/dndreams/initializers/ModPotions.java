package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.mixin.BrewingRegistryMixin;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {
    public static Potion VOID_FLOW = registerPotion("void_flow", ModStatusEffects.VOID_FLOW, 600, 0);
    public static Potion VOID_FLOW_LONG = registerPotion("long_void_flow", "void_flow", ModStatusEffects.VOID_FLOW, 900, 0);

    public static Potion DREAMY = registerPotion("dreamy", ModStatusEffects.DREAMY, 1800, 0);
    public static Potion DREAMY_LONG = registerPotion("long_dreamy", "dreamy", ModStatusEffects.DREAMY, 3600, 0);
    public static Potion DREAMY_STRONG = registerPotion("strong_dreamy", "dreamy", ModStatusEffects.DREAMY, 1500, 1);

    public static Potion SPIRIT_WARD = registerPotion("spirit_ward", ModStatusEffects.SPIRIT_WARD, 3600, 0);
    public static Potion SPIRIT_WARD_LONG = registerPotion("long_spirit_ward", "spirit_ward", ModStatusEffects.SPIRIT_WARD, 9600, 0);

    public static Potion MEMORY = registerPotion("memory", ModStatusEffects.MEMORY, 3600, 0);
    public static Potion MEMORY_LONG = registerPotion("long_memory", "memory", ModStatusEffects.MEMORY, 9600, 0);
    public static Potion MEMORY_STRONG = registerPotion("strong_memory", "memory", ModStatusEffects.MEMORY, 2400, 1);

    public static Potion SILENCE = registerPotion("silence", ModStatusEffects.SILENCE, 1, 0);
    public static Potion SILENCE_STRONG = registerPotion("strong_silence", "silence", ModStatusEffects.SILENCE, 1, 1);

    public static Potion LIFEMANA = registerPotion("lifemana", ModStatusEffects.LIFEMANA, 900, 0);
    public static Potion LIFEMANA_LONG = registerPotion("long_lifemana", "lifemana", ModStatusEffects.LIFEMANA, 1800, 0);

    public static Potion DECAY = registerPotion("decay", StatusEffects.WITHER, 300, 0);
    public static Potion DECAY_LONG = registerPotion("long_decay", "decay", StatusEffects.WITHER, 500, 0);
    public static Potion DECAY_STRONG = registerPotion("strong_decay", "decay", StatusEffects.WITHER, 200, 1);





    public static Potion registerPotion(String name, StatusEffect effect, int duration, int amplifier) {
        return Registry.register(Registry.POTION, new Identifier(Initializer.MODID, name), new Potion(new StatusEffectInstance(effect, duration, amplifier)));
    }

    public static Potion registerPotion(String name, String baseName, StatusEffect effect, int duration, int amplifier) {
        return Registry.register(Registry.POTION, new Identifier(Initializer.MODID, name), new Potion(baseName, new StatusEffectInstance(effect, duration, amplifier)));
    }

    public static void registerPotions() {
        System.out.println("Registering potions for " + Initializer.MODID);

        registerPotionRecipes();
    }

    private static void registerPotionRecipes() {
        // Void Flow
        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.LIQUID_VOID, ModPotions.VOID_FLOW);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.VOID_FLOW, Items.REDSTONE, ModPotions.VOID_FLOW_LONG);

        // Dreamy
        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.DREAM_POWDER, ModPotions.DREAMY);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DREAMY, Items.REDSTONE, ModPotions.DREAMY_LONG);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DREAMY, Items.GLOWSTONE_DUST, ModPotions.DREAMY_STRONG);

        // Memory
        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.SCULK_POWDER, ModPotions.MEMORY);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MEMORY, Items.REDSTONE, ModPotions.MEMORY_LONG);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MEMORY, Items.GLOWSTONE_DUST, ModPotions.MEMORY_STRONG);

        // Silence
        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.NIGHTMARE_FUEL, ModPotions.SILENCE);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.SILENCE, Items.GLOWSTONE_DUST, ModPotions.SILENCE_STRONG);

        // Spirit Ward
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.SILENCE, Items.FERMENTED_SPIDER_EYE, ModPotions.SPIRIT_WARD);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.SPIRIT_WARD, Items.REDSTONE, ModPotions.SPIRIT_WARD_LONG);

        // Soulpour
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.VOID_FLOW, Items.FERMENTED_SPIDER_EYE, ModPotions.LIFEMANA);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.VOID_FLOW_LONG, Items.FERMENTED_SPIDER_EYE, ModPotions.LIFEMANA_LONG);

        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.LIFEMANA, Items.REDSTONE, ModPotions.LIFEMANA_LONG);

        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.WITHER_ROSE, ModPotions.DECAY);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DECAY, Items.REDSTONE, ModPotions.DECAY_LONG);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DECAY, Items.GLOWSTONE_DUST, ModPotions.DECAY_STRONG);
    }
}
