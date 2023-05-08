package net.eman3600.dndreams.items.hellsteel;

import net.eman3600.dndreams.items.ModArmorItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.dndreams.corrupt_armor"));
        tooltip.add(Text.translatable(getTranslationKey() + ".tooltip"));
    }

    public static int wornPieces(Entity entity) {
        int wornPieces = 0;

        for (ItemStack stack: entity.getArmorItems()) {
            wornPieces += stack.getItem() instanceof CorruptArmorItem ? 1 : 0;
        }

        return wornPieces;
    }
}
