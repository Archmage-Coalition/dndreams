package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.mixin.BrewingRegistryMixin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {
    public static Potion DREAMY = registerPotion("dreamy", ModStatusEffects.DREAMY, 1800, 0);
    public static Potion DREAMY_LONG = registerPotion("long_dreamy", "dreamy", ModStatusEffects.DREAMY, 3600, 0);
    public static Potion DREAMY_STRONG = registerPotion("strong_dreamy", "dreamy", ModStatusEffects.DREAMY, 1500, 1);

    public static Potion MEMORY = registerPotion("memory", ModStatusEffects.MEMORY, 3600, 0);
    public static Potion MEMORY_LONG = registerPotion("long_memory", "memory", ModStatusEffects.MEMORY, 9600, 0);
    public static Potion MEMORY_STRONG = registerPotion("strong_memory", "memory", ModStatusEffects.MEMORY, 2400, 1);





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
        // Dreamy
        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.DREAM_POWDER, ModPotions.DREAMY);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DREAMY, Items.REDSTONE, ModPotions.DREAMY_LONG);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DREAMY, Items.GLOWSTONE_DUST, ModPotions.DREAMY_STRONG);

        // Memory
        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.SCULK_POWDER, ModPotions.MEMORY);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MEMORY, Items.REDSTONE, ModPotions.MEMORY_LONG);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MEMORY, Items.GLOWSTONE_DUST, ModPotions.MEMORY_STRONG);
    }
}
