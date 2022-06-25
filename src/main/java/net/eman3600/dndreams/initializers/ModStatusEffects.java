package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.mob_effects.ModStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {
    public static StatusEffect IMPENDING = register(4240, "impending", new ModStatusEffect(StatusEffectCategory.HARMFUL, 11141290));
    public static StatusEffect BLOODLUST = register(4241, "bloodlust", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 15597568));
    public static StatusEffect SUPPRESSED = register(4242, "suppressed", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0));

    private static StatusEffect register(int rawId, String name, StatusEffect entry) {
        return (StatusEffect) Registry.register(Registry.STATUS_EFFECT, new Identifier(Initializer.MODID, name), entry);
    }

    public static void registerEffects() {
        System.out.println("Registering effects for " + Initializer.MODID);
    }
}
