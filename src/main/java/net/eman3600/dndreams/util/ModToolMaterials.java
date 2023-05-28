package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
    RESONANT(3, 1561, 4.0F, 1.0F, 18,
            () -> Ingredient.ofItems(Items.DIAMOND)),
    CORRUPT(3, 1299, 7.0F, 2.0F, 13,
            () -> Ingredient.ofItems(ModItems.CORRUPT_INGOT)),
    CELESTIUM(4, 2145, 12.0F, 5.0F, 18,
            () -> Ingredient.ofItems(ModItems.CELESTIUM)),
    ARTIFACT(4, 3002, 12.0F, 4.0F, 22,
            () -> Ingredient.ofItems(ModItems.DREAM_POWDER, ModItems.SCULK_POWDER)),
    VITAL(1, 575, 4.0F, 1.0F, 12,
            () -> Ingredient.ofItems(ModItems.VITAL_SHARD)),
    TESLA(1, 2145, 4.0F, 2.0F, 12,
            () -> Ingredient.ofItems(Items.COPPER_INGOT)),
    MANAGOLD(2, 32, 12.0F, 2.0F, 22,
            () -> Ingredient.ofItems(ModItems.MANAGOLD_INGOT)),
    TORMITE(4, 2145, 13.0F, 4.0F, 22,
            () -> Ingredient.ofItems(ModItems.NIGHTMARE_FUEL)),
    PERICHARITE(4, 3122, 15.0F, 5.0F, 22,
            () -> Ingredient.ofItems(ModItems.PERICHARITE)),
    SLUMBERING_EDGE(4, 735, 12.0F, 1.0F, 10,
            () -> Ingredient.ofItems(ModItems.NIGHTMARE_FUEL)),
    CROWNED_EDGE(4, 3122, 12.0F, 3.0F, 18,
            () -> Ingredient.ofItems(ModItems.NIGHTMARE_FUEL)),
    LAMENT(4, 6244, 12.0F, 3.0F, 18,
            () -> Ingredient.ofItems(ModItems.NIGHTMARE_FUEL));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;

    private ModToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = new Lazy(repairIngredient);
    }

    public int getDurability() {
        return this.itemDurability;
    }

    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getMiningLevel() {
        return this.miningLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
