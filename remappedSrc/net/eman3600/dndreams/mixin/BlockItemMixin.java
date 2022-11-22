package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void dndreams$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (ModStatusEffects.shouldRestrict(context.getPlayer())) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
