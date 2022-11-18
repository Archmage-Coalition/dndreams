package net.eman3600.dndreams.items.hellsteel;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.items.interfaces.MagicDamageItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CorruptSword extends SwordItem implements BloodlustItem, MagicDamageItem {


    public CorruptSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return inflictBloodlust(world, user, user.getStackInHand(hand));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (hasBloodlust(attacker)) {
            target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.AFFLICTION, 140), attacker);
            target.timeUntilRegen = 8;
            target.damage(DamageSource.magic(attacker, attacker), getMagicDamage(stack));
            target.takeKnockback(0.4f, MathHelper.sin(attacker.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(attacker.getYaw() * ((float) Math.PI / 180)));
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public float getBaseMagicDamage() {
        return 3;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipMagicDamage(stack));
        tooltip.add(getTooltipBloodlust(world));
    }
}
