package net.eman3600.dndreams.entities.renderers.features;

import net.eman3600.dndreams.entities.mobs.DreamSheepEntity;
import net.eman3600.dndreams.entities.models.DreamSheepEntityModel;
import net.eman3600.dndreams.entities.models.DreamSheepWoolEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class DreamSheepWoolFeatureRenderer
        extends FeatureRenderer<DreamSheepEntity, DreamSheepEntityModel<DreamSheepEntity>> {
    private static final Identifier SKIN = new Identifier("textures/entity/sheep/sheep_fur.png");
    private final DreamSheepWoolEntityModel<DreamSheepEntity> model;

    public DreamSheepWoolFeatureRenderer(FeatureRendererContext<DreamSheepEntity, DreamSheepEntityModel<DreamSheepEntity>> context, EntityModelLoader loader) {
        super(context);
        this.model = new DreamSheepWoolEntityModel<>(loader.getModelPart(EntityModelLayers.SHEEP_FUR));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, DreamSheepEntity sheepEntity, float f, float g, float h, float j, float k, float l) {
        if (!sheepEntity.canView(MinecraftClient.getInstance().player) || !sheepEntity.isCorporeal()) return;
        float u;
        float t;
        float s;
        if (sheepEntity.isSheared()) {
            return;
        }
        if (sheepEntity.isInvisible()) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            boolean bl = minecraftClient.hasOutline(sheepEntity);
            if (bl) {
                (this.getContextModel()).copyStateTo(this.model);
                this.model.animateModel(sheepEntity, f, g, h);
                this.model.setAngles(sheepEntity, f, g, j, k, l);
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(SKIN));
                this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(sheepEntity, 0.0f), 0.0f, 0.0f, 0.0f, 1.0f);
            }
            return;
        }
        int n = sheepEntity.age / 25 + sheepEntity.getId();
        int o = DyeColor.values().length;
        int p = n % o;
        int q = (n + 1) % o;
        float r = ((float)(sheepEntity.age % 25) + h) / 25.0f;
        float[] fs = SheepEntity.getRgbColor(DyeColor.byId(p));
        float[] gs = SheepEntity.getRgbColor(DyeColor.byId(q));
        s = fs[0] * (1.0f - r) + gs[0] * r;
        t = fs[1] * (1.0f - r) + gs[1] * r;
        u = fs[2] * (1.0f - r) + gs[2] * r;
        net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer.render(this.getContextModel(), this.model, SKIN, matrixStack, vertexConsumerProvider, i, sheepEntity, f, g, j, k, l, h, s, t, u);
    }
}
