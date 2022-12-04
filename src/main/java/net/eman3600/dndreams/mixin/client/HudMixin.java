package net.eman3600.dndreams.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.mixin_interfaces.HudAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
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
public abstract class HudMixin extends DrawableHelper implements HudAccess {
    @Unique
    private static final Identifier DNDREAMS_GUI_ICONS = new Identifier(Initializer.MODID, "textures/gui/icons.png");

    @Unique private static final Identifier DRAGON_FLASH_IMAGE = new Identifier(Initializer.MODID, "textures/gui/shader/dragon_flash.png");
    @Unique private static final Identifier INSANITY_VIGNETTE_TEXTURE = new Identifier(Initializer.MODID, "textures/gui/shader/insanity_vignette.png");
    @Unique private static final Identifier ATTUNEMENT_VIGNETTE_TEXTURE = new Identifier(Initializer.MODID, "textures/gui/shader/attunement_vignette.png");

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
    private static final int TORMENT_Y_OFFSET = 7;

    @Unique private static final int TORMENT_Y_BIG_OFFSET = 28;

    @Unique
    private static final int TORMENT_WIDTH = 14;

    @Unique
    private static final int TORMENT_HEIGHT = 14;

    @Unique
    private static final int POWER_X_OFFSET = 8;

    @Unique
    private static final int POWER_Y_OFFSET = 8;

    @Unique
    private static final int POWER_WIDTH = 5;

    @Unique
    private static final int POWER_HEIGHT = 82;

    @Unique
    private int dragonFlashTicks = 0;



    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();


    @Shadow public float vignetteDarkness;

    @Override
    public void setDragonFlash(int ticks) {
        dragonFlashTicks = ticks;
    }


