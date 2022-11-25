package net.eman3600.dndreams.items.misc_tool;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaystoneItem extends Item {
    public WaystoneItem(Settings settings) {
        super(settings);
    }

    public BlockPos boundPos(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("bound_pos")) {
            return readBoundPos(nbt);
        }

        return null;
    }

    public static void writeBoundPos(NbtCompound nbt, BlockPos pos) {
        NbtList list = toNbtList(pos.getX(), pos.getY(), pos.getZ());

        nbt.put("bound_pos", list);
    }

    public static ItemStack boundAt(BlockPos pos, int count) {
        ItemStack stack = new ItemStack(ModItems.WAYSTONE, count);

        NbtCompound nbt = stack.getOrCreateNbt();
        writeBoundPos(nbt, pos);
        stack.setNbt(nbt);

        return stack;
    }

    public static ItemStack boundAt(BlockPos pos) {
        return boundAt(pos, 1);
    }

    @Nullable
    public static BlockPos readBoundPos(NbtCompound nbt) {
        return readBoundPos(nbt, null);
    }

    @Nullable
    public static BlockPos readBoundPos(ItemStack stack) {
        return readBoundPos(stack.getOrCreateNbt());
    }

    @Nullable
    public static BlockPos readBoundPos(NbtCompound nbt, @Nullable BlockPos defaultPos) {
        if (!nbt.contains("bound_pos")) return defaultPos;

        NbtList posList = nbt.getList("bound_pos", NbtCompound.INT_TYPE);

        return new BlockPos(posList.getInt(0),posList.getInt(1),posList.getInt(2));
    }

    private static NbtList toNbtList(int... values) {
        NbtList nbtList = new NbtList();

        for (int d : values) {
            nbtList.add(NbtInt.of(d));
        }

        return nbtList;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("bound_pos")) {
            BlockPos pos = readBoundPos(nbt);

            tooltip.add(Text.translatable(getTranslationKey() + ".tooltip", "§d" + pos.getX(), "§d" + pos.getY(), "§d" + pos.getZ()));
        }
    }
}
