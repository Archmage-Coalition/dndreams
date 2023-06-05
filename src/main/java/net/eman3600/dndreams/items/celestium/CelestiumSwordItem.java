package net.eman3600.dndreams.items.celestium;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CelestiumSwordItem extends SwordItem implements DivineWeaponItem, AirSwingItem {

    private static final double DISTANCE = 9;

    public CelestiumSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void swingItem(ServerPlayerEntity user, Hand hand, ServerWorld world, ItemStack stack, @Nullable Entity hit) {

        if (hit == null) {

            EntityHitResult cast = AirSwingItem.castWithDistance(user, DISTANCE, entity -> !entity.isSpectator() && entity.canHit());

            if (cast != null && cast.getEntity() instanceof LivingEntity target) {

                Vec3d vec = user.getPos().subtract(target.getPos());
                vec = vec.subtract(0, vec.y, 0).normalize().multiply(1.75).add(target.getPos());

                user.requestTeleportAndDismount(vec.x, vec.y, vec.z);

                user.attack(target);
                world.playSound(null, vec.x, vec.y, vec.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.7f, 1.3f);

                Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(user);

                if (optional.isPresent() && optional.get().isEquipped(ModItems.DISSOCIATION_CHARM) && !user.hasStatusEffect(ModStatusEffects.DISCORDANT)) {
                    user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.DISCORDANT, 80));
                } else {
                    user.damage(DamageSource.FALL, 3.0f);
                }

                stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.0"));
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.1"));
    }
}
