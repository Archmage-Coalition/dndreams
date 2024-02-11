package net.eman3600.dndreams.mob_effects;

import net.eman3600.dndreams.events.damage_sources.AfflictionProjectileDamageSource;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.Nullable;

public class InstantModStatusEffect extends InstantStatusEffect {
    public InstantModStatusEffect(StatusEffectCategory statusEffectCategory, int i) {
        super(statusEffectCategory, i);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (this == ModStatusEffects.SILENCE && entity.isPlayer()) {
            EntityComponents.TORMENT.get(entity).lowerSanity(-20f * (amplifier + 1));
        } else if (this == ModStatusEffects.IMPENDING && entity.isPlayer()) {
            EntityComponents.TORMENT.get(entity).lowerSanity(30f * (amplifier + 1));
        } else if (this == ModStatusEffects.IMMOLATION) {
            entity.setOnFireFor(14 * (amplifier + 1));
        } else if (this == ModStatusEffects.CLEANSING) {
            entity.clearStatusEffects();
        } else if (this == ModStatusEffects.AFFLICTION) {
            EntityComponents.ROT.maybeGet(entity).ifPresent(rot -> {
                if (!rot.isImmune()) {
                    entity.damage(DamageSourceAccess.AFFLICTION, 2 * (1 + amplifier));
                }
            });
        } else if (this == ModStatusEffects.PURITY) {
            EntityComponents.ROT.maybeGet(entity).ifPresent(rot -> {
                rot.healRot(4 * (1 + amplifier));
            });
        }
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        int i;
        if (((this == ModStatusEffects.SILENCE || this == ModStatusEffects.IMPENDING) && target.isPlayer()) || this == ModStatusEffects.CLEANSING) {
            applyUpdateEffect(target, amplifier);
        } else if (this == ModStatusEffects.IMMOLATION) {
            i = (int)(proximity * (double)(14 * (amplifier + 1)) + 0.5);
            target.setOnFireFor(i);
        } else if (this == ModStatusEffects.AFFLICTION) {
            i = (int)(proximity * (double)(2 * (amplifier + 1)) + 0.5);
            EntityComponents.ROT.maybeGet(target).ifPresent(rot -> {
                if (!rot.isImmune()) {
                    if (source == null) {
                        target.damage(DamageSourceAccess.AFFLICTION, i);
                    } else {
                        target.damage(AfflictionProjectileDamageSource.magic(source, attacker), i);
                    }
                }
            });
        } else if (this == ModStatusEffects.PURITY) {
            i = (int)(proximity * (double)(4 * (amplifier + 1)) + 0.5);
            EntityComponents.ROT.maybeGet(target).ifPresent(rot -> {
                rot.healRot(i);
            });
        }
    }
}
