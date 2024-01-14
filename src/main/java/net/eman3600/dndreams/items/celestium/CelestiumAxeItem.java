package net.eman3600.dndreams.items.celestium;

import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CelestiumAxeItem extends AxeItem implements DivineWeaponItem, ManaCostItem {

    public CelestiumAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {

        if (world.isClient) return true;
        int broken = state.getHardness(world, pos) == 0.0f ? 0 : 1;

        if (isLog(state) && !miner.isSneaking() && miner instanceof PlayerEntity player && canAffordMana(player, stack)) {
            broken += chainBreak(world, pos, 20, miner);
            spendMana(player, stack);
        }

        stack.damage(broken, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));

        return true;
    }

    private boolean isTree(BlockState state) {
        return state != null && (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.WART_BLOCKS) || state.isOf(Blocks.MANGROVE_ROOTS));
    }

    private boolean isLog(BlockState state) {
        return state != null && (state.isIn(BlockTags.LOGS) || state.isOf(Blocks.MANGROVE_ROOTS));
    }

    public int chainBreak(World world, BlockPos pos, int tries, LivingEntity miner) {

        int broken = 0;
        for (Direction dir: Direction.values()) {
            BlockPos offset = pos.offset(dir);
            BlockState state = world.getBlockState(offset);

            if (isTree(state)) {
                world.breakBlock(offset, true, miner);
                broken += state.getHardness(world, pos) == 0.0f ? 0 : 1;
                if (tries > 0) broken += chainBreak(world, offset, tries - 1, miner);
            }
        }
        return broken;
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
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) return ActionResult.PASS;

        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
        tooltip.add(getTooltipMana(stack));
    }

    @Override
    public int getBaseManaCost() {
        return 7;
    }
}
