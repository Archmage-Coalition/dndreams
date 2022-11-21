package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.item.Item;

import javax.annotation.Nonnegative;

public interface PermItemComponentI extends AutoSyncedComponent {
    boolean pair(Item item);

    void decrement(Item item);
    void decrement(Item item, @Nonnegative int amount);
    int timesUsed(Item item);
    int remainingUses(Item item);

    boolean canUse(Item item);
}
