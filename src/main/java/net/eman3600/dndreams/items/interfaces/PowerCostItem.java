package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.initializers.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

public interface PowerCostItem {
    float getPowerCost();
    default boolean canAffordPower(PlayerEntity player) {
        if (player != null)
            return EntityComponents.INFUSION.get(player).getPower() >= getPowerCost();
        return false;
    }
    default Text getTooltipPower() {
        return Texts.toText(() -> "§dPower Cost: " + getPowerCost() + "%");
    }
}
