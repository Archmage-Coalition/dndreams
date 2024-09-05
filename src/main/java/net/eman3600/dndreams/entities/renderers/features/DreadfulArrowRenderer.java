package net.eman3600.dndreams.entities.renderers.features;

import net.eman3600.dndreams.entities.projectiles.DreadfulArrowEntity;
import net.eman3600.dndreams.entities.projectiles.ManagoldArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class DreadfulArrowRenderer extends ProjectileEntityRenderer<DreadfulArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/dreadful_arrow.png");

    public DreadfulArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(DreadfulArrowEntity entity) {
        return TEXTURE;
    }
}
