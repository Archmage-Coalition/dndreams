package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.items.enchantments.ManaCostEnchantment;
import net.eman3600.dndreams.items.enchantments.PotencyEnchantment;
import net.eman3600.dndreams.items.edge_series.CrownedEdgeItem.CrownedEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {

    public static final Enchantment POTENCY = register("potency", new PotencyEnchantment(Enchantment.Rarity.COMMON, 4, EquipmentSlot.values()));
    public static final Enchantment THRIFTY = register("thrifty", new ManaCostEnchantment(Enchantment.Rarity.UNCOMMON, 3, EquipmentSlot.values()));
    public static final Enchantment WICKED = register("wicked", new CrownedEnchantment(Enchantment.Rarity.UNCOMMON, 1, EquipmentSlot.values()));

    public static Enchantment register(String id, Enchantment enchant) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(Initializer.MODID, id), enchant);
    }

    public static void registerEnchants() {
        System.out.println("Registering enchantments for " + Initializer.MODID);
    }
}
