package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.entities.models.FacelessEntityModel;
import net.eman3600.dndreams.entities.renderers.features.FacelessEyesRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static net.eman3600.dndreams.Initializer.MODID;

@Environment(EnvType.CLIENT)
public class FacelessEntityRenderer extends GeoEntityRenderer<FacelessEntity> {

    public static Identifier TEXTURE = new Identifier(MODID, "textures/entity/faceless.png");
    private final HeldItemRenderer heldItemRenderer;


    public FacelessEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FacelessEntityModel());
        this.shadowOpacity = 0f;

        this.heldItemRenderer = renderManager.getHeldItemRenderer();
        FacelessEyesRenderer eyesRenderer = new FacelessEyesRenderer(this);

        this.addLayer(eyesRenderer);

    }

    @Override
    public Identifier getTextureResource(FacelessEntity animatable) {
        return TEXTURE;
    }

    @Override
    public RenderLayer getRenderType(FacelessEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public Color getRenderColor(FacelessEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
        float clarity = animatable.renderedClarity(MinecraftClient.getInstance().player);
        float opacity = animatable.renderedOpacity(MinecraftClient.getInstance().player);

        return animatable.hurtTime > 0 ? Color.ofRGBA(clarity, clarity * .2f, clarity * .2f, opacity) : Color.ofRGBA(clarity, clarity, clarity, opacity);
    }

    @Override
    public void render(GeoModel model, FacelessEntity animatable, float partialTick, RenderLayer type, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        //renderHands(poseStack, bufferSource, packedLight, animatable, model);
    }

    /*public void renderHands(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, FacelessEntity livingEntity, GeoModel model) {
        ItemStack itemStack2;
        boolean bl = ((LivingEntity)livingEntity).getMainArm() == Arm.RIGHT;
        ItemStack itemStack = bl ? livingEntity.getOffHandStack() : livingEntity.getMainHandStack();
        itemStack2 = bl ? livingEntity.getMainHandStack() : livingEntity.getOffHandStack();
        if (itemStack.isEmpty() && itemStack2.isEmpty()) {
            return;
        }
        matrixStack.push();
        this.renderItem(livingEntity, model, itemStack2, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrixStack, vertexConsumerProvider, i);
        this.renderItem(livingEntity, model, itemStack, ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrixStack, vertexConsumerProvider, i);
        matrixStack.pop();
    }

    protected void renderItem(LivingEntity entity, GeoModel model, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (stack.isEmpty()) {
            return;
        }
        matrices.push();

        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-180.0f));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        matrices.translate(0, -1f, .2);
        boolean bl = arm == Arm.LEFT;
        matrices.translate((float)(bl ? -1 : 1) / 16.0f, 0.125, -0.625);
        this.heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
        matrices.pop();
    }*/
}
