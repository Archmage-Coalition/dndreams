package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.items.AtlasItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Nameable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements Inventory, Nameable {

    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow @Final public PlayerEntity player;

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private void dndreams$dropSelectedItem(boolean entireStack, CallbackInfoReturnable<ItemStack> cir) {

        ItemStack stack = this.getMainHandStack();

        if (stack.getItem() instanceof AtlasItem item && item.isActive(stack)) {

            cir.setReturnValue(player.world.isClient ? stack : ItemStack.EMPTY);
            item.setForm(stack, AtlasItem.InstrumentForm.INACTIVE);
        }
    }
}
