package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {

    @Inject(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/predicate/item/EnchantmentPredicate;test(Ljava/util/Map;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$test$silkCloud(ItemStack stack, CallbackInfoReturnable<Boolean> cir, Map<Enchantment, Integer> map, EnchantmentPredicate[] var3, int var4, int var5, EnchantmentPredicate enchantmentPredicate) {
        if (stack.isIn(ModTags.SILKY_TOOLS) && !map.containsKey(Enchantments.SILK_TOUCH)) {
            map.put(Enchantments.SILK_TOUCH, 1);
        }
    }
}
