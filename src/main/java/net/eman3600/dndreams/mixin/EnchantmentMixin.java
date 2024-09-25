package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.enchantment.*;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
    private void dndreams$isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if ((((Object)this) instanceof InfinityEnchantment && stack.getItem() instanceof ManaCostItem) || (((Object)this instanceof UnbreakingEnchantment || (Object)this instanceof MendingEnchantment) && stack.isIn(ModTags.AUTO_REPAIRING_TOOLS)) || (((Object)this instanceof SilkTouchEnchantment || (Object)this == Enchantments.FORTUNE) && stack.isIn(ModTags.SILKY_TOOLS))) {
            cir.setReturnValue(false);
        }
    }
}
