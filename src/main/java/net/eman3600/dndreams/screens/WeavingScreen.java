package net.eman3600.dndreams.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.recipes.WeavingRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class WeavingScreen extends HandledScreen<WeavingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Initializer.MODID, "textures/gui/container/weaving.png");
    private static final int TORMENT_INNER_WIDTH = 20;
    private static final int TORMENT_INNER_HEIGHT = 20;
    private final PlayerEntity player;
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;
    private final WeavingScreenHandler handler;
    private List<Ingredient> missingIngredients = new ArrayList<>();
    private float time = 0;

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
        time += delta;
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

                this.drawTexture(matrices, i + 139, j + 45, 24, 220, 24, 24);

                WeavingRecipe recipe = handler.getCurrentRecipe();
                int bitmask = handler.getInputSlotStates(recipe);

                int missingItems = (bitmask >> 4 & 1) + (bitmask >> 5 & 1);
                int filledSlots = (bitmask & 1) + (bitmask >> 1 & 1);

                int slotIndex = (bitmask & 1) == 1 ? 1 : 0;
                int ingredientIndex = (bitmask >> 4 & 1) == 1 ? 0 : 1;

                while (missingItems > 0 && filledSlots < 2) {

                    drawMissingIngredient(matrices, i, j, slotIndex++, recipe.input.get(ingredientIndex++));

                    missingItems--;
                    filledSlots++;
                }

                if ((bitmask & 1) == 1 && (bitmask >> 2 & 1) == 0) {
                    drawWrongIngredientOverlay(matrices, i, j, 0);
                }
                if ((bitmask >> 1 & 1) == 1 && (bitmask >> 3 & 1) == 0) {
                    drawWrongIngredientOverlay(matrices, i, j, 1);
                }
            }
        }
    }

    private void drawMissingIngredient(MatrixStack matrices, int i, int j, int slot, Ingredient ingredient) {
        this.drawTexture(matrices, i + 20, j + 33 + 18 * slot, 48, 220, 16, 16);

        ItemStack[] stacks = ingredient.getMatchingStacks();
        ItemStack stack = stacks[(int)time % stacks.length];

        itemRenderer.renderGuiItemIcon(stack, i + 20, j + 33 + 18 * slot);

        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    private void drawWrongIngredientOverlay(MatrixStack matrices, int i, int j, int slot) {
        this.drawTexture(matrices, i + 20, j + 33 + 18 * slot, 64, 220, 16, 16);
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

            float tormentMaxPercent = (maxSanity / TormentComponent.MAX_SANITY);
            float tormentPercent = (sanity / TormentComponent.MAX_SANITY);
            float costPercent = (sanityCost / TormentComponent.MAX_SANITY);
            int skipV2 = MathHelper.ceil((TORMENT_INNER_HEIGHT) * (1f - tormentMaxPercent));
            int skipV = MathHelper.ceil((TORMENT_INNER_HEIGHT) * (1f - tormentPercent));

            int skipV3 = MathHelper.ceil((TORMENT_INNER_HEIGHT) * costPercent);

            int startX = 136 + i;
            int startY = 10 + j;

            this.drawTexture(matrices, startX, startY, 16, 166, 30, 30);

            boolean canAfford = torment.canFullyAfford(sanityCost);

            int costU = canAfford ? 86 : 106;
            int costV = canAfford ? 166 + skipV : 186 - skipV3;
            int costY = canAfford ? startY + 5 + skipV : startY + 25 - skipV3;

            this.drawTexture(matrices, startX + 5, startY + 5 + skipV2, 66, 166 + skipV2, TORMENT_INNER_WIDTH, TORMENT_INNER_HEIGHT - skipV2);
            this.drawTexture(matrices, startX + 5, startY + 5 + skipV, 46, 166 + skipV, TORMENT_INNER_WIDTH, TORMENT_INNER_HEIGHT - skipV);
            this.drawTexture(matrices, startX + 5, costY, costU, costV, TORMENT_INNER_WIDTH, skipV3);
        });
        int k = (int)(41.0f * this.scrollAmount);
        this.drawTexture(matrices, i + 119, j + 15 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
        int l = this.x + 52;
        int m = this.y + 14;
        int n = this.scrollOffset + 12;
        this.renderRecipeBackground(matrices, mouseX, mouseY, l, m, n);
        this.renderRecipeIcons(l, m, n);

        this.drawMissingItems(matrices, delta, mouseX, mouseY);
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