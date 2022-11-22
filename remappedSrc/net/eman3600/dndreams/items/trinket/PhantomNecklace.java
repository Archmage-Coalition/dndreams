package net.eman3600.dndreams.items.trinket;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class PhantomNecklace extends TrinketItem {
    public PhantomNecklace(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> list = super.getModifiers(stack, slot, entity, uuid);

        list.put(ModAttributes.PLAYER_MAX_MANA, new EntityAttributeModifier
                (uuid, "dndreams:necklace", 10d, EntityAttributeModifier.Operation.ADDITION));

        return list;
    }
}
