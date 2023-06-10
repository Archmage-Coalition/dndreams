package net.eman3600.dndreams.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.recipes.WeavingRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeavingScreen extends HandledScreen<WeavingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Initializer.MODID, "textures/gui/container/weaving.png");
    private final PlayerEntity player;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;
    private final WeavingScreenHandler handler;
    private Map<Integer, Ingredient> missingIngredients = new HashMap<>();

    public WeavingScreen(WeavingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onInventoryChange);
        this.player = inventory.player;
        this.handler = handler;
    }

    protected void init() {
        super.init();
        this.titleY -= 2;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (handler.clientDirty) {
            handler.clientDirty = false;

            missingIngredients = handler.getMissingIngredients();
        }

        this.renderBackground(matrices);
        this.drawMissingItems(matrices, delta, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);

        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private void drawMissingItems(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;

        if (handler.showsOutput()) {

            if (!handler.trulyMatches()) {

                this.drawTexture(matrices, i + 139, j + 29, 24, 220, 24, 24);
            }
        }
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

        EntityComponents.TORMENT.maybeGet(player).ifPresent(torment -> {
            float sanity = torment.getSanity();
            float maxSanity = torment.getMaxSanity();
            float sanityCost = handler.getSanityCost();
            int sanityPixels = (int)(sanity * .12f);
            int sanityPixelsCeil = MathHelper.ceil(sanity * .12f);
            int maxSanityPixels = (int)(maxSanity * .12f);
            int costPixels = MathHelper.ceil(sanityCost * .12f);

            int startX = 38 + i;
            int startY = 35 + j;

            int costV = torment.canAfford(sanityCost) ? 220 - sanityPixelsCeil : 232 - costPixels;
            int costY = torment.canAfford(sanityCost) ? 12 - sanityPixelsCeil : 12 - costPixels;

            this.drawTexture(matrices, startX, startY + 12 - maxSanityPixels, 0, 232 + 12 - maxSanityPixels, 12, maxSanityPixels);
            this.drawTexture(matrices, startX, startY + 12 - sanityPixels, 0, 220 + 12 - sanityPixels, 12, sanityPixels);
            this.drawTexture(matrices, startX, startY + costY, 12, costV + 12, 12, costPixels);
        });
        int k = (int)(41.0f * this.scrollAmount);
        this.drawTexture(matrices, i + 119, j + 15 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
        int l = this.x + 52;
        int m = this.y + 14;
        int n = this.scrollOffset + 12;
        this.renderRecipeBackground(matrices, mouseX, mouseY, l, m, n);
        this.renderRecipeIcons(l, m, n);
    }

    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int i = this.x + 52;
            int j = this.y + 14;
            int k = this.scrollOffset + 12;
            for (int l = this.scrollOffset; l < k; ++l) {
                int m = l - this.scrollOffset;
                double d = mouseX - (double)(i + m % 4 * 16);
                double e = mouseY - (double)(j + m / 4 * 18);
                if (!(d >= 0.0) || !(e >= 0.0) || !(d < 16.0) || !(e < 18.0) || !(this.handler).onButtonClick(this.client.player, l)) continue;
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                this.client.interactionManager.clickButton(this.handler.syncId, l);
                return true;
            }
            i = this.x + 119;
            j = this.y + 9;
            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 14;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5f) / ((float)(j - i) - 15.0f);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0f, 1.0f);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 4;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)amount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 4;
        }
        return true;
    }

    private boolean shouldScroll() {
        return this.canCraft && (this.handler).getAvailableRecipeCount() > 12;
    }

    protected int getMaxScroll() {
        return ((this.handler).getAvailableRecipeCount() + 4 - 1) / 4 - 3;
    }

    private void onInventoryChange() {
        this.canCraft = (this.handler).canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0f;
            this.scrollOffset = 0;
        }
        missingIngredients = handler.getMissingIngredients();
    }

    private void renderRecipeBackground(MatrixStack matrices, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < (this.handler).getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;
            int n = this.backgroundHeight;
            if (i == (this.handler).getSelectedRecipe()) {
                n += 18;
            } else if (mouseX >= k && mouseY >= m && mouseX < k + 16 && mouseY < m + 18) {
                n += 36;
            }
            this.drawTexture(matrices, k, m - 1, 0, n, 16, 18);
        }
    }

    private void renderRecipeIcons(int x, int y, int scrollOffset) {
        List<WeavingRecipe> list = (this.handler).getAvailableRecipes();
        for (int i = this.scrollOffset; i < scrollOffset && i < (this.handler).getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;
            this.client.getItemRenderer().renderInGuiWithOverrides(list.get(i).getOutput(), k, m);
        }
    }


}