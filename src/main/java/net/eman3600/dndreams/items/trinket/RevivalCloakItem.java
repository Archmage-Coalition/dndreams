package net.eman3600.dndreams.items.trinket;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class RevivalCloakItem extends TrinketItem {
    public RevivalCloakItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> list = super.getModifiers(stack, slot, entity, uuid);

        list.put(ModAttributes.PLAYER_REVIVAL, new EntityAttributeModifier
                (uuid, "dndreams:cloak", 1d, EntityAttributeModifier.Operation.ADDITION));

        return list;
    }
}
