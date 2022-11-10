package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
public abstract class RocketItemMixin extends Item {
    public RocketItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void dndreams$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getPlayer().hasStatusEffect(ModStatusEffects.INSUBSTANTIAL)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
