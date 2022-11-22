package net.eman3600.dndreams.cardinal_components.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Map;

public interface StatBoonComponentI extends Component {
    void increase(@Nullable Identifier id, double amount);
    default void decrease(@Nullable Identifier id, double amount) {
        increase(id, -amount);
    }
    void set(@Nullable Identifier id, double amount);
    void remove(@Nullable Identifier id);

    void reloadAttributes();

    Map<EntityAttribute, EntityAttributeModifier> getAttributes();
    @Nullable
    EntityAttributeModifier getAttribute(EntityAttribute attribute);
}
