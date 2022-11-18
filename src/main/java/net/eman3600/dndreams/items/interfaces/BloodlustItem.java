package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.util.ModDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface BloodlustItem {
    float DAMAGE = 2.5f;
    DamageSource CRIMSON_SACRIFICE = new ModDamageSource("crimson_sacrifice").setBypassesArmor().setBypassesProtection().setUnblockable();

    default TypedActionResult<ItemStack> inflictBloodlust(World world, PlayerEntity player, ItemStack stack) {
        if (!hasBloodlust(player)) {
            boolean bl = false;
            if (world != null) {
                BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(world);

                bl = component.isBloodMoon();
            }

            player.timeUntilRegen = 5;
            player.damage(CRIMSON_SACRIFICE, DAMAGE);
            player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLOODLUST, bl ? 280 : 140));

            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.pass(stack);
    }

    default boolean hasBloodlust(LivingEntity entity) {
        return entity.hasStatusEffect(ModStatusEffects.BLOODLUST);
    }

    default Text getTooltipBloodlust(World world) {
        if (world instanceof ClientWorldAccess access && hasBloodlust(access.getClient().player)) {
            return Text.translatable("tooltip.dndreams.bloodlust.active");
        } else {
            return Text.translatable("tooltip.dndreams.bloodlust");
        }
    }
}
