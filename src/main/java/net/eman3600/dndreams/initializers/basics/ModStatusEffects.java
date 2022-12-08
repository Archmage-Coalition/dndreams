package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mob_effects.InstantModStatusEffect;
import net.eman3600.dndreams.mob_effects.ModStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {
    public static StatusEffect IMPENDING = register("impending", new InstantModStatusEffect(StatusEffectCategory.HARMFUL, 0x7700e0));
    public static StatusEffect BLOODLUST = register("bloodlust", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 15597568));
    public static StatusEffect SUPPRESSED = register("suppressed", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0));
    public static StatusEffect LOOMING = register("looming", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0x4400c2));
    public static StatusEffect SPIRIT_WARD = register("spirit_ward", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 11184640));
    public static StatusEffect DREAMY = register("dreamy", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 13369480));
    public static StatusEffect VOID_FLOW = register("void_flow", new ModStatusEffect(StatusEffectCategory.HARMFUL, 5592405));
    public static StatusEffect MEMORY = register("memory", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 26214));
    public static StatusEffect SILENCE = register("silence", new InstantModStatusEffect(StatusEffectCategory.BENEFICIAL, 12303291));
    public static StatusEffect LIFEMANA = register("lifemana", new ModStatusEffect(StatusEffectCategory.HARMFUL, 17510));
    public static StatusEffect INSUBSTANTIAL = register("insubstantial", new ModStatusEffect(StatusEffectCategory.HARMFUL, 2289390));
    public static StatusEffect GRACE = register("grace", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 15658496));
    public static StatusEffect RESTRICTED = register("restricted", new ModStatusEffect(StatusEffectCategory.HARMFUL, 5570560));
    public static StatusEffect SPOTTED = register("spotted", new ModStatusEffect(StatusEffectCategory.HARMFUL, 21862));
    public static StatusEffect AETHER = register("aether", new ModStatusEffect(StatusEffectCategory.HARMFUL, 1118481));
    public static StatusEffect AFFLICTION = register("affliction", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0x352922));
    public static StatusEffect IMMOLATION = register("immolation", new InstantModStatusEffect(StatusEffectCategory.HARMFUL, 0xFF8A1A));
    public static StatusEffect GAS_MASK = register("gas_mask", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xEEEE11));
    public static StatusEffect REJUVENATION = register("rejuvenation", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFF1522));
    public static StatusEffect MORTAL = register("mortal", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0x440000));

    private static StatusEffect register(String name, StatusEffect entry) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(Initializer.MODID, name), entry);
    }

    public static void registerEffects() {
        System.out.println("Registering effects for " + Initializer.MODID);
    }

    public static boolean shouldRestrict(PlayerEntity player) {
        try {
            if (player.isCreative()) return false;
            return player.hasStatusEffect(RESTRICTED) || player.world.getRegistryKey() == ModDimensions.GATEWAY_DIMENSION_KEY;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
