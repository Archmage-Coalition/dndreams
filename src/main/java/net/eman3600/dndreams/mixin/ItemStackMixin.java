package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract boolean isDamageable();

    @Unique
    private static final float POWER_DIVISOR = 100f;

    @Inject(at = @At("HEAD"), cancellable = true, method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z")
    public void injectDamage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        ItemStack stack = (ItemStack)(Object)this;
        if (isDamageable() && stack.isIn(ModTags.POWER_USING_TOOLS)) {
            InfusionComponent infusion = EntityComponents.INFUSION.get(player);
            if (infusion.getPower() >= amount / POWER_DIVISOR) {
                if (amount > 0) {
                    int i = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
                    int j = amount;

                    for(int k = 0; i > 0 && k < amount; ++k) {
                        if (UnbreakingEnchantment.shouldPreventDamage(stack, i, random)) {
                            j--;
                        }
                    }

                    if (j > 0) {
                        infusion.usePower(j / POWER_DIVISOR);
                    }
                }

                info.setReturnValue(false);
                return;
            }
        }
        if (isDamageable() && stack.isIn(ModTags.MANA_USING_TOOLS)) {
            ManaComponent mana = EntityComponents.MANA.get(player);
            if (mana.getMana() >= amount) {
                if (amount > 0) {
                    int i = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
                    int j = amount;

                    for(int k = 0; i > 0 && k < amount; ++k) {
                        if (UnbreakingEnchantment.shouldPreventDamage(stack, i, random)) {
                            j--;
                        }
                    }

                    if (j > 0) {
                        mana.useMana(j);
                    }
                }

                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void dndreams$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntity player = context.getPlayer();

        if (player != null && ModStatusEffects.shouldRestrict(player)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
