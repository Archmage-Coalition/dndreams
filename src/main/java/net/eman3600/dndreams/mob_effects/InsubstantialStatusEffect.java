package net.eman3600.dndreams.mob_effects;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.mixin.LivingEntityMixin;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityMixinI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class InsubstantialStatusEffect extends StatusEffect implements StickyStatusEffect {

    public InsubstantialStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        entity.setVelocity(new Vec3d(entity.getVelocity().x, 0, entity.getVelocity().z));
    }

    @Override
    public boolean shouldStick(LivingEntity entity) {
        return ((LivingEntityMixinI)entity).isTrulyInsideWall();
    }
}
