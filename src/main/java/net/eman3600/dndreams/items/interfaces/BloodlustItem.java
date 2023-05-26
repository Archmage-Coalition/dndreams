package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.items.hellsteel.CorruptArmorItem;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface BloodlustItem {
    float DAMAGE = 2.5f;
    DamageSource CRIMSON_SACRIFICE = new DamageSource("crimson_sacrifice").setBypassesArmor().setBypassesProtection().setUnblockable();
    DamageSource CRIMSON_HEMORRHAGE = new DamageSource("crimson_hemorrhage").setBypassesArmor().setUsesMagic();

    default TypedActionResult<ItemStack> inflictBloodlust(World world, PlayerEntity player, ItemStack stack) {
        if (!hasBloodlust(player)) {
            boolean bl = false;
            if (world != null) {
                BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(world);

                bl = component.isBloodMoon();
            }

            player.timeUntilRegen = 5;
            player.damage(CRIMSON_SACRIFICE, DAMAGE);
            int duration = bl ? 280 : 140;
            duration += 40 * CorruptArmorItem.wornPieces(player);
            player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLOODLUST, duration));

            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.pass(stack);
    }

    default boolean hasBloodlust(LivingEntity entity) {
        return entity.hasStatusEffect(ModStatusEffects.BLOODLUST);
    }

    default Text getTooltipBloodlust(World world) {
        try {
            if (world.isClient && world instanceof ClientWorldAccess access && hasBloodlust(access.getPlayer())) {
                return Text.translatable("tooltip.dndreams.bloodlust.active");
            } else {
                return Text.translatable("tooltip.dndreams.bloodlust");
            }
        } catch (NullPointerException e) {
            return Text.literal("");
        }
    }

    static DamageSource hemorrhage(Entity attacker) {
        return new EntityDamageSource("crimson_hemorrhage", attacker).setUsesMagic();
    }
}
