package net.eman3600.dndreams.mob_effects;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModCriterion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModStatusEffect extends StatusEffect {

    public static final DamageSource COSMIC_OVERLOAD = new DamageSource("cosmic_overload").setUsesMagic().setBypassesArmor().setBypassesProtection().setUnblockable();


    public ModStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);

        if (this == ModStatusEffects.THIRD_EYE && entity instanceof ServerPlayerEntity player) {
            ModCriterion.INSANITY.trigger(player);
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (this == ModStatusEffects.VOID_FLOW) {

            if (!entity.isPlayer()) {
                entity.timeUntilRegen = 8;
                entity.damage(COSMIC_OVERLOAD, 1.0f);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        if (this == ModStatusEffects.VOID_FLOW) {
            i = duration % 15;
            if (i == 0) {
                return true;
            }
        }

        return this == ModStatusEffects.HAUNTED;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        if (this == ModStatusEffects.VOID_FLOW && entity.isPlayer()) {
            ManaComponent component = EntityComponents.MANA.get(entity);

            if (component.getMana() > component.getManaMax()) {
                entity.damage(COSMIC_OVERLOAD, 0.3f * ((float)component.getMana() - component.getManaMax()));
            }
        }

        if (this == ModStatusEffects.INSUBSTANTIAL && !entity.isOnGround()) {
            EntityComponents.SHOCK.maybeGet(entity).ifPresent(component -> component.setCushioned(true));
        }
    }
}
