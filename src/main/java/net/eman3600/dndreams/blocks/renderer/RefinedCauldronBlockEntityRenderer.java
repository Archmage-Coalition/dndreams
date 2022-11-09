package net.eman3600.dndreams.blocks.renderer;

import net.eman3600.dndreams.blocks.entities.RefinedCauldronBlockEntity;
import net.eman3600.dndreams.blocks.properties.CauldronState;
import net.eman3600.dndreams.initializers.ModParticles;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class RefinedCauldronBlockEntityRenderer implements BlockEntityRenderer<RefinedCauldronBlockEntity> {
    public static final float[] HEIGHT = {0, 0.25f, 0.4375f, 0.63f};
    public static final SpriteIdentifier WATER = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/water_still"));

    @Override
    public void render(RefinedCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();

        if (world != null) {
            BlockPos pos = entity.getPos();
            int level = entity.getLevel();

            if (level > 0) {
                matrices.push();
                matrices.translate(0, HEIGHT[level], 0);
                renderWater(entity.getColor(), matrices, vertexConsumers, light, overlay, WATER.getSprite());
                matrices.pop();
            }
        }
    }

    private void renderWater(int color, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Sprite sprite) {
        float sizeFactor = 0.125F;
        float maxV = (sprite.getMaxV() - sprite.getMinV()) * sizeFactor;
        float minV = (sprite.getMaxV() - sprite.getMinV()) * (1 - sizeFactor);
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        matrices.push();
        Matrix4f mat = matrices.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
        vertexConsumer.vertex(mat, sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).texture(sprite.getMinU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
        vertexConsumer.vertex(mat, 1 - sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).texture(sprite.getMaxU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
        vertexConsumer.vertex(mat, 1 - sizeFactor, 0, sizeFactor).color(red, green, blue, 255).texture(sprite.getMaxU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
        vertexConsumer.vertex(mat, sizeFactor, 0, sizeFactor).color(red, green, blue, 255).texture(sprite.getMinU(), sprite.getMinV() + minV).light(light).overlay(overlay).normal(1, 1, 1).next();
        matrices.pop();
    }
}
