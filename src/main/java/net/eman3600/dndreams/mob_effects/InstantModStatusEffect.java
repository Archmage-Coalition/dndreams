package net.eman3600.dndreams.mob_effects;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
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
            EntityComponents.TORMENT.get(entity).addTorment(-35.0f * (amplifier + 1));
        } else if (this == ModStatusEffects.IMMOLATION) {
            entity.setOnFireFor(14 * (amplifier + 1));
        }
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        int i;
        if (this == ModStatusEffects.SILENCE && target.isPlayer()) {
            applyUpdateEffect(target, amplifier);
        } else if (this == ModStatusEffects.IMMOLATION) {
            i = (int)(proximity * (double)(14 * (amplifier + 1)) + 0.5);
            target.setOnFireFor(i);
        }
    }
}
