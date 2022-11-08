package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.DreamingComponentI;
import net.eman3600.dndreams.initializers.*;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class DreamingComponent implements DreamingComponentI {
    private PlayerEntity player;

    private boolean dreaming = false;
    private Vec3d returnPos;
    private PlayerInventory storedInv;
    private boolean hasDreamt = false;
    private boolean congealed = false;


    public DreamingComponent(PlayerEntity player) {
        this.player = player;
        returnPos = player.getPos();
        storedInv = new PlayerInventory(player);
    }

    private TormentComponent torment() {
        return EntityComponents.TORMENT.get(player);
    }

    public void congeal() {
        congealed = true;
    }

    @Override
    public boolean isCongealed() {
        return congealed;
    }

    @Override
    public void changeDimension(boolean toDream) {
        dreaming = toDream;

        PlayerInventory currInv = new PlayerInventory(player);

        transferInventories(currInv, player.getInventory());
        transferInventories(player.getInventory(), storedInv);
        transferInventories(storedInv, currInv);
        currInv.clear();

        player.clearStatusEffects();

        if (toDream) {
            player.incrementStat(ModStats.ENTER_DREAM);

            if (!hasDreamt) {
                hasDreamt = true;
                player.getInventory().insertStack(new ItemStack(ModItems.BOOK_OF_DREAMS, 1));
            }

            returnPos = player.getPos();
            player.setHealth((float)player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 4), player);
            player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.GRACE, 60, 0), player);
            torment().setTorment(0);
        } else {
            player.setPosition(returnPos);
            player.setHealth((float)player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 4), player);
            player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.GRACE, 60, 0), player);
            torment().setTorment(0);

            if (congealed) {
                List<ItemStack> stacks = new ArrayList<>();

                for (int i = 0; i < storedInv.size(); i++) {
                    ItemStack stack = storedInv.getStack(i);

                    if (stack.isIn(ModTags.DREAM_EXCLUSIVE)) stacks.add(stack);
                }

                DefaultedList<ItemStack> dreamStacks = DefaultedList.copyOf(ItemStack.EMPTY, stacks.toArray(new ItemStack[0]));

                for (int i = 0; i < dreamStacks.size(); i++) {
                    if (player.getInventory().insertStack(dreamStacks.get(i))) {
                        dreamStacks.set(i, ItemStack.EMPTY);
                    } else {
                        ItemScatterer.spawn(player.world, player.getBlockPos(), dreamStacks);

                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean isDreaming() {
        return dreaming;
    }

    @Override
    public PlayerInventory storedInv() {
        return storedInv;
    }

    @Override
    public Vec3d returnPos() {
        return returnPos;
    }

    @Override
    public void serverTick() {
        if (dreaming && getDimension() != ModDimensions.DREAM_TYPE_KEY) {
            changeDimension(false);
        } else if (!dreaming && getDimension() == ModDimensions.DREAM_TYPE_KEY) {
            changeDimension(true);
        } else if (dreaming) {
            torment().addPerMinute(10f);
        }

        EntityComponents.DREAMING.sync(player);
    }

    private RegistryKey<DimensionType> getDimension() {
        return player.getWorld().getDimensionKey();
    }

    private void transferInventories(PlayerInventory into, PlayerInventory outOf) {
        into.clear();
        for (int i = 0; i < outOf.size(); i++) {
            into.setStack(i, outOf.getStack(i));
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtList inventoryList = tag.getList("stored_inv", 10);
        NbtList posList = tag.getList("return_pos", 6);


        storedInv.readNbt(inventoryList);
        returnPos = new Vec3d(posList.getDouble(0),posList.getDouble(1),posList.getDouble(2));
        dreaming = tag.getBoolean("dreaming");
        hasDreamt = tag.getBoolean("has_dreamt");
        congealed = tag.getBoolean("congealed");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.put("stored_inv", storedInv.writeNbt(new NbtList()));
        tag.put("return_pos", this.toNbtList(returnPos.getX(),returnPos.getY(),returnPos.getZ()));
        tag.putBoolean("dreaming", dreaming);
        tag.putBoolean("has_dreamt", hasDreamt);
        tag.putBoolean("congealed", congealed);
    }

    private NbtList toNbtList(double... values) {
        NbtList nbtList = new NbtList();
        double[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            double d = var3[var5];
            nbtList.add(NbtDouble.of(d));
        }

        return nbtList;
    }
}
