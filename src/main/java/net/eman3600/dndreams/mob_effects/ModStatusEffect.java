package net.eman3600.dndreams.mob_effects;

import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class ModStatusEffect extends StatusEffect {

    public ModStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);


    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (this == ModStatusEffects.BLOODLUST && amplifier > 0) {
            if (entity.isPlayer()) {
                PlayerEntity player = (PlayerEntity)entity;
                if (player.isSneaking()) {
                    player.removeStatusEffect(this);
                    player.heal(4.0f);
                }
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        if (this == ModStatusEffects.BLOODLUST) {
            return true;
        }

        return false;
    }
}
