package net.eman3600.dndreams.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getPossibleEntries", at = @At(value = "RETURN"), cancellable = true)
    private static void dndreams$getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        if (stack.isOf(Items.BOOK)) return;

        List<EnchantmentLevelEntry> list = cir.getReturnValue();
        List<EnchantmentLevelEntry> newList = new ArrayList<>();

        for (EnchantmentLevelEntry entry: list) {

            if (entry.enchantment.isAcceptableItem(stack)) {
                newList.add(entry);
            }
        }

        cir.setReturnValue(newList);
    }
}
