package net.eman3600.dndreams.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;

public interface ItemInFlowingSpiritCallback {
    Event<ItemInFlowingSpiritCallback> EVENT = EventFactory.createArrayBacked(ItemInFlowingSpiritCallback.class,
        (listeners) -> item -> {
            for (ItemInFlowingSpiritCallback event : listeners) {
                event.collision(item);
            }
        }
    );

    void collision(ItemEntity item);

}
