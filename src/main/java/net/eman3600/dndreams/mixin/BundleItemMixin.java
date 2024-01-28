package net.eman3600.dndreams.mixin;

import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin extends Item {
    public BundleItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getItemOccupancy", at = @At(value = "RETURN"), cancellable = true)
    private static void dndreams$getItemOccupancy(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!stack.isOf(Items.BUNDLE) && cir.getReturnValue() > 1) cir.setReturnValue(1);
    }
}
