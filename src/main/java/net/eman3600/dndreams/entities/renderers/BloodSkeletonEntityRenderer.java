package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.renderers.features.BloodSkeletonOverlayFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class BloodSkeletonEntityRenderer extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/blood_moon/blood_skeleton.png");

    public BloodSkeletonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, EntityModelLayers.STRAY, EntityModelLayers.STRAY_INNER_ARMOR, EntityModelLayers.STRAY_OUTER_ARMOR);
        this.addFeature(new BloodSkeletonOverlayFeatureRenderer<>(this, context.getModelLoader()));
    }

    @Override
    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }
}
