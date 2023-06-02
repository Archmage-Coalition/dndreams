package net.eman3600.dndreams.items.celestium;

import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class CelestiumSwordItem extends SwordItem implements DivineWeaponItem {
    public CelestiumSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
}
