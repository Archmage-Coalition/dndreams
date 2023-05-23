package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.entities.models.TormentorEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static net.eman3600.dndreams.Initializer.MODID;

@Environment(EnvType.CLIENT)
public class TormentorEntityRenderer extends GeoEntityRenderer<TormentorEntity> {

    public static Identifier TEXTURE = new Identifier(MODID, "textures/entity/tormentor/tormentor.png");


    public TormentorEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new TormentorEntityModel());
        this.shadowOpacity = 0;
    }

    @Override
    public Identifier getTextureResource(TormentorEntity animatable) {
        return TEXTURE;
    }

    @Override
    public RenderLayer getRenderType(TormentorEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, Identifier texture) {
        return RenderLayer.getEntityTranslucentCull(texture);
    }

    @Override
    public Color getRenderColor(TormentorEntity animatable, float partialTick, MatrixStack poseStack, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
        float clarity = animatable.renderedClarity(MinecraftClient.getInstance().player);
        float opacity = animatable.renderedOpacity(MinecraftClient.getInstance().player);

        return Color.ofRGBA(clarity, clarity, clarity, opacity);
    }
}
