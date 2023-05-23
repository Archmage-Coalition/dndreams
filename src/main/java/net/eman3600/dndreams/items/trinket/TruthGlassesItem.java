package net.eman3600.dndreams.items.trinket;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class TruthGlassesItem extends TrinketItem {
    public TruthGlassesItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<EntityAttribute, EntityAttributeModifier> list = super.getModifiers(stack, slot, entity, uuid);

        list.put(ModAttributes.PLAYER_MANA_REGEN, new EntityAttributeModifier
                (uuid, "dndreams:face", .40d, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

        return list;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.0"));
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip.1"));
    }
}
