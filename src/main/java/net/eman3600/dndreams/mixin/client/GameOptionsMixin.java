package net.eman3600.dndreams.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

    @Shadow protected MinecraftClient client;

    /*@Inject(method = "getClampedViewDistance", at = @At("RETURN"), cancellable = true)
    private void dndreams$getClampedViewDistance(CallbackInfoReturnable<Integer> cir) {
        if (ClientInitializer.drawAether(client.world)) {
            cir.setReturnValue(2);
        }
    }*/
}
