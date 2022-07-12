package net.eman3600.dndreams.items.tool_mirror;

import com.mojang.datafixers.util.Pair;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;

public class ModHoeItem extends HoeItem {
    public ModHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public static void injectTillActions() {
        TILLING_ACTIONS.put(ModBlocks.DREAM_GRASS, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())));
    }
}
