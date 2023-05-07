package net.eman3600.dndreams.mixin_interfaces;

import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface ArmorItemAccess {
    void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> modifiers);
}
