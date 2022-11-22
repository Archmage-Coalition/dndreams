package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.mobs.WardenRagdollEntity;
import net.eman3600.dndreams.entities.models.WardenRagdollEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WardenRagdollEntityRenderer extends GeoEntityRenderer<WardenRagdollEntity> {
    public WardenRagdollEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WardenRagdollEntityModel());
    }

    @Override
    public Identifier getTexture(WardenRagdollEntity entity) {
        return modelProvider.getTextureResource(entity);
    }
}
