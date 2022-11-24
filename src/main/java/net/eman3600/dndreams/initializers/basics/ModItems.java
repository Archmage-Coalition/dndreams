package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.entities.projectiles.BrewLingeringEntity;
import net.eman3600.dndreams.entities.projectiles.BrewSplashEntity;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.items.*;
import net.eman3600.dndreams.items.block_item.AliasedDreamyBlockItem;
import net.eman3600.dndreams.items.block_item.AliasedPlaceableOnWaterItem;
import net.eman3600.dndreams.items.charge.AttunedShardItem;
import net.eman3600.dndreams.items.charge.ChargedShardItem;
import net.eman3600.dndreams.items.charge.TuningItem;
import net.eman3600.dndreams.items.consumable.*;
import net.eman3600.dndreams.items.consumable.brew.BrewIngestedItem;
import net.eman3600.dndreams.items.consumable.brew.BrewThrownItem;
import net.eman3600.dndreams.items.consumable.permanent.AttributePermItem;
import net.eman3600.dndreams.items.consumable.permanent.ManifestBrewItem;
import net.eman3600.dndreams.items.consumable.permanent.PanaceaItem;
import net.eman3600.dndreams.items.creative.InfusionPearlItem;
import net.eman3600.dndreams.items.edge_series.CrownedEdgeItem;
import net.eman3600.dndreams.items.edge_series.SlumberingSwordItem;
import net.eman3600.dndreams.items.edge_series.TrueCrownedEdgeItem;
import net.eman3600.dndreams.items.hellsteel.*;
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
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
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

    // Stardust
    public static final Item STARDUST = registerItem("stardust",
            new StardustItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Refined Evil
    public static final Item REFINED_EVIL = registerItem("refined_evil",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Crystal Mix
    public static final Item CRYSTAL_MIX = registerItem("crystal_mix",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Shade Veil
    public static final Item SHADE_CLOTH = registerItem("shade_cloth", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Ravaged Flesh
    public static final Item RAVAGED_FLESH = registerItem("ravaged_flesh", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)
            .food(ModFoodComponents.RAVAGED_FLESH)));

    // World Flow
    public static final Item LIQUID_VOID = registerItem("liquid_void",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.BREWING).recipeRemainder(Items.GLASS_BOTTLE).rarity(Rarity.UNCOMMON),
                    false, new StatusEffectInstance(ModStatusEffects.VOID_FLOW, 200)));

    // Liquid Soul
    public static final Item LIQUID_SOUL = registerItem("liquid_soul",
            new LiquidSoulItem(new FabricItemSettings().group(ItemGroup.BREWING).recipeRemainder(Items.GLASS_BOTTLE).rarity(Rarity.RARE)));

    // Nightmare Fuel
    public static final Item NIGHTMARE_FUEL = registerItem("nightmare_fuel",
            new NightmareFuelItem(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.NIGHTMARE_FUEL).fireproof()));

    // Starfruit
    public static final Item STAR_FRUIT = registerItem("star_fruit",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.STAR_FRUIT)));

    // End Sticks
    public static final Item END_STICK = registerItem("end_stick", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Gemstones
    public static final Item VITAL_SHARD = registerItem("vital_shard",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item CELESTIUM = registerItem("celestium",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item PERICHARITE = registerItem("pericharite",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Infernal Residue
    public static final Item INFERNAL_RESIDUE = registerItem("infernal_residue", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Archfuel
    public static final Item ARCHFUEL = registerItem("archfuel", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Mutandis
    public static final Item MUTANDIS = registerItem("mutandis", new MutandisItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item MUTANDIS_EXTREMIS = registerItem("mutandis_extremis", new MutandisExtremisItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item MUTANDIS_ONEIROS = registerItem("mutandis_oneiros", new MutandisOneirosItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Wood Ash
    public static final Item WOOD_ASH = registerItem("wood_ash", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Amethyst Jars, Fumes, & Essences
    public static final Item AMETHYST_JAR = registerItem("amethyst_jar", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item FOUL_FUME = registerItem("foul_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item SAKURA_FUME = registerItem("sakura_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item DREAMWOOD_FUME = registerItem("dreamwood_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item SCULK_WOOD_FUME = registerItem("sculk_wood_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item JAPANESE_MAPLE_FUME = registerItem("japanese_maple_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item SELENE_FUME = registerItem("selene_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item HELIOS_FUME = registerItem("helios_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item EOS_FUME = registerItem("eos_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item STAR_FUME = registerItem("star_fume", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));

    public static final Item DROP_OF_LUCK = registerItem("drop_of_luck", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item MELLIFLUOUS_HUNGER = registerItem("mellifluous_hunger", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item DISTILLED_SPIRIT = registerItem("distilled_spirit", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item HOLLOW_TEAR = registerItem("hollow_tear", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));

    public static final Item ENTANGLED_EARTH = registerItem("entangled_earth", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item IMPRISONED_FEAR = registerItem("imprisoned_fear", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item STOLEN_WIT = registerItem("stolen_wit", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item RESTRAINED_LIGHT = registerItem("restrained_light", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));
    public static final Item WITHHELD_AGONY = registerItem("withheld_agony", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(AMETHYST_JAR)));

    // Lotus & Wither Bud
    public static final Item LOTUS_FLOWER = registerItem("lotus_flower",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).food(ModFoodComponents.LOTUS_FLOWER)));

    public static final Item WITHER_BUD = registerItem("wither_bud",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).food(ModFoodComponents.WITHER_BUD)));

    // Bottled Stuff
    public static final Item VITAL_OIL = registerItem("vital_oil",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(Items.GLASS_BOTTLE),
                    false, new StatusEffectInstance(StatusEffects.REGENERATION, 400), new StatusEffectInstance(StatusEffects.INSTANT_HEALTH,1, 1)));
    public static final Item SOUL_SOUP = registerItem("soul_soup",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(Items.GLASS_BOTTLE),
                    true, new StatusEffectInstance(ModStatusEffects.LIFEMANA, 600), new StatusEffectInstance(ModStatusEffects.AFFLICTION, 1800)));
    public static final Item NATURES_GOSPEL = registerItem("natures_gospel",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(Items.GLASS_BOTTLE),
                    true, new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 7)));
    public static final Item INFERNAL_ANIMUS = registerItem("infernal_animus",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(Items.GLASS_BOTTLE),
                    true, new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 7)));
    public static final Item TRACE_OF_OTHERWHERE = registerItem("trace_of_otherwhere",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(Items.GLASS_BOTTLE),
                    true, new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 7)));
    public static final Item GHOST_OF_LIGHT = registerItem("ghost_of_light",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(Items.GLASS_BOTTLE),
                    true, new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 7)));

    // Ingots
    public static final Item CORRUPT_SCRAP = registerItem("corrupt_scrap",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item CORRUPT_INGOT = registerItem("corrupt_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item RAW_MANAGOLD = registerItem("raw_managold",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
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
            new CorruptHoe(ModToolMaterials.CORRUPT, -2, 0F,
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
            new ExcaliburItem(ModToolMaterials.ARTIFACT, 1, -2.6F,
            new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON)));

    // Vital Sword
    public static final Item VITAL_SWORD = registerItem("vital_sword",
            new VitalSwordItem(ModToolMaterials.VITAL, 3, -2.4F,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON)));
    public static final Item BROKEN_VITAL_SWORD = registerItem("broken_vital_sword",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).maxCount(1).rarity(Rarity.UNCOMMON)));

    // Shattered Sword
    public static final Item SHATTERED_SWORD = registerItem("shattered_sword",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON).maxCount(1).fireproof()));

    // Slumbering Sword
    public static final Item SLUMBERING_SWORD = registerItem("slumbering_sword",
            new SlumberingSwordItem(ModToolMaterials.CROWNED_EDGE, 3, -2.3F,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.UNCOMMON).fireproof()));

    // Crowned Edge
    public static final Item CROWNED_EDGE = registerItem("crowned_edge",
            new CrownedEdgeItem(ModToolMaterials.CROWNED_EDGE, 3, -2.3F, 5,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.RARE).fireproof()));

    // True Crowned Edge (aka Epitome)
    public static final Item TRUE_CROWNED_EDGE = registerItem("true_crowned_edge",
            new TrueCrownedEdgeItem(ModToolMaterials.LAMENT, 6, -2.2F, 5,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC).fireproof()));



    // Brews of Lofty Dreams, Congealed Mind, & the Panacea
    public static final Item SLEEPING_BREW = registerItem("sleeping_brew",
            new SleepingBrewItem(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item MANIFEST_BREW = registerItem("manifest_brew",
            new ManifestBrewItem(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.EPIC).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item PANACEA = registerItem("panacea",
            new PanaceaItem(new FabricItemSettings().group(ItemGroup.MISC).rarity(Rarity.EPIC).recipeRemainder(Items.GLASS_BOTTLE)));

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

    // Book of Dreams
    public static final Item BOOK_OF_DREAMS = registerItem("book_of_dreams",
            new BookOfDreams(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));

    // Magic Apples
    public static final Item SUCCULENT_APPLE = registerItem("succulent_apple",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.SUCCULENT_APPLE)));

    public static final Item POISON_APPLE = registerItem("poison_apple",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.POISON_APPLE)));

    // Attribute Crystals
    public static final Item VITAL_HEART = registerItem("vital_heart",
            new AttributePermItem(new FabricItemSettings().group(ItemGroup.MISC),
                    5, EntityAttributes.GENERIC_MAX_HEALTH, 2d));

    public static final Item ECHO_STAR = registerItem("echo_star",
            new AttributePermItem(new FabricItemSettings().group(ItemGroup.MISC),
                    5, ModAttributes.PLAYER_MAX_MANA, 5d));



    // Snowbell Seeds
    public static final Item SNOWBELL_SEEDS = registerItem("snowbell_seeds",
            new AliasedBlockItem(ModBlocks.SNOWBELL, new FabricItemSettings().group(ItemGroup.MISC)));

    // Water Artichoke Seeds
    public static final Item LOTUS_SEEDS = registerItem("lotus_seeds",
            new AliasedPlaceableOnWaterItem(ModBlocks.LOTUS, new FabricItemSettings().group(ItemGroup.MISC)));

    // Applethorn Seeds
    public static final Item APPLETHORN_SEEDS = registerItem("applethorn_seeds",
            new AliasedDreamyBlockItem(ModBlocks.APPLETHORN, new FabricItemSettings().group(ItemGroup.MISC)));

    // Wither Blossom Seeds
    public static final Item WITHER_BLOSSOM_SEEDS = registerItem("wither_blossom_seeds",
            new AliasedBlockItem(ModBlocks.WITHER_BLOSSOM, new FabricItemSettings().group(ItemGroup.MISC)));



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


    
    // Staff of Satiation
    public static final Item SATIATION_STAFF = registerItem("satiation_staff",
            new SatiationStaffItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));



    // Brews
    public static final Item BREW_INGESTED = registerItem("brew_ingested",
            new BrewIngestedItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16)));
    public static final Item BREW_SPLASH = registerItem("brew_splash",
            new BrewThrownItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16), BrewSplashEntity::new));
    public static final Item BREW_LINGERING = registerItem("brew_lingering",
            new BrewThrownItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16), BrewLingeringEntity::new));




    // CREATIVE ONLY
    public static final Item INFUSION_CHANGER = registerItem("infusion_changer",
            new InfusionPearlItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));

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
