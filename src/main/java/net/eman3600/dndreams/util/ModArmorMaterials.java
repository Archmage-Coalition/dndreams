package net.eman3600.dndreams.util;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    MANAGOLD("managold", 7, new int[]{2, 5, 7, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> Ingredient.ofItems(ModItems.MANAGOLD_INGOT)),
    TORMITE("tormite", 45, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4.0F, 0.0F, () -> Ingredient.ofItems(ModItems.SHADE_CLOTH)),
    CORRUPT("corrupt", 29, new int[]{3, 6, 8, 3}, 13, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1f, 0.0f, () -> Ingredient.ofItems(ModItems.CORRUPT_INGOT)),
    CELESTIUM("celestium", 45, new int[]{3, 6, 8, 3}, 18, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F, 0.0F, () -> Ingredient.ofItems(ModItems.CELESTIUM)),
    PERICHARITE("pericharite", 66, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 5.0F, 0.0F, () -> Ingredient.ofItems(ModItems.PERICHARITE)),
    CLOUD("cloud", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2.0f,0.0f, () -> Ingredient.ofItems(ModItems.CLOUD));

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Lazy<Ingredient> repairIngredientSupplier;

    private ModArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = new Lazy(repairIngredientSupplier);
    }

    public static int getEquipCount(PlayerEntity player, ArmorMaterial material) {
        int i = 0;
        for (ItemStack stack: player.getArmorItems()) if (stack.getItem() instanceof ArmorItem item && item.getMaterial() == material) i++;
        return i;
    }

    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
    }

    public int getProtectionAmount(EquipmentSlot slot) {
        return this.protectionAmounts[slot.getEntitySlotId()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredientSupplier.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
