package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.mob_effects.InstantModStatusEffect;
import net.eman3600.dndreams.mob_effects.ModStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {
    public static StatusEffect IMPENDING = register(4240, "impending", new ModStatusEffect(StatusEffectCategory.HARMFUL, 11141290));
    public static StatusEffect BLOODLUST = register(4241, "bloodlust", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 15597568));
    public static StatusEffect SUPPRESSED = register(4242, "suppressed", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0));
    public static StatusEffect LOOMING = register(4243, "looming", new ModStatusEffect(StatusEffectCategory.HARMFUL, 11141290));
    public static StatusEffect SPIRIT_WARD = register(4244, "spirit_ward", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 11184640));
    public static StatusEffect DREAMY = register(4245, "dreamy", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 13369480));
    public static StatusEffect VOID_FLOW = register(4246, "void_flow", new ModStatusEffect(StatusEffectCategory.HARMFUL, 5592405));
    public static StatusEffect MEMORY = register(4247, "memory", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 26214));
    public static StatusEffect SILENCE = register(4248, "silence", new InstantModStatusEffect(StatusEffectCategory.BENEFICIAL, 12303291));
    public static StatusEffect LIFEMANA = register(4249, "lifemana", new ModStatusEffect(StatusEffectCategory.HARMFUL, 17510));

    private static StatusEffect register(int rawId, String name, StatusEffect entry) {
        return (StatusEffect) Registry.register(Registry.STATUS_EFFECT, new Identifier(Initializer.MODID, name), entry);
    }

    public static void registerEffects() {
        System.out.println("Registering effects for " + Initializer.MODID);
    }
}
