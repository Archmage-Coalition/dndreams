package net.eman3600.dndreams.items;

import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.mixin_interfaces.BloodMoonWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CorruptSword extends SwordItem {
    private static final int LUST_DURATION = 30;
    private static final int MAX_DURATION = 100;
    private static final int DEBUFF_DURATION = 200;

    public CorruptSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    private float getCorruptDamage(LivingEntity attacker) {
        return (float)attacker.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) + getAttackDamage();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        float multiplier;
        try {
            multiplier = target.getDamageTracker().getMostRecentDamage().getDamage() / getCorruptDamage(attacker);
        } catch (Exception e) {
            multiplier = 2.0F;
        }

        if (attacker.hasStatusEffect(ModStatusEffects.BLOODLUST)) {
            StatusEffectInstance effect = attacker.getStatusEffect(ModStatusEffects.BLOODLUST);
            int newDuration = effect.getDuration() + (int)(multiplier * LUST_DURATION);
            if (newDuration >= MAX_DURATION) {
                attacker.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLOODLUST, MAX_DURATION,
                        1));
                addStatusEffects(target, attacker);
            } else {
                if (effect.getAmplifier() > 0) {
                    addStatusEffects(target, attacker);
                }
                attacker.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLOODLUST, newDuration,
                        effect.getAmplifier()));
            }

        } else {
            int newDuration = (int)(multiplier * LUST_DURATION);
            attacker.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLOODLUST, newDuration,
                    0));
        }

        return super.postHit(stack, target, attacker);
    }

    private void addStatusEffects(LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, DEBUFF_DURATION, 0), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, DEBUFF_DURATION, 0), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, DEBUFF_DURATION, 0), attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world instanceof ServerWorld && hand == Hand.MAIN_HAND) {
            System.out.println("Blood Moon: " + ((BloodMoonWorld)world).isBloodMoon());
            System.out.println("Blood Moon Chance: " + ((BloodMoonWorld)world).getBloodMoonChance());
        }

        return super.use(world, user, hand);
    }
}
