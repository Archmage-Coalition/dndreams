package net.eman3600.dndreams.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.StringIdentifiable;

public enum ExtractionMethod implements StringIdentifiable {
    NONE("none"),
    BOTTLE("bottle"),
    BUCKET("bucket");

    private final String name;

    ExtractionMethod(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean correctExtractor(ItemStack stack) {
        if (this == BOTTLE && stack.isOf(Items.GLASS_BOTTLE)) return true;
        if (this == BUCKET && stack.isOf(Items.BUCKET)) return true;

        return false;
    }

    public static ExtractionMethod fromString(String name) {
        for (ExtractionMethod method : ExtractionMethod.values()) {
            if (name.equals(method.name)) return method;
        }
        return NONE;
    }
}
