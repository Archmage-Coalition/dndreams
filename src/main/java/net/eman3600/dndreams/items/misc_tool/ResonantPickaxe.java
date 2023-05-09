package net.eman3600.dndreams.items.misc_tool;

import net.eman3600.dndreams.items.interfaces.VariedMineSpeedItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.world.World;

public class ResonantPickaxe extends PickaxeItem implements VariedMineSpeedItem {
    public ResonantPickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }


    @Override
    public float additionalModifiers(ItemStack stack, PlayerEntity entity, BlockState state, World world) {
        if (state.isIn(BlockTags.PICKAXE_MINEABLE) && state.getBlock().getHardness() > 0f) {
            return 0.46875f * state.getBlock().getHardness();
        }
        return 1;
    }
}
