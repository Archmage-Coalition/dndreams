package net.eman3600.dndreams.entities.renderers;

import net.eman3600.dndreams.entities.mobs.DreamSheepEntity;
import net.eman3600.dndreams.entities.models.DreamSheepEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class DreamSheepEntityRenderer
        extends MobEntityRenderer<DreamSheepEntity, DreamSheepEntityModel<DreamSheepEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/sheep/sheep.png");

    public DreamSheepEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DreamSheepEntityModel(context.getPart(EntityModelLayers.SHEEP)), 0.7f);
        this.shadowOpacity = 0f;
        this.addFeature(new DreamSheepWoolFeatureRenderer(this, context.getModelLoader()));
    }

    @Override
    public Identifier getTexture(DreamSheepEntity sheepEntity) {
        return TEXTURE;
    }

    @Override
    public void render(DreamSheepEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (mobEntity.canView(MinecraftClient.getInstance().player) && mobEntity.isCorporeal()) super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}