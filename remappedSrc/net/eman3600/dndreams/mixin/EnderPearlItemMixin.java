package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderPearlItem.class)
public abstract class EnderPearlItemMixin extends Item {

    public EnderPearlItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void dndreams$use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (ModStatusEffects.shouldRestrict(user)) {
            cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
        }
    }
}
