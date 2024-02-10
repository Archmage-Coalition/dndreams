package net.eman3600.dndreams.items.magic_bow;

import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.UseAction;

public abstract class MagicCrossbowItem extends TooltipItem {

    public MagicCrossbowItem(Settings settings) {
        super(settings);
    }

    public abstract int pullTime(ItemStack stack);
    public final float getPullProgress(int useTicks, ItemStack stack) {
        float f = (float)useTicks / (float) pullTime(stack);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 12000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    public static void setCharged(ItemStack stack, boolean charged) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean("Charged", charged);
    }

    public static boolean isCharged(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.getBoolean("Charged");
    }

    public static void setCharges(ItemStack stack, int charges) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt("Charges", charges);
        nbtCompound.putBoolean("Charged", charges > 0);
    }

    public static int getCharges(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.contains("Charges") ? nbtCompound.getInt("Charges") : 0;
    }
}
