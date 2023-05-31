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

    private static final Identifier DNDREAMS_GUI_MANA_BAR = new Identifier(Initializer.MODID, "textures/gui/mana_bar.png");

    @Unique private static final Identifier DRAGON_FLASH_IMAGE = new Identifier(Initializer.MODID, "textures/gui/shader/dragon_flash.png");
    @Unique private static final Identifier INSANITY_VIGNETTE_TEXTURE = new Identifier(Initializer.MODID, "textures/gui/shader/insanity_vignette.png");

    @Unique
    private static final int MANA_X_OFFSET = 6;

    @Unique
    private static final int MANA_Y_OFFSET = 7;

    @Unique
    private static final int MANA_WIDTH = 110;

    @Unique
    private static final int MANA_HEIGHT = 8;

    @Unique
    private static final int TORMENT_X_OFFSET = 6;

    @Unique
    private static final int TORMENT_Y_OFFSET = 7;

    @Unique private static final int TORMENT_Y_BIG_OFFSET = 30;

    @Unique
    private static final int TORMENT_WIDTH = 14;

    @Unique
    private static final int TORMENT_HEIGHT = 14;

    @Unique private static final int REVIVE_X_OFFSET = TORMENT_X_OFFSET + TORMENT_WIDTH + 8;

    @Unique private static final int REVIVE_Y_OFFSET = TORMENT_Y_OFFSET;

    @Unique private static final int REVIVE_Y_BIG_OFFSET = TORMENT_Y_BIG_OFFSET;

    @Unique
    private static final int REVIVE_WIDTH = 16;

    @Unique
    private static final int REVIVE_HEIGHT = 16;

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
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, 0x009295);
            }

            if (manaComponent.shouldRender()) {
                int xPos = client.options.getMainArm().getValue() == Arm.LEFT ? scaledWidth - MANA_X_OFFSET - MANA_WIDTH : MANA_X_OFFSET;
                int yPos = scaledHeight - MANA_Y_OFFSET - MANA_HEIGHT;

                String string = mana + "/" + maxMana;
                int k = xPos + 9;
                int l = (yPos - MANA_HEIGHT - 3);
                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, 0x009295);
                //drawCenteredText(matrices, client.textRenderer, mana + "/" + maxMana, (xPosMana + MANA_WIDTH/2), (yPos - MANA_HEIGHT - 5), Color.MAGENTA.getRGB());
            }
        });



        EntityComponents.REVIVE.maybeGet(player).ifPresent(revive -> {
            if (revive.shouldDisplay()) {
                int amount = revive.remainingRevives();
                String string = revive.remainingRevives() >= 0 ? "x" + amount : "Debt";

                int k = client.options.getMainArm().getValue() == Arm.RIGHT ? REVIVE_X_OFFSET + REVIVE_WIDTH + 5 : scaledWidth - REVIVE_X_OFFSET - REVIVE_WIDTH - 5 - 10;
                int l = scaledHeight - (revive.shouldOffsetRender() ? REVIVE_Y_BIG_OFFSET : REVIVE_Y_OFFSET) - 12;

                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, revive.onCooldown() ? Color.MAGENTA.getRGB() : Color.RED.getRGB());
            }
        });
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 2, target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;"))
    private void dndreams$renderStatusBars(MatrixStack matrices, CallbackInfo callbackInfo) {
        int xPos = client.options.getMainArm().getValue() == Arm.LEFT ? scaledWidth - MANA_X_OFFSET - MANA_WIDTH : MANA_X_OFFSET;
        int yPos = scaledHeight - MANA_Y_OFFSET - MANA_HEIGHT;



        PlayerEntity player = getCameraPlayer();



        if (!player.hasStatusEffect(ModStatusEffects.LIFEMANA))
            EntityComponents.MANA.maybeGet(player).ifPresent(manaComponent -> {
                if (!manaComponent.shouldRender()) return;

                int mana = manaComponent.getMana();
                int maxMana = manaComponent.getManaMax();
                int vPos = manaComponent.getManaFrame() * 6;

                RenderSystem.setShaderTexture(0, DNDREAMS_GUI_MANA_BAR);
                RenderSystem.setShaderColor(1, 1, 1, 1.0f);
                drawTexture(matrices, xPos, yPos, MANA_WIDTH - 2, MANA_HEIGHT, MANA_WIDTH, MANA_HEIGHT, 230, 1944);
                drawTexture(matrices, xPos + 1, yPos + 1, 0, vPos, (int)((MANA_WIDTH -2) * Math.min((float)mana / maxMana, 1f)), MANA_HEIGHT -2, 230, 1944);
                if(player.hasStatusEffect(ModStatusEffects.SUPPRESSED))
                {
                    drawTexture(matrices, xPos, yPos, MANA_WIDTH - 2, 0, MANA_WIDTH, MANA_HEIGHT, 230, 1944);
                }
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
            int skipV2 = MathHelper.ceil((TORMENT_HEIGHT) * (1f - tormentMaxPercent));
            int skipV = MathHelper.ceil((TORMENT_HEIGHT) * (1f - tormentPercent));

            int x = component.isAttuned() ? 103 : 79;
            int y = component.isAwakened() ? 42 : 0;

            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);
            drawTexture(matrices, tormentXPos, tormentYPos, x, y, TORMENT_WIDTH, TORMENT_HEIGHT);
            drawTexture(matrices, tormentXPos, tormentYPos + skipV2, x, tormentV2 + skipV2 + y, TORMENT_WIDTH, (int)((TORMENT_HEIGHT) * tormentMaxPercent));
            drawTexture(matrices, tormentXPos, tormentYPos + skipV, x, tormentV + skipV + y, TORMENT_WIDTH, (int)((TORMENT_HEIGHT) * tormentPercent));
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        });

        EntityComponents.REVIVE.maybeGet(player).ifPresent(revive -> {
            if (!revive.shouldDisplay()) return;

            int reviveXPos = client.options.getMainArm().getValue() == Arm.RIGHT ? REVIVE_X_OFFSET : (scaledWidth - REVIVE_X_OFFSET - REVIVE_WIDTH);
            int reviveYPos = scaledHeight - REVIVE_HEIGHT - (revive.shouldOffsetRender() ? REVIVE_Y_BIG_OFFSET : REVIVE_Y_OFFSET);

            float vitalityPercent = revive.getDisplayedVitality() / 100;

            int skipV = MathHelper.ceil(REVIVE_HEIGHT * (1f - vitalityPercent));
            int u = revive.onCooldown() ? 134 : 117;

            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);

            drawTexture(matrices, reviveXPos, reviveYPos, u, 16, REVIVE_WIDTH, REVIVE_HEIGHT);
            drawTexture(matrices, reviveXPos, reviveYPos + skipV, u, skipV, REVIVE_WIDTH, (int)(REVIVE_HEIGHT * vitalityPercent));

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
