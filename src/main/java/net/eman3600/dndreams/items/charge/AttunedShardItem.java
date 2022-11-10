package net.eman3600.dndreams.items.charge;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class AttunedShardItem extends AbstractChargeItem {
    public final int maxCharge;

    public AttunedShardItem(int maxCharge, Settings settings) {
        super(settings);
        this.maxCharge = maxCharge;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.contains("charge")) {
            int charge = nbt.getInt("charge");

            return Math.round(charge * 13.0f / (float)this.maxCharge);
        }

        return 0;
    }

    @Override
    public ItemStack charge(ItemStack stack, int amount) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains("charge")) {
            nbt.putInt("charge", 0);
        }

        int charge = nbt.getInt("charge");
        charge = Math.min(maxCharge, charge + amount);

        if (charge >= maxCharge) {
            ItemStack shard = new ItemStack(ModItems.CHARGED_SHARD);
            nbt.remove("charge");
            if (nbt.isEmpty()) {
                nbt = null;
            }
            shard.setNbt(nbt);

            return shard;
        }

        nbt.putInt("charge", charge);
        stack.setNbt(nbt);

        return stack;
    }

    @Override
    public boolean canAffordCharge(ItemStack stack, int amount) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains("charge")) {
            nbt.putInt("charge", 0);
        }

        return nbt.getInt("charge") >= amount;
    }

    @Override
    public ItemStack discharge(ItemStack stack, int amount) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains("charge")) {
            nbt.putInt("charge", 0);
        }

        int charge = nbt.getInt("charge");
        charge -= amount;

        if (charge <= 0) {
            ItemStack shard = new ItemStack(Items.AMETHYST_SHARD);
            nbt.remove("charge");
            if (nbt.isEmpty()) {
                nbt = null;
            }
            shard.setNbt(nbt);

            return shard;
        } else {
            nbt.putInt("charge", charge);
            stack.setNbt(nbt);
        }

        return stack;
    }

    @Override
    public int getCharge(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.contains("charge")) {
            return nbt.getInt("charge");
        }

        return 0;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            ItemStack stack = new ItemStack(this);
            charge(stack, 250);

            stacks.add(stack);
        }
    }
}
