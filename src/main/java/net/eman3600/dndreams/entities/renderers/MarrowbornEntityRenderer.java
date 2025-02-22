package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.renderers.features.MarrowbornOverlayFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class MarrowbornEntityRenderer extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/marrowborn.png");

    public MarrowbornEntityRenderer(EntityRendererFactory.Context context) {
        super(context, EntityModelLayers.STRAY, EntityModelLayers.STRAY_INNER_ARMOR, EntityModelLayers.STRAY_OUTER_ARMOR);
        this.addFeature(new MarrowbornOverlayFeatureRenderer<>(this, context.getModelLoader()));
    }

    @Override
    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return TEXTURE;
    }
}
