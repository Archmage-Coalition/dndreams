package net.eman3600.dndreams.mob_effects;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModStatusEffects;
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
            EntityComponents.TORMENT.get(entity).addTorment(-35.0f * amplifier);
        }
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        int i;
        if (this == ModStatusEffects.SILENCE && target.isPlayer()) {
            applyUpdateEffect(target, amplifier);
        }
    }
}
