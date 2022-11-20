package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.item.Item;

import javax.annotation.Nonnegative;

public interface PermItemComponentI extends Component {
    boolean pair(Item item);

    void decrement(Item item);
    void decrement(Item item, @Nonnegative int amount);

    boolean canUse(Item item);
}
