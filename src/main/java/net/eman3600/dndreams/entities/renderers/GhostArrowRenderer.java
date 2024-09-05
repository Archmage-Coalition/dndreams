package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.projectiles.GhostArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class GhostArrowRenderer extends ProjectileEntityRenderer<GhostArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/ghost_arrow.png");

    public GhostArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(GhostArrowEntity entity) {
        return TEXTURE;
    }
}
