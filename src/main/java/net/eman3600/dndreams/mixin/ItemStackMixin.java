package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract boolean isDamageable();

    @Inject(at = @At("HEAD"), cancellable = true, method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z")
    public void injectDamage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        ItemStack stack = (ItemStack)(Object)this;
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
}
