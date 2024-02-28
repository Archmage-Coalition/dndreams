package net.eman3600.dndreams.items;

import com.google.common.collect.ImmutableMultimap;
import net.eman3600.dndreams.mixin_interfaces.ArmorItemAccess;
import net.eman3600.dndreams.util.Consumer2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ModArmorItem extends ArmorItem {
    private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    public ModArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings, Consumer2<ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier>, UUID> specialAttributes) {
        super(material, slot, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = MODIFIERS[slot.getEntitySlotId()];
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uUID, "Armor modifier", this.getProtection(), EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uUID, "Armor toughness", this.getToughness(), EntityAttributeModifier.Operation.ADDITION));

        specialAttributes.accept(builder, uUID);

        if (this instanceof ArmorItemAccess access) {
            access.setAttributes(builder.build());
        }
    }

    public static boolean isWearing(Entity entity, Item item) {
        for (ItemStack stack: entity.getArmorItems()) {
            if (stack.isOf(item)) return true;
        }
        return false;

    }


}
