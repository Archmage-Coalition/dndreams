package net.eman3600.dndreams.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.RotComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.HudAccess;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class HudMixin extends DrawableHelper implements HudAccess {
    @Unique
    private static final Identifier DNDREAMS_GUI_ICONS = new Identifier(Initializer.MODID, "textures/gui/icons.png");

    private static final Identifier DNDREAMS_GUI_MANA_BAR = new Identifier(Initializer.MODID, "textures/gui/mana_bar.png");
    private static final Identifier DNDREAMS_GUI_HEARTS = new Identifier(Initializer.MODID, "textures/gui/hearts.png");
    private static final Identifier DNDREAMS_GUI_SANITY_METER = new Identifier(Initializer.MODID, "textures/gui/sanity_meter.png");

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

    @Unique private static final int TORMENT_X_OFFSET = 6;

    @Unique private static final int TORMENT_Y_OFFSET = 7;

    @Unique private static final int TORMENT_Y_BIG_OFFSET = 30;

    @Unique private static final int TORMENT_WIDTH = 30;

    @Unique private static final int TORMENT_HEIGHT = 30;

    @Unique private static final int TORMENT_INNER_WIDTH = 20;
    @Unique private static final int TORMENT_INNER_HEIGHT = 20;

    @Unique private static final int REVIVE_X_OFFSET = TORMENT_X_OFFSET + TORMENT_WIDTH + 8;

    @Unique private static final int REVIVE_Y_OFFSET = TORMENT_Y_OFFSET + 6;

    @Unique private static final int REVIVE_Y_BIG_OFFSET = TORMENT_Y_BIG_OFFSET + 6;

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


    @Shadow @Final private Random random;

    @Shadow protected abstract void drawHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart);

    @Shadow private ItemStack currentStack;

    @Override
    public void setDragonFlash(int ticks) {
        dragonFlashTicks = ticks;
        System.out.println("Set Dragon Flash Ticks");
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
            }
        });



        EntityComponents.REVIVE.maybeGet(player).ifPresent(revive -> {
            if (revive.shouldDisplay()) {
                int amount = revive.remainingRevives();
                String string = amount >= 0 ? "x" + amount : "x0";

                int k = client.options.getMainArm().getValue() == Arm.RIGHT ? REVIVE_X_OFFSET + REVIVE_WIDTH + 5 : scaledWidth - REVIVE_X_OFFSET - REVIVE_WIDTH - 5 - 10;
                int l = scaledHeight - (revive.shouldOffsetRender() ? REVIVE_Y_BIG_OFFSET : REVIVE_Y_OFFSET) - 12;

                this.getTextRenderer().draw(matrices, string, (float)(k + 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)(k - 1), (float)l, 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l + 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)(l - 1), 0);
                this.getTextRenderer().draw(matrices, string, (float)k, (float)l, Color.RED.getRGB());
            }
        });
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", shift = At.Shift.AFTER, ordinal = 2, target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;"))
    private void dndreams$renderStatusBars(MatrixStack matrices, CallbackInfo callbackInfo) {
        int xPos = client.options.getMainArm().getValue() == Arm.LEFT ? scaledWidth - MANA_X_OFFSET - MANA_WIDTH : MANA_X_OFFSET;
        int yPos = scaledHeight - MANA_Y_OFFSET - MANA_HEIGHT;



        PlayerEntity player = getCameraPlayer();




        EntityComponents.MANA.maybeGet(player).ifPresent(manaComponent -> {
            if (!manaComponent.shouldRender()) return;

            int maxMana = manaComponent.getManaMax();
            float mana = manaComponent.getMana() + manaComponent.getPartialMana(maxMana);
            int vPos = manaComponent.getManaFrame() * 6;

            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_MANA_BAR);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);
            drawTexture(matrices, xPos, yPos, MANA_WIDTH - 2, MANA_HEIGHT, MANA_WIDTH, MANA_HEIGHT, 230, 1944);
            drawTexture(matrices, xPos + 1, yPos + 1, 0, vPos, (int)((MANA_WIDTH - 2) * Math.min(mana / maxMana, 1f)), MANA_HEIGHT - 2, 230, 1944);
            if (player.hasStatusEffect(ModStatusEffects.SUPPRESSED)) {
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

        int tormentV = 30;
        int tormentV2 = 50;

        EntityComponents.TORMENT.maybeGet(player).ifPresent(component -> {
            int tormentYPos = scaledHeight - TORMENT_HEIGHT - (component.shouldOffsetRender() ? TORMENT_Y_BIG_OFFSET : TORMENT_Y_OFFSET);
            int tormentInnerY = tormentYPos + 5;
            int tormentInnerX = tormentXPos + 5;

            float tormentMaxPercent = (component.getMaxSanity() / TormentComponent.MAX_SANITY);
            float tormentPercent = (component.getSanity() / TormentComponent.MAX_SANITY);
            int skipV2 = MathHelper.ceil((TORMENT_INNER_HEIGHT) * (1f - tormentMaxPercent));
            int skipV = MathHelper.ceil((TORMENT_INNER_HEIGHT) * (1f - tormentPercent));

            int mainU = component.isInStorm() ? 90 : player.hasStatusEffect(ModStatusEffects.BRAINFREEZE) ? 30 : 0;

            int innerU = 5;
            float sanity = component.getAttunedSanity();

            int treeU = sanity >= 85 ? 0 : sanity >= 65 ? 20 : sanity >= 45 ? 40 : sanity >= 25 ? 60 : sanity >= 5 ? 80 : 100;
            int treeV = EntityComponents.DREAMING.get(player).isDreaming() ? 90 : 70;


            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_SANITY_METER);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);

            if (component.isTruthActive()) {

                drawTexture(matrices, tormentXPos, tormentYPos, 60, 0, TORMENT_WIDTH, TORMENT_HEIGHT);
            } else {

                drawTexture(matrices, tormentXPos, tormentYPos, mainU, 0, TORMENT_WIDTH, TORMENT_HEIGHT);
                drawTexture(matrices, tormentInnerX, tormentInnerY + skipV2, innerU, tormentV2 + skipV2, TORMENT_INNER_WIDTH, TORMENT_INNER_HEIGHT - skipV2);
                drawTexture(matrices, tormentInnerX, tormentInnerY + skipV, innerU, tormentV + skipV, TORMENT_INNER_WIDTH, TORMENT_INNER_HEIGHT - skipV);

                drawTexture(matrices, tormentInnerX, tormentInnerY, treeU, treeV, TORMENT_INNER_WIDTH, TORMENT_INNER_HEIGHT);
            }



            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        });

        EntityComponents.REVIVE.maybeGet(player).ifPresent(revive -> {
            if (!revive.shouldDisplay()) return;

            int reviveXPos = client.options.getMainArm().getValue() == Arm.RIGHT ? REVIVE_X_OFFSET : (scaledWidth - REVIVE_X_OFFSET - REVIVE_WIDTH);
            int reviveYPos = scaledHeight - REVIVE_HEIGHT - (revive.shouldOffsetRender() ? REVIVE_Y_BIG_OFFSET : REVIVE_Y_OFFSET);

            float vitalityPercent = revive.getDisplayedVitality() / 100;

            int skipV = MathHelper.ceil(REVIVE_HEIGHT * (1f - vitalityPercent));
            int u = revive.canRecharge() ? 117 : 134;

            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_ICONS);
            RenderSystem.setShaderColor(1, 1, 1, 1.0f);

            drawTexture(matrices, reviveXPos, reviveYPos, u, 16, REVIVE_WIDTH, REVIVE_HEIGHT);
            drawTexture(matrices, reviveXPos, reviveYPos + skipV, u, skipV, REVIVE_WIDTH, (int)(REVIVE_HEIGHT * vitalityPercent));

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        });

        if (dragonFlashTicks > 0) {
            RenderSystem.setShaderTexture(0, DRAGON_FLASH_IMAGE);

            int n = scaledWidth / 2 - 323;
            int m = scaledHeight / 2 - 207;

            float alpha = 1;

            if (dragonFlashTicks <= 20) {
                alpha = (dragonFlashTicks / 20.0f);
            }

            RenderSystem.setShaderColor(1, 1, 1, alpha);

            drawTexture(matrices, n, m, 646, 414,0, 0, 646, 414, 646, 414);

            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void dndreams$drawHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {

        CustomHeartType customType = HudAccess.CustomHeartType.fromPlayerState(getCameraPlayer(), type, this.random);
        if (customType != CustomHeartType.NO_CHANGE) {
            RenderSystem.setShaderTexture(0, DNDREAMS_GUI_HEARTS);

            boolean hardcore = v > 0;

            drawTexture(matrices, x, y, customType.getU(), customType.getV(halfHeart, blinking, hardcore), 9, 9);


            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
            ci.cancel();
        }
    }

    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;ceil(F)I", ordinal = 2))
    private float dndreams$renderStatusBars$fixHeartLines(float value) {

        if (client.player != null && EntityComponents.ROT.isProvidedBy(client.player)) {
            RotComponent rot = EntityComponents.ROT.get(client.player);

            if (rot.getRot() > 0) {
                return value + (.05f * rot.getRot());
            }
        }

        return value;
    }

    @Inject(method = "renderHealthBar", at = @At("TAIL"))
    private void dndreams$renderHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {

        if (EntityComponents.ROT.isProvidedBy(player)) {
            RotComponent rot = EntityComponents.ROT.get(player);

            if (rot.getRot() > 0) {
                maxHealth = rot.getTrueHp();

                boolean hardcore = player.world.getLevelProperties().isHardcore();
                int i = 9 * (hardcore ? 5 : 0);
                int hearts = MathHelper.ceil((double)maxHealth / 2.0);
                int gloomHearts = rot.getRot() / 2;

                for (int m = hearts + gloomHearts - 1; m >= hearts; m--) {

                    int n = m / 10;
                    int o = m % 10;
                    int p = x + o * 8;
                    int q = y - n * lines;
                    if (lastHealth + absorption <= 4) {
                        q += this.random.nextInt(2);
                    }

                    this.drawHeart(matrices, InGameHud.HeartType.CONTAINER, p, q, i, blinking, false);
                    if (!player.hasStatusEffect(ModStatusEffects.AGONY)) this.drawGloomHeart(matrices, CustomHeartType.ROT, p, q, hardcore, blinking && m * 2 < health, false);
                }

                if (rot.getRot() % 2 == 1) {

                    int m = hearts - 1;
                    int n = m / 10;
                    int o = m % 10;
                    int p = x + o * 8;
                    int q = y - n * lines;
                    if (lastHealth + absorption <= 4) {
                        q += this.random.nextInt(2);
                    }
                    if (m == regeneratingHeartIndex) {
                        q -= 2;
                    }

                    if (!player.hasStatusEffect(ModStatusEffects.AGONY)) this.drawGloomHeart(matrices, CustomHeartType.ROT, p, q, hardcore, blinking && m * 2 < health, true);
                }
            }
        }
    }

    @Unique private void drawGloomHeart(MatrixStack matrices, CustomHeartType type, int x, int y, boolean hardcore, boolean blinking, boolean halfHeart) {

        RenderSystem.setShaderTexture(0, DNDREAMS_GUI_HEARTS);

        drawTexture(matrices, x, y, type.getU(), type.getV(halfHeart, blinking, hardcore), 9, 9);

        RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
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

    @Inject(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$renderHeldItemTooltip(MatrixStack matrices, CallbackInfo ci, MutableText mutableText, int i, int j, int k, int l) {

        if (this.currentStack.isIn(ModTags.ATLAS)) {

            //MutableText text = mutableText.formatted(Formatting.WHITE);

            String string = mutableText.getString();

            this.getTextRenderer().draw(matrices, string, (float)(j + 1), (float)k      , 0xF1B56D + (l << 24));
            this.getTextRenderer().draw(matrices, string, (float)(j - 1), (float)k      , 0xE7A360 + (l << 24));
            this.getTextRenderer().draw(matrices, string, (float)j      , (float)(k + 1), 0xE7A360 + (l << 24));
            this.getTextRenderer().draw(matrices, string, (float)j      , (float)(k - 1), 0xFFCE7F + (l << 24));
            this.getTextRenderer().draw(matrices, string, (float)j      , (float)k      , 0x09608E + (l << 24));

            RenderSystem.disableBlend();
            this.client.getProfiler().pop();

            ci.cancel();
        }
    }

    @ModifyConstant(method = "renderHeldItemTooltip", constant = @Constant(intValue = 59))
    private int dndreams$renderHeldItemTooltip$fixPosition(int constant) {

        if (this.client.interactionManager.hasStatusBars() && this.getCameraPlayer().getMaxHealth() > 20) {

            return (int) (constant + ((this.getCameraPlayer().getMaxHealth() - 1) / 20f) * 9);
        }

        return constant;
    }

    @Unique
    private void renderInsanityVignette() {
        PlayerEntity player = client.player;
        if (player == null) return;

        TormentComponent torment = EntityComponents.TORMENT.get(player);
        float g = torment.getNightmareHaze();

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
