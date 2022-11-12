package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.BossStateComponent;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EndermanEyesFeatureRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEyesFeatureRenderer.class)
public class EndermanEyesFeatureRendererMixin {
    private static final RenderLayer OTHER_SKIN = RenderLayer.getEyes(new Identifier(Initializer.MODID, "textures/entity/enderman/enderman_eyes_pure.png"));

    @Inject(method = "getEyesTexture", at = @At("HEAD"), cancellable = true)
    public void dndreams$greenEndermanEyes(CallbackInfoReturnable<RenderLayer> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            PlayerEntity player = client.player;
            if (player != null) {
                BossStateComponent component = WorldComponents.BOSS_STATE.get(player.getWorld().getScoreboard());
                if (component.dragonSlain()) {
                    cir.setReturnValue(OTHER_SKIN);
                }
            }
        }
    }
}
