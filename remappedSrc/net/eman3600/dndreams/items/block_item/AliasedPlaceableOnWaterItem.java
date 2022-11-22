package net.eman3600.dndreams.items.block_item;

import net.minecraft.block.Block;
import net.minecraft.item.PlaceableOnWaterItem;

public class AliasedPlaceableOnWaterItem extends PlaceableOnWaterItem {
    public AliasedPlaceableOnWaterItem(Block block, Settings settings) {
        super(block, settings);
    }

    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }
}
