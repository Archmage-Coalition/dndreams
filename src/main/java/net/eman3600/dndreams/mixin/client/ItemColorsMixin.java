package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemColors.class)
public abstract class ItemColorsMixin {
    @Inject(method = "create", at = @At("RETURN"))
    private static void dndreams$create(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        ItemColors colors = cir.getReturnValue();

        colors.register((stack, tintIndex) -> tintIndex > 0 ? -1 : PotionUtil.getColor(PotionUtil.getPotionEffects(stack)), ModItems.BREW_INGESTED, ModItems.BREW_SPLASH, ModItems.BREW_LINGERING);
    }
}
