package net.eman3600.dndreams.items.hellsteel;

import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.items.ModArmorItem;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CorruptArmorItem extends ModArmorItem {
    public CorruptArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {});
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof LivingEntity living && wornPieces(living) >= 4 && living instanceof LivingEntityAccess access && access.hasNotBrokenLava()) {
            living.addStatusEffect(new StatusEffectInstance(ModStatusEffects.FLAME_GUARD, 145, 0, true, true));
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.dndreams.corrupt_armor"));
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));

        if (world instanceof ClientWorldAccess access && access.getPlayer() != null && wornPieces(access.getPlayer()) >= 4) {
            tooltip.add(Text.translatable("tooltip.dndreams.corrupt_armor.set_bonus"));
        }
    }

    public static int wornPieces(Entity entity) {
        int wornPieces = 0;

        for (ItemStack stack: entity.getArmorItems()) {
            wornPieces += stack.getItem() instanceof CorruptArmorItem ? 1 : 0;
        }

        return wornPieces;
    }
}
