package net.eman3600.dndreams.items.trinket;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class FleetfootBandItem extends TrinketItem {
    public FleetfootBandItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> list = super.getModifiers(stack, slot, entity, uuid);

        list.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier
                (uuid, "dndreams:aglet", .15d, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

        return list;
    }
}