    @Inject(method = "renderExperienceBar", at = @At("TAIL"))
    public void dndreams$renderManaModifier(MatrixStack matrices, int x, CallbackInfo ci) {
        PlayerEntity player = getCameraPlayer();


        EntityComponents.MANA.maybeGet(player).ifPresent(manaComponent -> {
            int mana = manaComponent.getMana();
            int maxMana = manaComponent.getManaMax();
            int xpBonus = manaComponent.getXPBonus();

            if (xpBonus > 0) {
                String string = "+" + xpBonus;
                int k = x + 182 + 2;
                int l = this.scaledHeight - 31 + 1;
                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, Color.MAGENTA.getRGB());
            }

            if (manaComponent.shouldRender()) {
                int xPos = client.options.getMainArm().getValue() == Arm.LEFT ? scaledWidth - MANA_X_OFFSET - MANA_WIDTH : MANA_X_OFFSET;
                int yPos = scaledHeight - MANA_Y_OFFSET - MANA_HEIGHT;

                String string = mana + "/" + maxMana;
                int k = xPos + 9;
                int l = (yPos - MANA_HEIGHT - 5);
                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, Color.MAGENTA.getRGB());
                //drawCenteredText(matrices, client.textRenderer, mana + "/" + maxMana, (xPosMana + MANA_WIDTH/2), (yPos - MANA_HEIGHT - 5), Color.MAGENTA.getRGB());
            }
        });



        EntityComponents.INFUSION.maybeGet(player).ifPresent(infusionComponent -> {
            if (infusionComponent.infused()) {
                float power = infusionComponent.getRoundedPower();
                String string = power + "%";
                int k = POWER_X_OFFSET + POWER_WIDTH + 7;
                int l = POWER_Y_OFFSET + 4;
                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, infusionComponent.getInfusion().color.getRGB());
            }
        });
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 2, target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;"))
    private void dndreams$renderStatusBars(MatrixStack matrices, CallbackInfo callbackInfo) {
        int xPos = client.options.getMainArm().getValue() == Arm.LEFT ? scaledWidth - MANA_X_OFFSET - MANA_WIDTH : MANA_X_OFFSET;
        int yPos = scaledHeight - MANA_Y_OFFSET - MANA_HEIGHT;

        int vPos;

        PlayerEntity player = getCameraPlayer();

        if (player.hasStatusEffect(ModStatusEffects.SUPPRESSED)) {
            vPos = 10;
        } else {
            vPos = 5;
        }

        if (!player.hasStatusEffect(ModStatusEffects.LIFEMANA))
            EntityComponents.MANA.maybeGet(player).ifPresent(manaComponent -> {
                int mana = manaComponent.getMana();
                int maxMana = manaComponent.getManaMax();
                if (!manaComponent.shouldRender()) return;

                RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
                RenderSystem.setShaderColor(1, 1, 1, 1.0f);
                drawTexture(matrices, xPos, (yPos), 0, 0, MANA_WIDTH, MANA_HEIGHT);
                drawTexture(matrices, xPos, yPos, 0, vPos, (int)((MANA_WIDTH) * Math.min((float)mana / maxMana, 1f)), MANA_HEIGHT);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
            });

        int tormentXPos;

        if (client.options.getMainArm().getValue() != Arm.RIGHT) {
            tormentXPos = scaledWidth - TORMENT_X_OFFSET - TORMENT_WIDTH;
        } else {
            tormentXPos = TORMENT_X_OFFSET;
        }

        int tormentV = 28;
        int tormentV2 = 14;

        EntityComponents.TORMENT.maybeGet(player).ifPresent(component -> {
            int tormentYPos = scaledHeight - TORMENT_HEIGHT - (component.shouldOffsetRender() ? TORMENT_Y_BIG_OFFSET : TORMENT_Y_OFFSET);

            float tormentMaxPercent = (component.getMaxSanity() / TormentComponent.MAX_SANITY);
            float tormentPercent = (component.getSanity() / TormentComponent.MAX_SANITY);
            if (!component.shouldRender()) return;
            int skipV2 = MathHelper.ceil((TORMENT_HEIGHT) * (1f - tormentMaxPercent));
            int skipV = MathHelper.ceil((TORMENT_HEIGHT) * (1f - tormentPercent));

            int x = component.isAttuned() ? 103 : 79;

            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);
            drawTexture(matrices, tormentXPos, tormentYPos, x, 0, TORMENT_WIDTH, TORMENT_HEIGHT);
            drawTexture(matrices, tormentXPos, tormentYPos + skipV2, x, tormentV2 + skipV2, TORMENT_WIDTH, (int)((TORMENT_HEIGHT) * tormentMaxPercent));
            drawTexture(matrices, tormentXPos, tormentYPos + skipV, x, tormentV + skipV, TORMENT_WIDTH, (int)((TORMENT_HEIGHT) * tormentPercent));
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        });

        int powerXPos = POWER_X_OFFSET;
        int powerYPos = POWER_Y_OFFSET;

        EntityComponents.INFUSION.maybeGet(player).ifPresent(infusionComponent -> {
            float powerPercent = (infusionComponent.getPower() / infusionComponent.getPowerMax());
            if (!infusionComponent.infused()) return;
            int skipV = (int)(POWER_HEIGHT * (1f - powerPercent));

            Color color = infusionComponent.getInfusion().color;

            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
            RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f);
            drawTexture(matrices, powerXPos, powerYPos, 93, 0, POWER_WIDTH, POWER_HEIGHT);
            drawTexture(matrices, powerXPos, powerYPos + skipV, 98, skipV, POWER_WIDTH, (int)(POWER_HEIGHT * powerPercent));
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        });

        /*if (dragonFlashTicks > 0) {
            RenderSystem.setShaderTexture(0, DRAGON_FLASH_IMAGE);

            int n = scaledWidth / 2;
            int m = scaledHeight / 2;

            n -= 323;
            m -= 207;

            float alpha = 1;

            if (dragonFlashTicks <= 20) {
                alpha = (dragonFlashTicks / 20.0f);
            }

            RenderSystem.setShaderColor(1, 1, 1, alpha);

            drawTexture(matrices, n, m, 0, 0, scaledWidth, scaledHeight, scaledWidth, scaledHeight);

            RenderSystem.setShaderColor(1, 1, 1, 1);
        }*/
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void dndreams$tick(CallbackInfo ci) {
        if (dragonFlashTicks > 0) {
            dragonFlashTicks--;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignetteOverlay(Lnet/minecraft/entity/Entity;)V"))
    private void dndreams$render$vignette(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        renderInsanityVignette();
    }

    @Unique
    private void renderInsanityVignette() {
        PlayerEntity player = client.player;
        if (player == null) return;

        TormentComponent torment = EntityComponents.TORMENT.get(player);
        float g = torment.getSanityDamage();

        renderCustomVignette(g, g, g, INSANITY_VIGNETTE_TEXTURE);
    }

    @Unique
    private void renderCustomVignette(float r, float g, float b, Identifier texture) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(g, g, g, 1.0f);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, this.scaledHeight, -90.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -90.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(this.scaledWidth, 0.0, -90.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
