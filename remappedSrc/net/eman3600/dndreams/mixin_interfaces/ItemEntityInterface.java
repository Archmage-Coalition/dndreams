package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

public interface ItemEntityInterface {

    boolean getFloating();
    void setFloating(boolean floating);
    int getWindupTicks();
    void setWindupTicks(int ticks);

    default void incrementWindupTicks(int amount) {
        setWindupTicks(getWindupTicks() + amount);
    }


    static void markUnbrewable(ItemEntity entity) {
        if (entity instanceof ItemEntityInterface access) {
            access.markUnbrewable();
        }
    }
    static boolean isUnbrewable(ItemEntity entity) {
        if (entity instanceof ItemEntityInterface access) {
            return access.isUnbrewable();
        }
        return false;
    }

    void markUnbrewable();
    boolean isUnbrewable();
}
