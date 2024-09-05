package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.projectiles.StormArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class StormArrowRenderer extends ProjectileEntityRenderer<StormArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/projectile/storm_arrow.png");

    public StormArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(StormArrowEntity entity) {
        return TEXTURE;
    }
}
