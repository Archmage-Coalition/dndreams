package net.eman3600.dndreams.cardinal_components;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.cardinal_components.interfaces.DreamingComponentI;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModStats;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.fabric.api.util.NbtType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DreamingComponent implements DreamingComponentI {
    private PlayerEntity player;

    private boolean dreaming = false;
    private Vec3d returnPos;
    private PlayerInventory storedInv;
    private Map<String, Map<String, TrinketInventory>> storedTrinkets = new HashMap<>();
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

        TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(player).get();
        Map<String, Map<String, TrinketInventory>> trinkets = trinketComponent.getInventory();
        Map<String, Map<String, TrinketInventory>> currTrinkets = new HashMap<>();

        trinketComponent.clearModifiers();
        transferInventories(currTrinkets, trinkets);
        transferInventories(trinkets, storedTrinkets);
        transferInventories(storedTrinkets, currTrinkets);
        currTrinkets.clear();
        trinketComponent.update();
        TrinketsApi.TRINKET_COMPONENT.sync(player);

        player.clearStatusEffects();
        EntityComponents.ROT.get(player).setRot(0);

        if (toDream) {
            player.incrementStat(ModStats.ENTER_DREAM);

            if (!hasDreamt) {
                hasDreamt = true;
                player.getInventory().insertStack(new ItemStack(ModItems.BOOK_OF_DREAMS, 1));
            }

            returnPos = player.getPos();
            player.setHealth(player.getMaxHealth());
            if (Float.isNaN(player.getHealth())) {
                player.setHealth(20);
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 4), player);
            EntityComponents.SHOCK.maybeGet(player).ifPresent(component -> component.setCushioned(true));

            if (storedInv.contains(ModTags.ATLAS)) {
                List<ItemStack> stacks = new ArrayList<>();

                for (int i = 0; i < storedInv.size(); i++) {
                    ItemStack stack = storedInv.getStack(i);

                    if (stack.isIn(ModTags.ATLAS)) stacks.add(stack);
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

        } else {
            player.setPosition(returnPos);
            player.setHealth((float)player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
            if (Float.isNaN(player.getHealth())) {
                player.setHealth(20);
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 4), player);
            EntityComponents.SHOCK.maybeGet(player).ifPresent(component -> component.setCushioned(true));

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

        EntityComponents.DREAMING.sync(player);
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
            torment().lowerPerMinute(10f);
        }
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

    private void transferInventories(Map<String, Map<String, TrinketInventory>> into, Map<String, Map<String, TrinketInventory>> outOf) {
        into.clear();
        for (String key: outOf.keySet()) {
            Map<String, TrinketInventory> map = new HashMap<>();

            for (String key2: outOf.get(key).keySet()) {
                map.put(key2, outOf.get(key).get(key2));
            }

            into.put(key, map);
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

        NbtCompound nbt = tag.getCompound("stored_trinkets");
        for (String groupKey: nbt.getKeys()) {
            NbtCompound groupTag = nbt.getCompound(groupKey);
            Map<String, TrinketInventory> groupSlots = storedTrinkets.get(groupKey);
            if (groupSlots != null) {
                for (String slotKey : groupTag.getKeys()) {
                    NbtCompound slotTag = groupTag.getCompound(slotKey);
                    NbtList list = slotTag.getList("Items", NbtType.COMPOUND);
                    TrinketInventory inv = groupSlots.get(slotKey);

                    if (inv != null) {
                        inv.fromTag(slotTag.getCompound("Metadata"));
                    }

                    for (int i = 0; i < list.size(); i++) {
                        NbtCompound c = list.getCompound(i);
                        ItemStack stack = ItemStack.fromNbt(c);
                        if (inv != null && i < inv.size()) {
                            inv.setStack(i, stack);
                        } else {
                            storedInv.insertStack(stack);
                        }
                    }
                }
            } else {
                for (String slotKey : groupTag.getKeys()) {
                    NbtCompound slotTag = groupTag.getCompound(slotKey);
                    NbtList list = slotTag.getList("Items", NbtType.COMPOUND);
                    for (int i = 0; i < list.size(); i++) {
                        NbtCompound c = list.getCompound(i);
                        storedInv.insertStack(ItemStack.fromNbt(c));
                    }
                }
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.put("stored_inv", storedInv.writeNbt(new NbtList()));
        tag.put("return_pos", this.toNbtList(returnPos.getX(),returnPos.getY(),returnPos.getZ()));
        tag.putBoolean("dreaming", dreaming);
        tag.putBoolean("has_dreamt", hasDreamt);
        tag.putBoolean("congealed", congealed);

        NbtCompound trinketsNbt = new NbtCompound();

        for (Map.Entry<String, Map<String, TrinketInventory>> group : this.storedTrinkets.entrySet()) {
            NbtCompound groupTag = new NbtCompound();
            for (Map.Entry<String, TrinketInventory> slot : group.getValue().entrySet()) {
                NbtCompound slotTag = new NbtCompound();
                NbtList list = new NbtList();
                TrinketInventory inv = slot.getValue();
                for (int i = 0; i < inv.size(); i++) {
                    NbtCompound c = inv.getStack(i).writeNbt(new NbtCompound());
                    list.add(c);
                }
                slotTag.put("Metadata", inv.toTag());
                slotTag.put("Items", list);
                groupTag.put(slot.getKey(), slotTag);
            }
            trinketsNbt.put(group.getKey(), groupTag);
        }

        tag.put("stored_trinkets", trinketsNbt);
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
