package net.eman3600.dndreams.items.celestium;

import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.items.misc_tool.ScytheItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CelestiumScytheItem extends ScytheItem implements ManaCostItem {

    public CelestiumScytheItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(context.getSide());
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();
        if (player == null || !player.isSneaking()) {
            ActionResult result = super.useOnBlock(context);

            if (result != ActionResult.PASS) {
                return result;
            }
        }
        if (canAffordMana(player, stack)) {
            ItemStack syntheticBoneMeal = Items.BONE_MEAL.getDefaultStack();
            if (BoneMealItem.useOnFertilizable(syntheticBoneMeal, world, blockPos)) {
                if (!world.isClient) {
                    world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                    spendMana(player, stack);
                    if (!player.isCreative()) {
                        context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
                    }
                }
                return ActionResult.success(world.isClient);
            }
            BlockState blockState = world.getBlockState(blockPos);
            boolean bl = blockState.isSideSolidFullSquare(world, blockPos, context.getSide());
            if (bl && BoneMealItem.useOnGround(syntheticBoneMeal, world, blockPos2, context.getSide())) {
                if (!world.isClient) {
                    world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos2, 0);
                    spendMana(player, stack);
                    if (!player.isCreative()) {
                        context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
                    }
                }
                return ActionResult.success(world.isClient);
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public int getBaseManaCost() {
        return 5;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
        tooltip.add(getTooltipMana(stack));
    }
}
