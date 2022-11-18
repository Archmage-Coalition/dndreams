package net.eman3600.dndreams.items.edge_series;

import net.eman3600.dndreams.items.interfaces.UnbreakableItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class ShatteredSwordItem extends SwordItem implements UnbreakableItem {
    public ShatteredSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
}
