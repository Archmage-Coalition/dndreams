package net.eman3600.dndreams.mixin;

import com.google.common.collect.Multimap;
import net.eman3600.dndreams.mixin_interfaces.ArmorItemAccess;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Wearable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin extends Item implements Wearable, ArmorItemAccess {
    @Mutable
    @Shadow @Final private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public ArmorItemMixin(Settings settings) {
        super(settings);
    }

    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> modifiers) {
        this.attributeModifiers = modifiers;
    }
}
