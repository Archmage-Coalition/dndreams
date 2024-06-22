package net.eman3600.dndreams.entities.renderers.features;

import net.eman3600.dndreams.entities.mobs.ShamblerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

@Environment(value= EnvType.CLIENT)
public class ShamblerOverlayFeatureRenderer<T extends ShamblerEntity>
        extends FeatureRenderer<T, DrownedEntityModel<T>> {
    private static final Identifier SKIN = new Identifier(MODID, "textures/entity/shambler_outer_layer.png");
    private final DrownedEntityModel<T> model;

    public ShamblerOverlayFeatureRenderer(FeatureRendererContext<T, DrownedEntityModel<T>> context, EntityModelLoader loader) {
        super(context);
        this.model = new DrownedEntityModel(loader.getModelPart(EntityModelLayers.DROWNED_OUTER));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T drownedEntity, float f, float g, float h, float j, float k, float l) {
        render(this.getContextModel(), this.model, SKIN, matrixStack, vertexConsumerProvider, i, drownedEntity, f, g, j, k, l, h, 1.0f, 1.0f, 1.0f);
    }
}

