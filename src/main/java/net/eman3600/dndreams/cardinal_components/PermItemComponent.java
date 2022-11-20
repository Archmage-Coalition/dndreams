package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.PermItemComponentI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermItemComponent implements PermItemComponentI {
    private final PlayerEntity player;
    private final Map<Item, Integer> permanents = new HashMap<>();
    private static final Map<Item, Integer> defaults = new HashMap<>();
    public static final List<Item> resettables = new ArrayList<>();

    public PermItemComponent(PlayerEntity player) {
        this.player = player;
    }

    private void pair(Item item, int maxUses) {
        permanents.put(item, maxUses);
    }

    @Override
    public boolean pair(Item item) {
        if (defaults.containsKey(item)) {
            pair(item, defaults.get(item));
            return true;
        }
        return false;
    }

    public static void pairDefault(Item item, int maxUses) {
        defaults.put(item, maxUses);
    }

    public static void pairDefault(Item item, int maxUses, boolean resettable) {
        pairDefault(item, maxUses);
        if (resettable && !resettables.contains(item)) {
            resettables.add(item);
        }
    }

    @Override
    public void decrement(Item item, @Nonnegative int amount) {
        if (permanents.containsKey(item)) {
            permanents.put(item, permanents.get(item) - amount);
        } else {
            pair(item);
        }
    }

    @Override
    public void decrement(Item item) {
        decrement(item, 1);
    }

    @Override
    public boolean canUse(Item item) {
        return ((permanents.containsKey(item) || pair(item)) && permanents.get(item) > 0);
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        for (String key: tag.getKeys()) {
            Identifier id = Identifier.tryParse(key);
            Item item = Registry.ITEM.get(id);

            if (item == null) return;
            permanents.put(item, tag.getInt(key));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        for (Item item: permanents.keySet()) {
            Identifier id = Registry.ITEM.getId(item);
            if (id == null) continue;

            tag.putInt(id.toString(), permanents.get(item));
        }
    }
}
