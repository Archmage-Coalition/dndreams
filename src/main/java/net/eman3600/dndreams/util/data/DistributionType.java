package net.eman3600.dndreams.util.data;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;

public enum DistributionType {
    INGESTED(),
    SPLASH(Items.GUNPOWDER, ModItems.WATER_ARTICHOKE_GLOBE),
    LINGERING(Items.DRAGON_BREATH),
    LIQUID(Items.HONEY_BOTTLE),
    GAS(ModItems.EOS_FUME);


    public final Item[] ingredients;

    DistributionType(Item... ingredients) {
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
