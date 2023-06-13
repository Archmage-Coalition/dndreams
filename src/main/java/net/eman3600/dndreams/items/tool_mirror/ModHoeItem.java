package net.eman3600.dndreams.items.tool_mirror;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModHoeItem extends HoeItem {
    public ModHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public static void injectTillActions() {

    }

    public static Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> getTillingActions() {
        return TILLING_ACTIONS;
    }
}
