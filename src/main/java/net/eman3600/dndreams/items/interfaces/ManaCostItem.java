package net.eman3600.dndreams.items.interfaces;

import com.mojang.brigadier.Message;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

public interface ManaCostItem {
    int getManaCost();
    default boolean canAffordMana(PlayerEntity player) {
        if (player != null)
            return EntityComponents.MANA.get(player).getMana() >= getManaCost();
        return false;
    }
    default Text getTooltipMana() {
        return Texts.toText(() -> "§dMana Cost: " + getManaCost());
    }
}
