package net.eman3600.dndreams.items.interfaces;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.minecraft.item.ItemStack;

public interface RitualRemainItem {

    ItemStack getRitualRemains(AbstractRitual ritual, SoulCandleBlockEntity entity, ItemStack stack);
}
