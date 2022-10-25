package net.eman3600.dndreams.items.charge;

import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

public class ChargedShardItem extends AbstractChargeItem{
    public ChargedShardItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack charge(ItemStack stack, int amount) {
        return stack;
    }

    @Override
    public boolean canAffordCharge(ItemStack stack, int amount) {
        return true;
    }

    @Override
    public ItemStack discharge(ItemStack stack, int amount) {
        NbtCompound nbt = stack.getOrCreateNbt();
        int charge = ((AttunedShardItem)ModItems.ATTUNED_SHARD).maxCharge;

        charge -= amount;
        ItemStack shard;

        if (charge > 0) {
            shard = new ItemStack(ModItems.ATTUNED_SHARD);
            nbt.putInt("charge", charge);
        } else {
            shard = new ItemStack(Items.AMETHYST_SHARD);
        }
        shard.setNbt(nbt);
        return shard;
    }


    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x8923D6;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return 13;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
