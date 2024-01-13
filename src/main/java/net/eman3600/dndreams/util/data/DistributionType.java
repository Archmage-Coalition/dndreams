package net.eman3600.dndreams.util.data;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public enum DistributionType {
    INGESTED(ModItems.BREW_INGESTED, 0, 1),
    SPLASH(ModItems.BREW_SPLASH, 0, 1, Items.GUNPOWDER, ModItems.LOTUS_FLOWER),
    LINGERING(ModItems.BREW_LINGERING, 0, 1, Items.DRAGON_BREATH, Items.HONEY_BOTTLE);


    @Nullable
    public final Item brewItem;
    public final int capacity;
    public final int levels;
    public final Item[] ingredients;

    DistributionType(@Nullable Item brewItem, int capacity, int levels, Item... ingredients) {
        this.brewItem = brewItem;
        this.capacity = capacity;
        this.levels = levels;
        this.ingredients = ingredients;
    }

    public static HashMap<Item, DistributionType> asMap() {
        HashMap<Item, DistributionType> map = new HashMap<>();

        for (DistributionType type: values()) {
            for (Item item: type.ingredients) {
                map.put(item, type);
            }
        }

        return map;
    }
}
