package net.eman3600.dndreams.entities.models;

import net.eman3600.dndreams.entities.mobs.WardenRagdollEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static net.eman3600.dndreams.Initializer.MODID;

public class WardenRagdollEntityModel extends AnimatedGeoModel<WardenRagdollEntity> {



    @Override
    public Identifier getModelResource(WardenRagdollEntity object) {
        return new Identifier(MODID, "geo/warden_ragdoll.geo.json");
    }

    @Override
    public Identifier getTextureResource(WardenRagdollEntity object) {
        return new Identifier(MODID, "textures/entity/warden_ragdoll/warden.png");
    }

    @Override
    public Identifier getAnimationResource(WardenRagdollEntity animatable) {
        return new Identifier(MODID, "animations/warden_ragdoll.animation.json");
    }


    @Override
    public void setLivingAnimations(WardenRagdollEntity entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }
}
