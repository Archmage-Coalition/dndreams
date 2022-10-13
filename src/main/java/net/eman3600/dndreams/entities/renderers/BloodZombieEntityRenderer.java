package net.eman3600.dndreams.entities.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class BloodZombieEntityRenderer extends ZombieEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/blood_moon/blood_zombie.png");

    public BloodZombieEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }


    @Override
    protected void scale(ZombieEntity zombieEntity, MatrixStack matrixStack, float f) {
        float g = 1.0625f;
        matrixStack.scale(g, g, g);
        super.scale(zombieEntity, matrixStack, f);
    }

    @Override
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }
}
