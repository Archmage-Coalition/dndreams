package net.eman3600.dndreams.mixin_interfaces;

import net.minecraft.entity.ItemEntity;

public interface ItemEntityAccess {

    boolean isTransmutable();
    void setTransmutable(boolean transmutable);


    static void markUnbrewable(ItemEntity entity) {
        if (entity instanceof ItemEntityAccess access) {
            access.markUnbrewable();
        }
    }
    static boolean isUnbrewable(ItemEntity entity) {
        if (entity instanceof ItemEntityAccess access) {
            return access.isUnbrewable();
        }
        return false;
    }
    static void markTransmutable(ItemEntity entity, boolean transmutable) {
        if (entity instanceof ItemEntityAccess access) {
            access.setTransmutable(transmutable);
        }
    }
    static boolean isTransmutable(ItemEntity entity) {
        if (entity instanceof ItemEntityAccess access) {
            return access.isTransmutable();
        }
        return false;
    }

    void markUnbrewable();
    boolean isUnbrewable();
}
