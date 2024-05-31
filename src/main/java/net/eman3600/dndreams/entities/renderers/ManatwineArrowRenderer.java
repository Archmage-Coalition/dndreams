package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.projectiles.ManatwineArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class ManatwineArrowRenderer extends ProjectileEntityRenderer<ManatwineArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/manatwine_arrow.png");

    public ManatwineArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ManatwineArrowEntity entity) {
        return TEXTURE;
    }
}
