package net.eman3600.dndreams.entities.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class ManagoldArrowRenderer extends ProjectileEntityRenderer {
    public static final Identifier TEXTURE = new Identifier("dndreams", "textures/entity/projectile/managold_arrow.png");

    public ManagoldArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURE;
    }
}
