package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.mobs.ShamblerEntity;
import net.eman3600.dndreams.entities.renderers.features.ShamblerOverlayFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class ShamblerEntityRenderer extends ZombieBaseEntityRenderer<ShamblerEntity, DrownedEntityModel<ShamblerEntity>> {
    private static final Identifier TEXTURE = new Identifier(MODID, "textures/entity/shambler.png");

    public ShamblerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DrownedEntityModel<>(context.getPart(EntityModelLayers.DROWNED)), new DrownedEntityModel<>(context.getPart(EntityModelLayers.DROWNED_INNER_ARMOR)), new DrownedEntityModel<>(context.getPart(EntityModelLayers.DROWNED_OUTER_ARMOR)));
        this.addFeature(new ShamblerOverlayFeatureRenderer<>(this, context.getModelLoader()));
    }


    @Override
    protected void scale(ShamblerEntity shamblerEntity, MatrixStack matrixStack, float f) {
        float g = 1.0625f;
        matrixStack.scale(g, g, g);
        super.scale(shamblerEntity, matrixStack, f);
    }

    @Override
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }
}
