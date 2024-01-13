package net.eman3600.dndreams.items.trinket;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
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

public class DragonfootBandItem extends TrinketItem {
    public DragonfootBandItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> list = super.getModifiers(stack, slot, entity, uuid);

        list.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier
                (uuid, "dndreams:aglet", .2d, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

        return list;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
        tooltip.add(Text.translatable("tooltip.dndreams.horizontal_air"));
    }
}
