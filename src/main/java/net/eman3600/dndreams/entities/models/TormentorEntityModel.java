package net.eman3600.dndreams.entities.models;

import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.entities.renderers.TormentorEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static net.eman3600.dndreams.Initializer.MODID;

@Environment(EnvType.CLIENT)
public class TormentorEntityModel extends AnimatedGeoModel<TormentorEntity> {
    @Override
    public Identifier getModelResource(TormentorEntity object) {
        return new Identifier(MODID, "geo/tormentor.geo.json");
    }

    @Override
    public Identifier getTextureResource(TormentorEntity object) {
        return TormentorEntityRenderer.TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(TormentorEntity animatable) {
        return new Identifier(MODID, "animations/tormentor.animation.json");
    }
}
