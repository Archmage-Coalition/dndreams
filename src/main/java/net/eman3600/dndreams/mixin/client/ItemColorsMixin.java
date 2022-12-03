package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemColors.class)
public abstract class ItemColorsMixin {
    @Inject(method = "create", at = @At("RETURN"))
    private static void dndreams$create(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        ItemColors colors = cir.getReturnValue();

        colors.register((stack, tintIndex) -> tintIndex > 0 ? -1 : PotionUtil.getColor(PotionUtil.getPotionEffects(stack)), ModItems.BREW_INGESTED, ModItems.BREW_SPLASH, ModItems.BREW_LINGERING, ModItems.BREW_GAS, ModItems.BREW_LIQUID);
        colors.register(((stack, tintIndex) -> getInsanityRGB()), ModItems.NIGHTMARE_FUEL);
    }

    @Unique
    private static int getInsanityColor() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return -1;
        TormentComponent torment = EntityComponents.TORMENT.get(client.player);

        if (torment.getSanity() <= 25 || client.player.hasStatusEffect(ModStatusEffects.AETHER)) return -1;
        else if (torment.getSanity() > 60) return 0;

        float i = (60 - torment.getSanity())/35;
        return (int)(i * 255);
    }

    @Unique
    private static int getInsanityRGB() {
        int base = getInsanityColor();
        if (base < 0) return -1;
        else return base << 16 | base << 8 | base;
    }
}
