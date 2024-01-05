package net.eman3600.dndreams.items.celestium;

import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CelestiumAxeItem extends AxeItem implements DivineWeaponItem {

    public CelestiumAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {

        if (world.isClient) return true;

        if (validFall(state)) tryFall(world, pos.up(), 15);

        return super.postMine(stack, world, state, pos, miner);
    }

    public void tryFall(World world, BlockPos pos, int tries) {
        //if (tries <= 0 || world.isOutOfHeightLimit(pos) || !validFall(world.getBlockState(pos))) return;
        if (tries <= 0 || world.isOutOfHeightLimit(pos) || !validLog(world.getBlockState(pos))) return;
        System.out.println("Trying to fell block " + world.getBlockState(pos) + " at " + pos + " with " + tries + " tries remaining.");

        if (fall(world, pos, 8)) for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    tryFall(world, pos.add(i, k, j), tries - 1);
                }
            }
        }
    }

    private boolean validFall(BlockState state) {
        return state != null && (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.WART_BLOCKS) || state.isOf(Blocks.MANGROVE_ROOTS));
    }

    private boolean validLog(BlockState state) {
        return state != null && (state.isIn(BlockTags.LOGS) || state.isOf(Blocks.MANGROVE_ROOTS));
    }

    public boolean fall(World world, BlockPos pos, int distance) {
        if (distance <= 0) return false;

        BlockState lower = world.getBlockState(pos.down());
        if (validFall(lower)) {
            fall(world, pos.down(), distance - 1);
            //System.out.println("Passed check on block " + pos + " with " + (distance - 1) + " blocks remaining.");
        }

        BlockState state = world.getBlockState(pos);
        if (FallingBlock.canFallThrough(lower) && pos.getY() >= world.getBottomY()) {
            FallingBlockEntity.spawnFromBlock(world, pos, state);
            return true;
        }
        return false;
    }

    /*public void tryFall(World world, BlockPos pos, int tries) {
        if (tries <= 0 || world.isOutOfHeightLimit(pos)) return;

        BlockState state = world.getBlockState(pos);

        if (state.isIn(BlockTags.LOGS) || state.isOf(Blocks.MANGROVE_ROOTS)) {

            if (world.getBlockState(pos.down()).isIn(BlockTags.LEAVES)) tryFallLeaves(world, pos.down(), 10);
            if (FallingBlock.canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= world.getBottomY()) {
                FallingBlockEntity.spawnFromBlock(world, pos, state);
            }

            tries--;
            pos = pos.up();
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    tryFall(world, pos.add(i, 0, j), tries);
                }
            }
        }
    }

    public void tryFallLeaves(World world, BlockPos pos, int tries) {

        BlockState down = world.getBlockState(pos.down());
        if (pos.getY() - 1 < world.getBottomY()) return;

        if (down.isIn(BlockTags.LEAVES)) tryFallLeaves(world, pos.down(), tries - 1);

        BlockState state = world.getBlockState(pos);
        if (FallingBlock.canFallThrough(world.getBlockState(pos.down()))) {
            FallingBlockEntity.spawnFromBlock(world, pos, state);
        }
    }*/

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        HitResult hit = user.raycast(12, 0, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult result = (BlockHitResult) hit;

            BlockPos pos = result.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if ((isSuitableFor(state) || validFall(state)) && state.getHardness(world, pos) >= 0 && state.getBlock().getBlastResistance() < 1000f && FallingBlock.canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= world.getBottomY()) {

                if (!world.isClient) {
                    FallingBlockEntity.spawnFromBlock(world, pos, state);
                    stack.damage(1, user, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                }
                return TypedActionResult.success(stack);
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) return ActionResult.PASS;

        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }
}
