package net.eman3600.dndreams.items.tool_mirror;

import net.eman3600.dndreams.initializers.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;

public class ModShovelItem extends ShovelItem {
    public ModShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public static void injectPathStates() {
        PATH_STATES.put(ModBlocks.DREAM_GRASS, Blocks.DIRT_PATH.getDefaultState());
    }
}
