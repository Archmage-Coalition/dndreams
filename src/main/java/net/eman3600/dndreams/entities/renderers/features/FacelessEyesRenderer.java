package net.eman3600.dndreams.entities.renderers.features;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static net.eman3600.dndreams.Initializer.MODID;

public class FacelessEyesRenderer extends GeoLayerRenderer<FacelessEntity> {

    public static Identifier TEXTURE = new Identifier(MODID, "textures/entity/faceless_eyes.png");


    public FacelessEyesRenderer(IGeoRenderer<FacelessEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack PoseStackIn, VertexConsumerProvider bufferIn, int packedLightIn, FacelessEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (entitylivingbaseIn.getDataTracker().get(FacelessEntity.HAS_EYES)) {
            renderCopyModel(this.getEntityModel(), TEXTURE, PoseStackIn, bufferIn, 0xF00000, entitylivingbaseIn, partialTicks,1f, 1f, 1f);
        }
    }
}
