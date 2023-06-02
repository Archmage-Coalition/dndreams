package net.eman3600.dndreams.items.pericharite;

import net.eman3600.dndreams.items.interfaces.DivineWeaponItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class PerichariteSwordItem extends SwordItem implements DivineWeaponItem {
    public PerichariteSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
}
