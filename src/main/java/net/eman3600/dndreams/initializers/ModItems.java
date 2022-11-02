package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.items.*;
import net.eman3600.dndreams.items.block_item.AliasedPlaceableOnWaterItem;
import net.eman3600.dndreams.items.charge.AttunedShardItem;
import net.eman3600.dndreams.items.charge.ChargedShardItem;
import net.eman3600.dndreams.items.charge.TuningItem;
import net.eman3600.dndreams.items.consumable.*;
import net.eman3600.dndreams.items.creative.InfusionChangerItem;
import net.eman3600.dndreams.items.magic_sword.CorruptSword;
import net.eman3600.dndreams.items.magic_sword.CrownedEdgeItem;
import net.eman3600.dndreams.items.magic_sword.TrueCrownedEdgeItem;
import net.eman3600.dndreams.items.mindstring_bow.LightstringBow;
import net.eman3600.dndreams.items.mindstring_bow.MindstringBow;
import net.eman3600.dndreams.items.tool_mirror.ModAxeItem;
import net.eman3600.dndreams.items.tool_mirror.ModHoeItem;
import net.eman3600.dndreams.items.tool_mirror.ModPickaxeItem;
import net.eman3600.dndreams.items.tool_mirror.ModShovelItem;
import net.eman3600.dndreams.items.trinket.PhantomNecklace;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.util.ModArmorMaterials;
import net.eman3600.dndreams.util.ModFoodComponents;
import net.eman3600.dndreams.util.ModToolMaterials;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModItems {
    /*
     * Declaration
     * */

    // Dream Powder
    public static final Item DREAM_POWDER = registerItem("dream_powder",
            new DreamyItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Sculk Powder
    public static final Item SCULK_POWDER = registerItem("sculk_powder",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Shade Veil
    public static final Item SHADE_CLOTH = registerItem("shade_cloth", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // World Flow
    public static final Item LIQUID_VOID = registerItem("liquid_void",
            new Item(new FabricItemSettings().group(ItemGroup.BREWING).recipeRemainder(Items.GLASS_BOTTLE).rarity(Rarity.UNCOMMON)));

    // Liquid Soul
    public static final Item LIQUID_SOUL = registerItem("liquid_soul",
            new LiquidSoulItem(new FabricItemSettings().group(ItemGroup.BREWING).recipeRemainder(Items.GLASS_BOTTLE).rarity(Rarity.RARE)));

    // Nightmare Fuel
    public static final Item NIGHTMARE_FUEL = registerItem("nightmare_fuel",
            new NightmareFuel(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.NIGHTMARE_FUEL).fireproof()));

    // End Sticks
    public static final Item END_STICK = registerItem("end_stick", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Corrupt Scrap & Raw Celestium
    public static final Item CORRUPT_SCRAP = registerItem("corrupt_scrap", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)
            .fireproof()));
    public static final Item CELESTIUM = registerItem("celestium",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Baleful Residue
    public static final Item INFERNAL_RESIDUE = registerItem("infernal_residue", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Wood Ash
    public static final Item WOOD_ASH = registerItem("wood_ash", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Amethyst Jars & Fumes
    public static final Item AMETHYST_JAR = registerItem("amethyst_jar", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item FOUL_FUME = registerItem("foul_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item SAKURA_FUME = registerItem("sakura_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item DREAMWOOD_FUME = registerItem("dreamwood_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item SCULK_WOOD_FUME = registerItem("sculk_wood_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item JAPANESE_MAPLE_FUME = registerItem("japanese_maple_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));

    // Water Artichoke Globe
    public static final Item WATER_ARTICHOKE_GLOBE = registerItem("water_artichoke_globe",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Ingots
    public static final Item CORRUPT_INGOT = registerItem("corrupt_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item MANAGOLD_INGOT = registerItem("managold_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item TORMITE_INGOT = registerItem("tormite_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));

    // Obsidian Stick
    public static final Item OBSIDIAN_STICK = registerItem("obsidian_stick",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));

    // Hellsteel Sword & Tools
    public static final Item CORRUPT_SWORD = registerItem("corrupt_sword",
            new CorruptSword(ModToolMaterials.CORRUPT, 3, -1.8F,
            new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item CORRUPT_PICKAXE = registerItem("corrupt_pickaxe",
            new CorruptPickaxe(ModToolMaterials.CORRUPT, 1, -2.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_AXE = registerItem("corrupt_axe",
            new CorruptAxe(ModToolMaterials.CORRUPT, 5.0F, -2.9F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_SHOVEL = registerItem("corrupt_shovel",
            new CorruptShovel(ModToolMaterials.CORRUPT, 1.5F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_HOE = registerItem("corrupt_hoe",
            new CorruptHoe(ModToolMaterials.CORRUPT, -3, 0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));

    // Flint and Hellsteel
    public static final Item FLINT_AND_HELLSTEEL = registerItem("flint_and_hellsteel", new FlintAndHellsteelItem(new FabricItemSettings()
            .group(ItemGroup.TOOLS).fireproof().maxCount(1)));

    // Ancient Portal Activation
    public static final Item ANCIENT_SPARK = registerItem("ancient_spark", new AncientSparkItem(new FabricItemSettings()
            .group(ItemGroup.TOOLS).maxCount(1).maxDamage(64)));


    // Celestium Swords & Tools
    public static final Item CELESTIUM_SWORD = registerItem("celestium_sword",
            new SwordItem(ModToolMaterials.CELESTIUM, 3, -2.8F,
            new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CELESTIUM_PICKAXE = registerItem("celestium_pickaxe",
            new ModPickaxeItem(ModToolMaterials.CELESTIUM, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item CELESTIUM_AXE = registerItem("celestium_axe",
            new ModAxeItem(ModToolMaterials.CELESTIUM, 5.0F, -3.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));


    // Mindstring Bow
    public static final Item MINDSTRING_BOW = registerItem("mindstring_bow",
            new MindstringBow(new FabricItemSettings().group(ItemGroup.COMBAT).maxDamage(752)));
    public static final Item LIGHTSTRING_BOW = registerItem("lightstring_bow",
            new LightstringBow(new FabricItemSettings().group(ItemGroup.COMBAT).maxDamage(752)));

    // Managold Gear
    public static final Item MANAGOLD_SWORD = registerItem("managold_sword",
            new SwordItem(ModToolMaterials.MANAGOLD, 3, -2.4F,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_PICKAXE = registerItem("managold_pickaxe",
            new ModPickaxeItem(ModToolMaterials.MANAGOLD, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item MANAGOLD_AXE = registerItem("managold_axe",
            new ModAxeItem(ModToolMaterials.MANAGOLD, 6.0F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item MANAGOLD_SHOVEL = registerItem("managold_shovel",
            new ModShovelItem(ModToolMaterials.MANAGOLD, 1.5F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item MANAGOLD_HOE = registerItem("managold_hoe",
            new ModHoeItem(ModToolMaterials.MANAGOLD, -2, 0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));

    public static final Item MANAGOLD_HELMET = registerItem("managold_helmet",
            new ArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_CHESTPLATE = registerItem("managold_chestplate",
            new ArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_LEGGINGS = registerItem("managold_leggings",
            new ArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_BOOTS = registerItem("managold_boots",
            new ArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));

    // Tormite Gear
    public static final Item TORMITE_SWORD = registerItem("tormite_sword",
            new SwordItem(ModToolMaterials.TORMITE, 3, -2.4F,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item TORMITE_PICKAXE = registerItem("tormite_pickaxe",
            new ModPickaxeItem(ModToolMaterials.TORMITE, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item TORMITE_AXE = registerItem("tormite_axe",
            new ModAxeItem(ModToolMaterials.TORMITE, 5.0F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item TORMITE_SHOVEL = registerItem("tormite_shovel",
            new ModShovelItem(ModToolMaterials.TORMITE, 1.5F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item TORMITE_HOE = registerItem("tormite_hoe",
            new ModHoeItem(ModToolMaterials.TORMITE, -4, 0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));

    public static final Item TORMITE_HELMET = registerItem("tormite_helmet",
            new ArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item TORMITE_CHESTPLATE = registerItem("tormite_chestplate",
            new ArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item TORMITE_LEGGINGS = registerItem("tormite_leggings",
            new ArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item TORMITE_BOOTS = registerItem("tormite_boots",
            new ArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));

    // Phantom Necklace
    public static final Item PHANTOM_NECKLACE = registerItem("phantom_necklace",
            new PhantomNecklace(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).rarity(Rarity.RARE)));




    // Excalibur
    public static final Item EXCALIBUR = registerItem("excalibur",
            new Excalibur(ModToolMaterials.ARTIFACT, 1, -2.6F,
            new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON)));

    // Slumbering Sword
    public static final Item SLUMBERING_SWORD = registerItem("slumbering_sword",
            new SwordItem(ModToolMaterials.CROWNED_EDGE, 3, -2.3F,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON).fireproof()));

    // Crowned Edge
    public static final Item CROWNED_EDGE = registerItem("crowned_edge",
            new CrownedEdgeItem(ModToolMaterials.CROWNED_EDGE, 3, -2.3F, 5,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.RARE).fireproof()));

    // True Crowned Edge (aka Epitome)
    public static final Item TRUE_CROWNED_EDGE = registerItem("true_crowned_edge",
            new TrueCrownedEdgeItem(ModToolMaterials.LAMENT, 6, -2.2F, 5,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC).fireproof()));



    // Eye reference Oneiros
    public static final Item DREAM_EYE = registerItem("dream_eye",
            new DreamEye(new FabricItemSettings().group(ItemGroup.MISC)));

    // Icy Needle
    public static final Item ICY_NEEDLE = registerItem("icy_needle",
            new IcyNeedle(new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item MATERIALIZE_TOME = registerItem("materialize_tome",
            new MaterializeTome(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.RARE)));

    // Tainted Pearl
    public static final Item TAINTED_PEARL = registerItem("tainted_pearl",
            new TaintedPearlItem(new FabricItemSettings().group(ItemGroup.MISC)));

    // Book reference Dreams
    public static final Item BOOK_OF_DREAMS = registerItem("book_of_dreams",
            new BookOfDreams(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));



    // Snowbell Seeds
    public static final Item SNOWBELL_SEEDS = registerItem("snowbell_seeds",
            new AliasedBlockItem(ModBlocks.SNOWBELL_CROP, new FabricItemSettings().group(ItemGroup.MISC)));

    // Water Artichoke Seeds
    public static final Item WATER_ARTICHOKE_SEEDS = registerItem("water_artichoke_seeds",
            new AliasedPlaceableOnWaterItem(ModBlocks.WATER_ARTICHOKE, new FabricItemSettings().group(ItemGroup.MISC)));



    // Attuned Amethyst
    public static final Item ATTUNED_SHARD = registerItem("attuned_shard",
            new AttunedShardItem(500, new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));
    public static final Item CHARGED_SHARD = registerItem("charged_shard",
            new ChargedShardItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));

    // Waystone
    public static final Item WAYSTONE = registerItem("waystone",
            new WaystoneItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(32)));

    // Tuning Shards
    public static final Item OVERWORLD_TUNER = registerItem("overworld_tuner",
            new TuningItem(AbstractRitual.CandleTuning.OVERWORLD,
                   new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(32)));

    public static final Item NETHER_TUNER = registerItem("nether_tuner",
            new TuningItem(AbstractRitual.CandleTuning.NETHER,
                    new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(32)));

    public static final Item END_TUNER = registerItem("end_tuner",
            new TuningItem(AbstractRitual.CandleTuning.END,
                    new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(32)));




    // Pearl reference Limitless Infusion
    public static final Item INFUSION_CHANGER = registerItem("infusion_changer",
            new InfusionChangerItem(new FabricItemSettings().group(ItemGroup.MISC)));


    
    // Staff reference Satiation
    public static final Item SATIATION_STAFF = registerItem("satiation_staff",
            new SatiationStaffItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));

    /*
     * Item Registration
     * */
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
    }

    public static void registerItems() {
        System.out.println("Registering items for " + MODID);
    }
}
