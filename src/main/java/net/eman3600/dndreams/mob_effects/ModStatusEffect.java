package net.eman3600.dndreams.mob_effects;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
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
        } else if (this == ModStatusEffects.IMPENDING && entity.isPlayer()) {
            PlayerEntity player = (PlayerEntity)entity;
            EntityComponents.TORMENT.get(player).addPerSecond(0.5f * (amplifier + 1));
        } else if (this == ModStatusEffects.VOID_FLOW) {
            if (!entity.isPlayer()) {
                entity.damage(DamageSource.MAGIC, 1.0f);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        if (this == ModStatusEffects.BLOODLUST
        || this == ModStatusEffects.IMPENDING) {
            return true;
        } else if (this == ModStatusEffects.VOID_FLOW) {
            i = duration % 15;
            if (i == 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        if (this == ModStatusEffects.VOID_FLOW && entity.isPlayer()) {
            ManaComponent component = EntityComponents.MANA.get(entity);

            if (component.getMana() > component.getManaMax()) {
                entity.damage(DamageSource.MAGIC, 0.1f * ((float)component.getMana() - component.getManaMax()));
            }
        }
    }
}
