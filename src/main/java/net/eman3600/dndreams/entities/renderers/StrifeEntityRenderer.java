package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.projectiles.SparkBoltEntity;
import net.eman3600.dndreams.entities.projectiles.StrifeEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

import static net.eman3600.dndreams.Initializer.MODID;

public class StrifeEntityRenderer extends EntityRenderer<StrifeEntity> {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/strife.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);

    private static final float SCALE = 0.75f;

    public StrifeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(StrifeEntity entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(StrifeEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(SCALE, SCALE, SCALE);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    @Override
    public Identifier getTexture(StrifeEntity entity) {
        return TEXTURE;
    }
}
