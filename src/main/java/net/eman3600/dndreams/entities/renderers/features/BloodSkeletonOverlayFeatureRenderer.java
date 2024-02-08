package net.eman3600.dndreams.entities.renderers.features;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.StrayOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class BloodSkeletonOverlayFeatureRenderer<T extends MobEntity & RangedAttackMob, M extends EntityModel<T>> extends StrayOverlayFeatureRenderer<T, M> {
    private static final Identifier SKIN = new Identifier(MODID, "textures/entity/blood_moon/blood_skeleton_overlay.png");
    private final SkeletonEntityModel<T> model;


    public BloodSkeletonOverlayFeatureRenderer(FeatureRendererContext context, EntityModelLoader loader) {
        super(context, loader);
        this.model = new SkeletonEntityModel(loader.getModelPart(EntityModelLayers.STRAY_OUTER));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T mobEntity, float f, float g, float h, float j, float k, float l) {
        StrayOverlayFeatureRenderer.render(this.getContextModel(), this.model, SKIN, matrixStack, vertexConsumerProvider, i, mobEntity, f, g, j, k, l, h, 1.0f, 1.0f, 1.0f);
    }
}
