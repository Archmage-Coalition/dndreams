package net.eman3600.dndreams.items.celestium;

import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;

public class CelestiumAxeItem extends AxeItem implements DivineWeaponItem {

    public CelestiumAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
}
