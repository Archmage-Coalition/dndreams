package net.eman3600.dndreams.entities.models;

import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.entities.renderers.FacelessEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static net.eman3600.dndreams.Initializer.MODID;

@Environment(EnvType.CLIENT)
public class FacelessEntityModel extends AnimatedGeoModel<FacelessEntity> {
    @Override
    public Identifier getModelResource(FacelessEntity object) {
        return new Identifier(MODID, "geo/faceless.geo.json");
    }

    @Override
    public Identifier getTextureResource(FacelessEntity object) {
        return FacelessEntityRenderer.TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(FacelessEntity animatable) {
        return new Identifier(MODID, "animations/faceless.animation.json");
    }
}
