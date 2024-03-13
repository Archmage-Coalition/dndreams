package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.projectiles.ManagoldArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class ManagoldArrowRenderer extends ProjectileEntityRenderer<ManagoldArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/managold_arrow.png");

    public ManagoldArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ManagoldArrowEntity entity) {
        return TEXTURE;
    }
}
