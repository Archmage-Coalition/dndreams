package net.eman3600.dndreams.items.tormite;

import net.eman3600.dndreams.items.ModArmorItem;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TormiteArmorItem extends ModArmorItem {
    public TormiteArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings, (builder, uUID) -> {});
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable("tooltip.dndreams.tormite_armor"));
        if (world instanceof ClientWorldAccess access && access.getPlayer() != null && wornPieces(access.getPlayer()) >= 4) {
            tooltip.add(Text.translatable("tooltip.dndreams.tormite_armor.set_bonus.0"));
            tooltip.add(Text.translatable("tooltip.dndreams.tormite_armor.set_bonus.1"));
        }
    }

    public static int wornPieces(Entity entity) {
        int wornPieces = 0;

        for (ItemStack stack: entity.getArmorItems()) {
            wornPieces += stack.getItem() instanceof TormiteArmorItem ? 1 : 0;
        }

        return wornPieces;
    }
}
