package net.eman3600.dndreams.items.celestium;

import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
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

public class CelestiumPickaxeItem extends PickaxeItem implements ManaCostItem {

    public CelestiumPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {

        float hardnessThreshold = state.getHardness(world, pos) * 1.5f;

        if (!world.isClient && hardnessThreshold != 0.0f) {
            int damage = 1;

            if (isSuitableFor(state) && !miner.isSneaking() && miner instanceof PlayerEntity player && canAffordMana(player, stack)) {
                for (int i = -1; i < 2; i++) for (int j = -1; j < 2; j++) for (int k = -1; k < 2; k++) {

                    BlockPos extraPos = pos.add(i, j, k);
                    BlockState extraState = world.getBlockState(extraPos);

                    if (isSuitableFor(extraState) && extraState.getHardness(world, extraPos) > 0 && extraState.getHardness(world, extraPos) < hardnessThreshold) {
                        world.breakBlock(extraPos, true, miner);
                        damage += 1;
                    }
                }
                spendMana(player, stack);
            }

            stack.damage(damage, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public int getBaseManaCost() {
        return 3;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
        tooltip.add(getTooltipMana(stack));
    }
}
