package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.items.*;
import net.eman3600.dndreams.util.ModFoodComponents;
import net.eman3600.dndreams.util.ModMaterials;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.NetherStarItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {
    /*
     * Declaration
     * */

    // Dream Powder
    public static final Item DREAM_POWDER = registerItem("dream_powder", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Sculk Powder
    public static final Item SCULK_POWDER = registerItem("sculk_powder",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Shade Veil
    public static final Item SHADE_CLOTH = registerItem("shade_cloth", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // World Flow
    public static final Item LIQUID_VOID = registerItem("liquid_void",
            new Item(new FabricItemSettings().group(ItemGroup.BREWING).rarity(Rarity.UNCOMMON)));

    // Liquid Soul
    public static final Item LIQUID_SOUL = registerItem("liquid_soul",
            new Item(new FabricItemSettings().group(ItemGroup.BREWING).rarity(Rarity.RARE)));

    // End Sticks
    public static final Item END_STICK = registerItem("end_stick", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Corrupt Scrap & Raw Celestium
    public static final Item CORRUPT_SCRAP = registerItem("corrupt_scrap", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)
            .fireproof()));
    public static final Item RAW_CELESTIUM = registerItem("raw_celestium",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Baleful Residue
    public static final Item BLIGHT_POWDER = registerItem("blight_powder", new BlightPowderItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Corrupt & Holy Ingots
    public static final Item CORRUPT_INGOT = registerItem("corrupt_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item HOLY_INGOT = registerItem("holy_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Hellsteel Sword & Tools
    public static final Item CORRUPT_SWORD = registerItem("corrupt_sword",
            new CorruptSword(ModMaterials.CORRUPT, 3, -1.8F,
            new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item CORRUPT_PICKAXE = registerItem("corrupt_pickaxe",
            new CorruptPickaxe(ModMaterials.CORRUPT, 1, -2.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_AXE = registerItem("corrupt_axe",
            new CorruptAxe(ModMaterials.CORRUPT, 5.0F, -2.9F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_SHOVEL = registerItem("corrupt_shovel",
            new CorruptShovel(ModMaterials.CORRUPT, 1.5F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_HOE = registerItem("corrupt_hoe",
            new CorruptHoe(ModMaterials.CORRUPT, -3, 0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));

    // Flint and Hellsteel
    public static final Item FLINT_AND_HELLSTEEL = registerItem("flint_and_hellsteel", new FlintAndHellsteel(new FabricItemSettings()
            .group(ItemGroup.TOOLS).fireproof().maxCount(1)));


    // Celestium Swords & Tools
    public static final Item HOLY_SWORD = registerItem("holy_sword",
            new SwordItem(ModMaterials.HOLY, 3, -2.8F,
            new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item HOLY_PICKAXE = registerItem("holy_pickaxe",
            new ModPickaxeItem(ModMaterials.HOLY, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item HOLY_AXE = registerItem("holy_axe",
            new ModAxeItem(ModMaterials.HOLY, 5.0F, -3.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));




    // Excalibur
    public static final Item EXCALIBUR = registerItem("excalibur",
            new Excalibur(ModMaterials.ARTIFACT, 1, -2.6F,
            new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON)));


    // Nightmare Fuel
    public static final Item NIGHTMARE_FUEL = registerItem("nightmare_fuel",
            new NightmareFuel(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.NIGHTMARE_FUEL)));

    // Book of Dreams
    public static final Item BOOK_OF_DREAMS = registerItem("book_of_dreams",
            new BookOfDreams(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));


    /*
     * Item Registration
     * */
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Initializer.MODID, name), item);
    }

    public static void registerItems() {
        System.out.println("Registering items for " + Initializer.MODID);
    }
}
