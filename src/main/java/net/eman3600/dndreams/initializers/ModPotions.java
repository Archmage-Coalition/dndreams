package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.mixin.BrewingRegistryMixin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {
    public static Potion VOID_FLOW = registerPotion("void_flow", ModStatusEffects.VOID_FLOW, 600, 0);
    public static Potion VOID_FLOW_LONG = registerPotion("long_void_flow", "void_flow", ModStatusEffects.VOID_FLOW, 900, 0);
    public static Potion VOID_FLOW_STRONG = registerPotion("strong_void_flow", "void_flow", ModStatusEffects.VOID_FLOW, 400, 1);

    public static Potion DREAMY = registerPotion("dreamy", ModStatusEffects.DREAMY, 1800, 0);
    public static Potion DREAMY_LONG = registerPotion("long_dreamy", "dreamy", ModStatusEffects.DREAMY, 3600, 0);
    public static Potion DREAMY_STRONG = registerPotion("strong_dreamy", "dreamy", ModStatusEffects.DREAMY, 1500, 1);


    public static Potion SPIRIT_WARD = registerPotion("spirit_ward", ModStatusEffects.SPIRIT_WARD, 3600, 0);
    public static Potion SPIRIT_WARD_LONG = registerPotion("long_spirit_ward", "spirit_ward", ModStatusEffects.SPIRIT_WARD, 9600, 0);





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
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.VOID_FLOW, Items.GLOWSTONE_DUST, ModPotions.VOID_FLOW_STRONG);

        // Dreamy
        BrewingRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.DREAM_POWDER, ModPotions.DREAMY);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DREAMY, Items.REDSTONE, ModPotions.DREAMY_LONG);
        BrewingRegistryMixin.invokeRegisterPotionRecipe(ModPotions.DREAMY, Items.GLOWSTONE_DUST, ModPotions.DREAMY_STRONG);
    }
}
