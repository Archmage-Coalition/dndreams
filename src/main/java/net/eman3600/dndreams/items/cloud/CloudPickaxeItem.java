package net.eman3600.dndreams.items.cloud;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CloudPickaxeItem extends PickaxeItem {
    /**
     * Access widened by fabric-transitive-access-wideners-v1 to accessible
     * Access widened by architectury to accessible
     *
     * @param material
     * @param attackDamage
     * @param attackSpeed
     * @param settings
     */
    public CloudPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        HitResult hit = user.raycast(30, 0, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult result = (BlockHitResult) hit;

            BlockPos pos = result.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.getHardness(world, pos) >= 0 && world.getBlockEntity(pos) == null && state.getBlock().getBlastResistance() < 1000f && FallingBlock.canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= world.getBottomY()) {

                if (!world.isClient) {
                    FallingBlockEntity entity = FallingBlockEntity.spawnFromBlock(world, pos, state);
                    entity.setHurtEntities(2f, state.isIn(BlockTags.ANVIL) || state.isOf(Blocks.POINTED_DRIPSTONE) ? 40 : 20);
                    stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
                return TypedActionResult.success(stack);
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }
}
