package net.eman3600.dndreams.items.misc_tool;

import net.eman3600.dndreams.items.interfaces.VariableMineSpeedItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResonantPickaxeItem extends PickaxeItem implements VariableMineSpeedItem {
    public ResonantPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }


    @Override
    public float additionalMiningModifiers(ItemStack stack, PlayerEntity entity, BlockState state, World world) {
        if (state.isIn(BlockTags.PICKAXE_MINEABLE) && state.getBlock().getHardness() > 0f) {
            return 0.46875f * state.getBlock().getHardness();
        }
        return 1;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }
}
