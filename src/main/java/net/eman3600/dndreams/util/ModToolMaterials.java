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
    CORRUPT(3, 1299, 8.0F, 2.0F, 13,
            () -> Ingredient.ofItems(ModItems.CORRUPT_INGOT)),
    CELESTIUM(4, 2145, 14.5F, 6.0F, 18,
            () -> Ingredient.ofItems(ModItems.CELESTIUM)),
    CELESTIUM_SLOW(4, 2145, 7F, 6.0F, 18,
            () -> Ingredient.ofItems(ModItems.CELESTIUM)),
    VITAL(1, 575, 4.0F, 1.0F, 12,
            () -> Ingredient.ofItems(ModItems.VITAL_SHARD)),
    MANAGOLD(2, 32, 12.0F, 2.0F, 22,
            () -> Ingredient.ofItems(ModItems.MANAGOLD_INGOT)),
    TORMITE(4, 2145, 18.5F, 8.0F, 22,
            () -> Ingredient.ofItems(ModItems.NIGHTMARE_FUEL)),
    PERICHARITE(4, 3122, 23F, 11.0F, 22,
            () -> Ingredient.ofItems(ModItems.PERICHARITE)),
    CROWNED_EDGE(4, 3122, 12.0F, 3.0F, 18,
            () -> Ingredient.ofItems(ModItems.SCULK_POWDER)),
    TRUE_EDGE(4, 6244, 26.0F, 6.0F, 18,
            () -> Ingredient.ofItems(ModItems.NIGHTMARE_FUEL)),
    CLOUD(4, 1562, 12.0F, 6.0F, 18,
            () -> Ingredient.ofItems(ModItems.CLOUD));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;

    ModToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
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
