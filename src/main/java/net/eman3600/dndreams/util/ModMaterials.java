package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModMaterials implements ToolMaterial {
    CORRUPT(3, 1785, 4.0F, 3.0F, 18,
            () -> Ingredient.ofItems(new ItemConvertible[]{ModItems.CORRUPT_INGOT})),
    HOLY(4, 1824, 12.0F, 5.0F, 18,
            () -> Ingredient.ofItems(new ItemConvertible[]{ModItems.HOLY_INGOT})),
    ARTIFACT(4, 3002, 12.0F, 4.0F, 22,
            () -> Ingredient.ofItems(new ItemConvertible[]{ModItems.DREAM_POWDER})),
    MANAGOLD(2, 32, 12.0F, 2.0F, 22,
            () -> Ingredient.ofItems(new ItemConvertible[]{ModItems.MANAGOLD_INGOT})),
    TORMITE(4, 128, 12.0F, 4.0F, 22,
            () -> Ingredient.ofItems(new ItemConvertible[]{ModItems.TORMITE_INGOT}));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;

    private ModMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
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
        return (Ingredient)this.repairIngredient.get();
    }
}
