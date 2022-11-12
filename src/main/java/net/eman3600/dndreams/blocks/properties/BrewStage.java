package net.eman3600.dndreams.blocks.properties;

import net.eman3600.dndreams.blocks.entities.RefinedCauldronBlockEntity;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
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
        if (stack.isOf(Items.FERMENTED_SPIDER_EYE)) return EFFECT;

        try {
            RecipeManager manager = world.getRecipeManager();
            Inventory inv = ImplementedInventory.of(DefaultedList.ofSize(1, stack));

            if (manager.getFirstMatch(ModRecipeTypes.APOTHECARY, inv, world).isPresent()) return EFFECT;
        } catch (NullPointerException ignored) {}

        if (RefinedCauldronBlockEntity.ENHANCEMENTS.containsKey(stack.getItem())) return ENHANCEMENT;
        if (DistributionType.asMap().containsKey(stack.getItem())) return DISTRIBUTION;


        return null;
    }
}
