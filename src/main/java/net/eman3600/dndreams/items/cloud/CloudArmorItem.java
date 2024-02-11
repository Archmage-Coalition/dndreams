package net.eman3600.dndreams.items.cloud;

import net.eman3600.dndreams.items.ModArmorItem;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CloudArmorItem extends ModArmorItem {
    public CloudArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {
            builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uUID, "Movement speed bonus", .025, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        });
    }
}
