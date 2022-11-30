package net.eman3600.dndreams.blocks.properties;

import net.eman3600.dndreams.blocks.entities.RefinedCauldronBlockEntity;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.util.data.DistributionType;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.HashMap;

public enum BrewStage {
    BASE,
    CAPACITY,
    EFFECT,
    ENHANCEMENT,
    DISTRIBUTION,
    TIPPING;

    public static BrewStage fromStack(ItemStack stack, World world) {
        if (stack.isOf(Items.NETHER_WART)) return BASE;
        if (RefinedCauldronBlockEntity.CAPACITIES.containsKey(stack.getItem())) return CAPACITY;
        if (stack.isIn(ModTags.CORRUPTORS)) return EFFECT;

        try {
            RecipeManager manager = world.getRecipeManager();
            Inventory inv = ImplementedInventory.of(DefaultedList.ofSize(2, stack));
            inv.setStack(1, ItemStack.EMPTY);

            if (manager.getFirstMatch(ModRecipeTypes.APOTHECARY, inv, world).isPresent()) return EFFECT;
        } catch (NullPointerException ignored) {}

        if (RefinedCauldronBlockEntity.ENHANCEMENTS.containsKey(stack.getItem())) return ENHANCEMENT;
        if (DistributionType.asMap().containsKey(stack.getItem())) return DISTRIBUTION;


        return null;
    }

    public static HashMap<BrewStage, Integer> toStepMap() {
        HashMap<BrewStage, Integer> map = new HashMap<>();

        for (int i = 0; i < values().length; i++) {
            map.put(values()[i], i);
        }

        return map;
    }
}
