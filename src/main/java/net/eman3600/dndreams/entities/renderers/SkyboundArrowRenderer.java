package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.projectiles.SkyboundArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class SkyboundArrowRenderer extends ProjectileEntityRenderer<SkyboundArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/skybound_arrow.png");

    public SkyboundArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(SkyboundArrowEntity entity) {
        return TEXTURE;
    }
}
