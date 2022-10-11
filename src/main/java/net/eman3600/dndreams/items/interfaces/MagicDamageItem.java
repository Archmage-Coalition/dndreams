package net.eman3600.dndreams.items.interfaces;

import net.minecraft.text.Text;

public interface MagicDamageItem {
    float getMagicDamage();

    default Text getTooltipMagicDamage() {
        if (getMagicDamage() % 1 == 0f) {
            return Text.translatable("tooltip.dndreams.magic_damage", (int)getMagicDamage());
        } else {
            return Text.translatable("tooltip.dndreams.magic_damage", getMagicDamage());
        }
    }
}
