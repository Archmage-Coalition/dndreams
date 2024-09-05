package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.projectiles.MindstrungArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class MindstrungArrowRenderer extends ProjectileEntityRenderer<MindstrungArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/mindstrung_arrow.png");

    public MindstrungArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(MindstrungArrowEntity entity) {
        return TEXTURE;
    }
}
