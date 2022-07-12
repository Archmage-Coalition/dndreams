package net.eman3600.dndreams.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextVisitFactory;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.core.config.plugins.convert.HexConverter;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class HudMixin extends DrawableHelper {
    @Unique
    private static final Identifier DNDREAMS_GUI_ICONS = new Identifier(Initializer.MODID, "textures/gui/icons.png");

    @Unique
    private static final int MANA_X_OFFSET = 6;

    @Unique
    private static final int MANA_Y_OFFSET = 7;

    @Unique
    private static final int MANA_WIDTH = 71;

    @Unique
    private static final int MANA_HEIGHT = 5;

    @Unique
    private static final int TORMENT_X_OFFSET = 6;

    @Unique
    private static final int TORMENT_Y_OFFSET = 5;

    @Unique
    private static final int TORMENT_WIDTH = 13;

    @Unique
    private static final int TORMENT_HEIGHT = 13;



    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 2, target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;"))
    private void renderPre(MatrixStack matrices, CallbackInfo callbackInfo) {
        int xPos = MANA_X_OFFSET;
        int yPos = scaledHeight - MANA_Y_OFFSET - MANA_HEIGHT;

        int vPos;

        PlayerEntity player = getCameraPlayer();

        if (player.hasStatusEffect(ModStatusEffects.SUPPRESSED)) {
            vPos = 10;
        } else {
            vPos = 5;
        }

        if (client.options.getMainArm().getValue() == Arm.LEFT) {
            xPos = scaledWidth - xPos - MANA_WIDTH;
        }

        int xPosMana = xPos;
        if (!player.hasStatusEffect(ModStatusEffects.LIFEMANA))
            EntityComponents.MANA.maybeGet(player).ifPresent(manaComponent -> {
                RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
                RenderSystem.setShaderColor(1, 1, 1, 1.0f);
                drawTexture(matrices, xPosMana, (yPos), 0, 0, MANA_WIDTH, MANA_HEIGHT);
                drawTexture(matrices, xPosMana, yPos, 0, vPos, (int)((MANA_WIDTH) * Math.min((float)manaComponent.getMana() / manaComponent.getManaMax(), 1f)), MANA_HEIGHT);
                drawCenteredText(matrices, client.textRenderer, manaComponent.getMana() + "/" + manaComponent.getManaMax(), (xPosMana + MANA_WIDTH/2), (yPos - MANA_HEIGHT - 5), Color.MAGENTA.getRGB());
                RenderSystem.setShaderColor(1, 1, 1, 1);
                RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
            });

        int tormentXPos;

        if (client.options.getMainArm().getValue() == Arm.RIGHT) {
            tormentXPos = scaledWidth - TORMENT_X_OFFSET - TORMENT_WIDTH;
        } else {
            tormentXPos = TORMENT_X_OFFSET;
        }

        int tormentV;

        if (EntityComponents.TORMENT.get(player).isTormentForced()) {
            tormentV = 26;
        } else {
            tormentV = 13;
        }

        int tormentYPos = scaledHeight - TORMENT_Y_OFFSET - TORMENT_HEIGHT;

        EntityComponents.TORMENT.maybeGet(player).ifPresent(tormentComponent -> {
            float tormentPercent = (tormentComponent.getTorment() / TormentComponent.MAX_TORMENT);
            int skipV = (int)(TORMENT_HEIGHT * (1f - tormentPercent));

            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);
            drawTexture(matrices, tormentXPos, tormentYPos, 80, 0, TORMENT_WIDTH, TORMENT_HEIGHT);
            drawTexture(matrices, tormentXPos, tormentYPos + skipV, 80, tormentV + skipV, TORMENT_WIDTH, (int)(TORMENT_HEIGHT * tormentPercent));
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        });
    }
}
