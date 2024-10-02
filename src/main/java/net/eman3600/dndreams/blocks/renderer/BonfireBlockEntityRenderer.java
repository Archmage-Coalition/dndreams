package net.eman3600.dndreams.blocks.renderer;

import net.eman3600.dndreams.blocks.energy.BonfireBlock;
import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.minecraft.block.CampfireBlock;
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
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class BonfireBlockEntityRenderer implements BlockEntityRenderer<BonfireBlockEntity> {
    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    public BonfireBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }


    @Override
    public void render(BonfireBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack focusStack = entity.getRitualFocus();

        if (!focusStack.isEmpty()) {
            float t;
            float s;
            matrices.push();

            int j = Item.getRawId(focusStack.getItem());
            this.random.setSeed(j);
            BakedModel bakedModel = this.itemRenderer.getModel(focusStack, entity.getWorld(), null, j);

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
                float r = -0.0f * (float) (k - 1) * 0.5f * o;
                s = -0.0f * (float) (k - 1) * 0.5f * p;
                t = -0.09375f * (float) (k - 1) * 0.5f * q;
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
                this.itemRenderer.renderItem(focusStack, ModelTransformation.Mode.FIXED, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, bakedModel);
                matrices.pop();
                if (bl) continue;
                matrices.translate(0.0f * o, 0.0f * p, 0.09375f * q);
            }

            matrices.pop();
        }

        Direction direction = entity.getCachedState().get(BonfireBlock.FACING);
        DefaultedList<ItemStack> defaultedList = entity.getRitualItems();
        int r = (int)entity.getPos().asLong();
        for (int i = 0; i < 4; ++i) {
            ItemStack itemStack = defaultedList.get(i);
            if (itemStack == ItemStack.EMPTY) continue;
            matrices.push();
            matrices.translate(0.5, 0.44921875, 0.5);
            Direction direction2 = Direction.fromHorizontal((i + direction.getHorizontal()) % 4);
            float g = -direction2.asRotation();
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(g));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
            matrices.translate(-0.3125, -0.3125, 0.0);
            matrices.scale(0.375f, 0.375f, 0.375f);
            this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, r + i);
            matrices.pop();
        }
    }
}
