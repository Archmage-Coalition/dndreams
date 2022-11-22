package net.eman3600.dndreams.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class RefineryScreen extends HandledScreen<RefineryScreenHandler> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/gui/container/refinery.png");


    public RefineryScreen(RefineryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        if (handler.delegate.get(3) != 0) {
            float progress = (float)handler.delegate.get(2) / handler.delegate.get(3);

            this.drawTexture(matrices, x + 79, y + 34, 176, 14, (int)(24 * progress), 16);

            if (handler.delegate.get(1) > 1) {
                progress = (float)handler.delegate.get(0) / handler.delegate.get(1);
                int k = (int)(13 * progress);

                this.drawTexture(matrices, x + 10, y + 47 - k, 176, 13 - k, 14, k + 1);
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
