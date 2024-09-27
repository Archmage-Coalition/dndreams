package net.eman3600.dndreams.blocks.renderer;

import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class BonfireBlockEntityRenderer implements BlockEntityRenderer<BonfireBlockEntity> {
    private final ItemRenderer itemRenderer;
    private final ItemStack stack;
    private final Random random = Random.create();

    public BonfireBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        stack = ItemStack.EMPTY;
    }


    @Override
    public void render(BonfireBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isEmpty()) return;

        float t;
        float s;
        matrices.push();

        int j = Item.getRawId(stack.getItem());
        this.random.setSeed(j);
        BakedModel bakedModel = this.itemRenderer.getModel(stack, entity.getWorld(), null, j);

        boolean bl = bakedModel.hasDepth();
        int k = 1;
        float l = 0.45f;
        float m = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.FIXED).scale.getY();
        matrices.translate(0.5, l + 0.25f * m, 0.5);
        float n = entity.getCachedState().get(Properties.HORIZONTAL_FACING).asRotation();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(n));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(135f));
        float o = bakedModel.getTransformation().fixed.scale.getX();
        float p = bakedModel.getTransformation().fixed.scale.getY();
        float q = bakedModel.getTransformation().fixed.scale.getZ();
        if (!bl) {
            float r = -0.0f * (float)(k - 1) * 0.5f * o;
            s = -0.0f * (float)(k - 1) * 0.5f * p;
            t = -0.09375f * (float)(k - 1) * 0.5f * q;
            matrices.translate(r, s, t);
        }
        for (int u = 0; u < k; ++u) {
            matrices.push();
            if (u > 0) {
                if (bl) {
                    s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    t = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float v = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    matrices.translate(s, t, v);
                } else {
                    s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    t = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    matrices.translate(s, t, 0.0);
                }
            }
            this.itemRenderer.renderItem(stack, ModelTransformation.Mode.FIXED, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, bakedModel);
            matrices.pop();
            if (bl) continue;
            matrices.translate(0.0f * o, 0.0f * p, 0.09375f * q);
        }

        matrices.pop();
    }
}
