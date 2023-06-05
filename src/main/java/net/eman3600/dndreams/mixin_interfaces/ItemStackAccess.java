package net.eman3600.dndreams.mixin_interfaces;

import net.eman3600.dndreams.util.Consumer2;
import net.eman3600.dndreams.util.Function2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;

import java.util.HashMap;
import java.util.Map;

public interface ItemStackAccess {
    Map<TagKey<Item>, Function2<ItemStack, PlayerEntity, Integer>> repairPredicates = new HashMap<>();
    Map<TagKey<Item>, Consumer2<ItemStack, PlayerEntity>> repairCostConsumers = new HashMap<>();

    static void registerRepairPredicate(TagKey<Item> tag, Function2<ItemStack, PlayerEntity, Integer> predicate) {

        repairPredicates.put(tag, predicate);
    }

    static void registerRepairPredicate(TagKey<Item> tag, Function2<ItemStack, PlayerEntity, Integer> predicate, Consumer2<ItemStack, PlayerEntity> consumer) {

        registerRepairPredicate(tag, predicate);
        repairCostConsumers.put(tag, consumer);
    }
}
