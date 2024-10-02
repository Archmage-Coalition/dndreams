package net.eman3600.dndreams.initializers.basics;

import net.eman3600.dndreams.entities.projectiles.BrewGasEntity;
import net.eman3600.dndreams.entities.projectiles.BrewLingeringEntity;
import net.eman3600.dndreams.entities.projectiles.BrewLiquidEntity;
import net.eman3600.dndreams.entities.projectiles.BrewSplashEntity;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.event.ModSoundEvents;
import net.eman3600.dndreams.items.*;
import net.eman3600.dndreams.items.block_item.AliasedPlaceableOnWaterItem;
import net.eman3600.dndreams.items.celestium.*;
import net.eman3600.dndreams.items.charge.AttunedShardItem;
import net.eman3600.dndreams.items.charge.ChargedShardItem;
import net.eman3600.dndreams.items.cloud.CloudArmorItem;
import net.eman3600.dndreams.items.cloud.CloudPickaxeItem;
import net.eman3600.dndreams.items.cloud.SkyboundArrowItem;
import net.eman3600.dndreams.items.consumable.*;
import net.eman3600.dndreams.items.consumable.brew.BrewIngestedItem;
import net.eman3600.dndreams.items.consumable.brew.BrewThrownItem;
import net.eman3600.dndreams.items.consumable.permanent.AttributePermItem;
import net.eman3600.dndreams.items.consumable.permanent.CrystalFeatherItem;
import net.eman3600.dndreams.items.consumable.permanent.FidiFruitItem;
import net.eman3600.dndreams.items.consumable.permanent.ManifestBrewItem;
import net.eman3600.dndreams.items.dreadful.DreadfulArrowItem;
import net.eman3600.dndreams.items.dreadful.GhostArrowItem;
import net.eman3600.dndreams.items.dreadful.StrifeItem;
import net.eman3600.dndreams.items.edge_series.CrownedEdgeItem;
import net.eman3600.dndreams.items.edge_series.TrueCrownedEdgeItem;
import net.eman3600.dndreams.items.hellsteel.*;
import net.eman3600.dndreams.items.magic_bow.*;
import net.eman3600.dndreams.items.managold.ManagoldArmorItem;
import net.eman3600.dndreams.items.managold.ManagoldArrowItem;
import net.eman3600.dndreams.items.misc_armor.CloudWingsItem;
import net.eman3600.dndreams.items.misc_armor.EvergaleItem;
import net.eman3600.dndreams.items.misc_tool.*;
import net.eman3600.dndreams.items.pericharite.PerichariteArmorItem;
import net.eman3600.dndreams.items.pericharite.PerichariteShovelItem;
import net.eman3600.dndreams.items.pericharite.PerichariteSwordItem;
import net.eman3600.dndreams.items.staff.*;
import net.eman3600.dndreams.items.tool_mirror.ModAxeItem;
import net.eman3600.dndreams.items.tool_mirror.ModHoeItem;
import net.eman3600.dndreams.items.tool_mirror.ModPickaxeItem;
import net.eman3600.dndreams.items.tool_mirror.ModShovelItem;
import net.eman3600.dndreams.items.tormite.TormiteArmorItem;
import net.eman3600.dndreams.items.trinket.*;
import net.eman3600.dndreams.util.ModArmorMaterials;
import net.eman3600.dndreams.util.ModFoodComponents;
import net.eman3600.dndreams.util.ModToolMaterials;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
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
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Sculk Powder
    public static final Item SCULK_POWDER = registerItem("sculk_powder",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Stardust
    public static final Item STARDUST = registerItem("stardust",
            new StardustItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Thread of Sanity
    public static final Item SANITY_THREAD = registerItem("sanity_thread",
            new SanityThreadItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Crystal Mix
    public static final Item CRYSTAL_MIX = registerItem("crystal_mix",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Horrific Veil
    public static final Item SHADE_CLOTH = registerItem("shade_cloth", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Pure Brilliance
    public static final Item BRILLIANCE = registerItem("brilliance", new MarbleSpreadItem(new FabricItemSettings().group(ItemGroup.MATERIALS)).withTooltip(null, 1));

    // Ravaged Flesh
    public static final Item RAVAGED_FLESH = registerItem("ravaged_flesh", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)
            .food(ModFoodComponents.RAVAGED_FLESH)));

    // Cosmic Fluid
    public static final Item LIQUID_VOID = registerItem("liquid_void",
            new LiquidVoidItem(new FabricItemSettings().group(ItemGroup.BREWING).recipeRemainder(Items.GLASS_BOTTLE).rarity(Rarity.UNCOMMON)));

    // Congealed Soul
    public static final Item SOUL = registerItem("soul",
            new SoulItem(new FabricItemSettings().group(ItemGroup.BREWING).rarity(Rarity.RARE)).withTooltip(null, 1));

    // Nightmare Fuel
    public static final Item NIGHTMARE_FUEL = registerItem("nightmare_fuel",
            new NightmareFuelItem(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.NIGHTMARE_FUEL).fireproof()).withTooltip(null, 2));

    // Subdued Madness
    public static final Item SUBDUED_MADNESS = registerItem("subdued_madness",
            new TooltipItem(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()).withTooltip(null, 2));

    // Starfruit
    public static final Item STAR_FRUIT = registerItem("star_fruit",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.STAR_FRUIT)));

    // Special Chunks & Sticks
    public static final Item OBSIDIAN_CHUNK = registerItem("obsidian_chunk",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item OBSIDIAN_STICK = registerItem("obsidian_stick",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));

    public static final Item DRAGON_BONE = registerItem("dragon_bone",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    public static final Item END_CHUNK = registerItem("end_chunk", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item END_STICK = registerItem("end_stick", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    public static final Item SILVER_ROD = registerItem("silver_rod",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Gemstones
    public static final Item VITAL_SHARD = registerItem("vital_shard",
            new WitherCureItem(40, new FabricItemSettings().group(ItemGroup.MATERIALS)).withTooltip(null, 1));
    public static final Item CELESTIUM = registerItem("celestium",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item CRYSTAL_SPIRIT = registerItem("crystal_spirit",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item PERICHARITE = registerItem("pericharite",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Infernal Residue
    public static final Item INFERNAL_RESIDUE = registerItem("infernal_residue", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Nightmarrow
    public static final Item NIGHTMARROW = registerItem("nightmarrow", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Archfuel
    public static final Item ARCHFUEL = registerItem("archfuel", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Mutandis
    public static final Item MUTANDIS = registerItem("mutandis", new MutandisItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item MUTANDIS_EXTREMIS = registerItem("mutandis_extremis", new MutandisExtremisItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item MUTANDIS_ONEIROS = registerItem("mutandis_oneiros", new MutandisOneirosItem(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Wood Ash
    public static final Item WOOD_ASH = registerItem("wood_ash", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    public static final Item SAKURA_PETALS = registerItem("sakura_petals", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item RAW_FROG = registerItem("raw_frog", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.RAW_FROG)));
    public static final Item COOKED_FROG = registerItem("cooked_frog", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.COOKED_FROG)));
    public static final Item CLOUD = registerItem("cloud", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item GOLD_FRUIT = registerItem("gold_fruit", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.GOLD_FRUIT)));
    public static final Item CAKE_APPLE = registerItem("cake_apple", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.CAKE_APPLE)));
    public static final Item LOST_DREAM = registerItem("lost_dream", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)));
    public static final Item PERMAFROST = registerItem("permafrost", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item FULGAR_BUD = registerItem("fulgar_bud", new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));


    // Lotus, Wither Bud, & Dragonfruit
    public static final Item LOTUS_FLOWER = registerItem("lotus_flower",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.LOTUS_FLOWER)));

    public static final Item WITHER_BUD = registerItem("wither_bud",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.WITHER_BUD)));

    public static final Item DRAGONFRUIT = registerItem("dragonfruit",
            new DragonfruitItem(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.DRAGONFRUIT))
                    .withTooltip(null, 2));


    // Bottled Stuff
    public static final Item VITAL_OIL = registerItem("vital_oil",
            new DrinkableItem(new FabricItemSettings().group(ItemGroup.MATERIALS).recipeRemainder(Items.GLASS_BOTTLE),
                    false, new StatusEffectInstance(StatusEffects.REGENERATION, 1200), new StatusEffectInstance(StatusEffects.INSTANT_HEALTH,1, 1), new StatusEffectInstance(ModStatusEffects.REJUVENATION,1200, 1)));

    // Ingots
    public static final Item CORRUPT_SCRAP = registerItem("corrupt_scrap",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item CORRUPT_INGOT = registerItem("corrupt_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item RAW_MANAGOLD = registerItem("raw_managold",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item MANAGOLD_INGOT = registerItem("managold_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item MANAGOLD_NUGGET = registerItem("managold_nugget",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item TORMITE_INGOT = registerItem("tormite_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).fireproof()));
    public static final Item RAW_SILVER = registerItem("raw_silver",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item SILVER_INGOT = registerItem("silver_ingot",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
    public static final Item SILVER_NUGGET = registerItem("silver_nugget",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));

    // Obsidian Vessel
    public static final Item DEMONIC_CORE = registerItem("demonic_core",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).maxCount(1).fireproof()));
    public static final Item DEMONIC_CORE_CHARGED = registerItem("demonic_core_charged",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).maxCount(1).fireproof()));


    // Managold Gear
    public static final Item MANAGOLD_SWORD = registerItem("managold_sword",
            new SwordItem(ModToolMaterials.MANAGOLD, 3, -2.2F,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_PICKAXE = registerItem("managold_pickaxe",
            new ModPickaxeItem(ModToolMaterials.MANAGOLD, 1, -2.6F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item MANAGOLD_AXE = registerItem("managold_axe",
            new ModAxeItem(ModToolMaterials.MANAGOLD, 5.0F, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item MANAGOLD_SHOVEL = registerItem("managold_shovel",
            new ModShovelItem(ModToolMaterials.MANAGOLD, 1.5F, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item MANAGOLD_HOE = registerItem("managold_hoe",
            new ModHoeItem(ModToolMaterials.MANAGOLD, -2, 0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));

    public static final Item MANAGOLD_HELMET = registerItem("managold_helmet",
            new ManagoldArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_CHESTPLATE = registerItem("managold_chestplate",
            new ManagoldArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_LEGGINGS = registerItem("managold_leggings",
            new ManagoldArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MANAGOLD_BOOTS = registerItem("managold_boots",
            new ManagoldArmorItem(ModArmorMaterials.MANAGOLD, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));


    // Resonant Pickaxe
    public static final Item RESONANT_PICKAXE = registerItem("resonant_pickaxe",
            new ResonantPickaxeItem(ModToolMaterials.RESONANT, 1, -2.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));


    // Hellsteel Sword & Tools
    public static final Item CORRUPT_SWORD = registerItem("corrupt_sword",
            new CorruptSword(ModToolMaterials.CORRUPT, 3, -2.4F,
            new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item CORRUPT_PICKAXE = registerItem("corrupt_pickaxe",
            new CorruptPickaxe(ModToolMaterials.CORRUPT, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_AXE = registerItem("corrupt_axe",
            new CorruptAxe(ModToolMaterials.CORRUPT, 5.0F, -3.1F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_SHOVEL = registerItem("corrupt_shovel",
            new CorruptShovel(ModToolMaterials.CORRUPT, 1.5F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));
    public static final Item CORRUPT_HOE = registerItem("corrupt_hoe",
            new CorruptHoe(ModToolMaterials.CORRUPT, -2, 0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));

    public static final Item CORRUPT_HELMET = registerItem("corrupt_helmet",
            new CorruptArmorItem(ModArmorMaterials.CORRUPT, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CORRUPT_CHESTPLATE = registerItem("corrupt_chestplate",
            new CorruptArmorItem(ModArmorMaterials.CORRUPT, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CORRUPT_LEGGINGS = registerItem("corrupt_leggings",
            new CorruptArmorItem(ModArmorMaterials.CORRUPT, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CORRUPT_BOOTS = registerItem("corrupt_boots",
            new CorruptArmorItem(ModArmorMaterials.CORRUPT, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));

    // Bloodflame Catalyst
    public static final Item BLOODFLAME_VESSEL = registerItem("bloodflame_vessel", new BloodflameVessel(new FabricItemSettings()
            .group(ItemGroup.TOOLS).fireproof().maxCount(1)));

    // Mind Shears
    public static final Item MIND_SHEARS = registerItem("mind_shears", new MindShearsItem(
            new FabricItemSettings().group(ItemGroup.TOOLS).fireproof().maxCount(1).maxDamage(256)).withTooltip(null, 1));

    // Ancient Portal Activation
    public static final Item RADIANT_KEY = registerItem("radiant_key", new RadiantKeyItem(new FabricItemSettings()
            .group(ItemGroup.TOOLS).maxCount(1).rarity(Rarity.UNCOMMON)).withTooltip(null, 1));

    // Veiled Key
    public static final Item VEILED_KEY = registerItem("veiled_key", new TooltipItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)).withTooltip(null, 1));


    // Celestium Swords & Tools
    public static final Item CELESTIUM_SWORD = registerItem("celestium_sword",
            new CelestiumSwordItem(ModToolMaterials.CELESTIUM, 3, -2.4F,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CELESTIUM_PICKAXE = registerItem("celestium_pickaxe",
            new CelestiumPickaxeItem(ModToolMaterials.CELESTIUM_SLOW, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item CELESTIUM_AXE = registerItem("celestium_axe",
            new CelestiumAxeItem(ModToolMaterials.CELESTIUM, 5.0F, -3F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));

    public static final Item CELESTIUM_SHOVEL = registerItem("celestium_shovel",
            new CelestiumShovelItem(ModToolMaterials.CELESTIUM, 1.5F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item CELESTIUM_HOE = registerItem("celestium_hoe",
            new ScytheItem(ModToolMaterials.CELESTIUM, 6, -3.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));

    public static final Item CELESTIUM_HELMET = registerItem("celestium_helmet",
            new CelestiumArmorItem(ModArmorMaterials.CELESTIUM, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CELESTIUM_CHESTPLATE = registerItem("celestium_chestplate",
            new CelestiumArmorItem(ModArmorMaterials.CELESTIUM, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CELESTIUM_LEGGINGS = registerItem("celestium_leggings",
            new CelestiumArmorItem(ModArmorMaterials.CELESTIUM, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CELESTIUM_BOOTS = registerItem("celestium_boots",
            new CelestiumArmorItem(ModArmorMaterials.CELESTIUM, EquipmentSlot.FEET,
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
            new ScytheItem(ModToolMaterials.TORMITE, 6, -3.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS).fireproof()));

    public static final Item TORMITE_HELMET = registerItem("tormite_helmet",
            new TormiteArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item TORMITE_CHESTPLATE = registerItem("tormite_chestplate",
            new TormiteArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item TORMITE_LEGGINGS = registerItem("tormite_leggings",
            new TormiteArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));
    public static final Item TORMITE_BOOTS = registerItem("tormite_boots",
            new TormiteArmorItem(ModArmorMaterials.TORMITE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));

    // Pericharite
    public static final Item PERICHARITE_SWORD = registerItem("pericharite_sword",
            new PerichariteSwordItem(ModToolMaterials.PERICHARITE, 3, -2.4F,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item PERICHARITE_PICKAXE = registerItem("pericharite_pickaxe",
            new ModPickaxeItem(ModToolMaterials.PERICHARITE, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item PERICHARITE_AXE = registerItem("pericharite_axe",
            new ModAxeItem(ModToolMaterials.PERICHARITE, 5.0F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));

    public static final Item PERICHARITE_SHOVEL = registerItem("pericharite_shovel",
            new PerichariteShovelItem(ModToolMaterials.PERICHARITE, 1.5F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item PERICHARITE_HOE = registerItem("pericharite_hoe",
            new ScytheItem(ModToolMaterials.PERICHARITE, 6, -3.4F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));

    public static final Item PERICHARITE_HELMET = registerItem("pericharite_helmet",
            new PerichariteArmorItem(ModArmorMaterials.PERICHARITE, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item PERICHARITE_CHESTPLATE = registerItem("pericharite_chestplate",
            new PerichariteArmorItem(ModArmorMaterials.PERICHARITE, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item PERICHARITE_LEGGINGS = registerItem("pericharite_leggings",
            new PerichariteArmorItem(ModArmorMaterials.PERICHARITE, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item PERICHARITE_BOOTS = registerItem("pericharite_boots",
            new PerichariteArmorItem(ModArmorMaterials.PERICHARITE, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));

    // Magic Bows
    public static final Item MANASTRING_BOW = registerItem("manastring_bow",
            new ManastringBowItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxDamage(752)));
    public static final Item MINDSTRING_BOW = registerItem("mindstring_bow",
            new MindstringBowItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxDamage(2145)));
    public static final Item BLOODY_CARBINE = registerItem("bloody_carbine",
            new BloodyCarbineItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxDamage(735)).withTooltip(null, 1).withTooltip("tooltip.dndreams.sacrifice", 1));
    public static final Item STRIFE = registerItem("strife",
            new StrifeItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxDamage(735)).withTooltip(null, 1).withTooltip("tooltip.dndreams.sacrifice", 1));

    // Magic Arrows & Quivers
    public static final Item MANAGOLD_ARROW = registerItem("managold_arrow",
            new ManagoldArrowItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item DREADFUL_ARROW = registerItem("dreadful_arrow",
            new DreadfulArrowItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item GHOST_ARROW = registerItem("ghost_arrow",
            new GhostArrowItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item SKYBOUND_ARROW = registerItem("skybound_arrow",
            new SkyboundArrowItem(new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item STORM_ARROW = registerItem("storm_arrow",
            new StormArrowItem(new FabricItemSettings().group(ItemGroup.COMBAT)));

    public static final Item MAGIC_QUIVER = registerItem("magic_quiver",
            new MagicQuiverItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)).withTooltip(null, 1));
    public static final Item SPECTRAL_QUIVER = registerItem("spectral_quiver",
            new SpectralQuiverItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)).withTooltip(null, 1));
    public static final Item MANAGOLD_QUIVER = registerItem("managold_quiver",
            new ManagoldQuiverItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)).withTooltip(null, 1));
    public static final Item DREADFUL_QUIVER = registerItem("dreadful_quiver",
            new DreadfulQuiverItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)).withTooltip(null, 1));
    public static final Item GHOST_QUIVER = registerItem("ghost_quiver",
            new GhostQuiverItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)).withTooltip(null, 2));
    public static final Item SKYBOUND_QUIVER = registerItem("skybound_quiver",
            new SkyboundQuiverItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)).withTooltip(null, 2));
    public static final Item STORM_QUIVER = registerItem("storm_quiver",
            new StormQuiverItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)).withTooltip(null, 1));

    // Trinkets
    public static final Item FLEETFOOT_BAND = registerItem("fleetfoot_band",
            new FleetfootBandItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1)));
    public static final Item DRAGONFOOT_BAND = registerItem("dragonfoot_band",
            new DragonfootBandItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1)));
    public static final Item DISSOCIATION_CHARM = registerItem("dissociation_charm",
            new TooltipTrinketItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1)));
    public static final Item LIFE_GIVING_AMULET = registerItem("life_giving_amulet",
            new LifeGivingAmuletItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));
    public static final Item MAD_SANITY_CHARM = registerItem("mad_sanity_charm",
            new TooltipTrinketItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));
    public static final Item ROSE_GLASSES = registerItem("rose_glasses",
            new BasicTrinketItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));
    public static final Item TRUTH_GLASSES = registerItem("truth_glasses",
            new TruthGlassesItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));
    public static final Item FEARLESS_SHADES = registerItem("fearless_shades",
            new FearlessShadesItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));
    public static final Item FLAME_CAPE = registerItem("flame_cape",
            new FlameCapeItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).fireproof()));
    public static final Item SUBSTANCE_CLOAK = registerItem("substance_cloak",
            new BasicTrinketItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1)));
    public static final Item REVIVE_CLOAK = registerItem("revive_cloak",
            new RevivalCloakItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1)));
    public static final Item WATER_STRIDERS = registerItem("water_striders",
            new BasicTrinketItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1)));
    public static final Item LAVA_STRIDERS = registerItem("lava_striders",
            new BasicTrinketItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).fireproof()));
    public static final Item SKYSTEP_SOCKS = registerItem("skystep_socks",
            new AirJumpItem(3, new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1)));

    // Unique Armors
    public static final Item CLOUD_WINGS = registerItem("cloud_wings",
            new CloudWingsItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxDamage(216)).withTooltip(null, 1));
    public static final Item EVERGALE = registerItem("evergale",
            new EvergaleItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxDamage(1561)).withTooltip(null, 2));

    // Staves
    public static final Item SPARK_STAFF = registerItem("spark_staff",
            new SparkStaffItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(1561))
                    .withTooltip(null, 1));
    public static final Item SNAP_STAFF = registerItem("snap_staff",
            new SnapStaffItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(735))
                    .withTooltip(null, 1));
    public static final Item LIGHT_STAFF = registerItem("light_staff",
            new LightStaffItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(1299))
                    .withTooltip(null, 2));
    public static final Item SEAR_STAFF = registerItem("sear_staff",
            new SearStaffItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(1299).fireproof())
                    .withTooltip(null, 1));
    public static final Item ENDER_STAFF = registerItem("ender_staff",
            new EnderStaffItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).maxDamage(735))
                    .withTooltip(null, 1));
    public static final Item TELEPORT_STAFF = registerItem("teleport_staff",
            new TeleportStaffItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).maxDamage(2173))
                    .withTooltip(null, 2));
    public static final Item FORCE_STAFF = registerItem("force_staff",
            new ForceStaffItem(new FabricItemSettings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(2173))
                    .withTooltip(null, 2));
    public static final Item LAUNCH_STAFF = registerItem("launch_staff",
            new LaunchStaffItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).maxDamage(2173))
                    .withTooltip(null, 1));
    public static final Item WINDWEAVE_STAFF = registerItem("windweave_staff",
            new TooltipItem(new FabricItemSettings().group(ItemGroup.TRANSPORTATION).maxCount(1).maxDamage(735))
                    .withTooltip(null, 1));
    public static final Item SATIATION_STAFF = registerItem("satiation_staff",
            new SatiationStaffItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(735))
                    .withTooltip(null, 1));
    public static final Item ASCEND_GRIP = registerItem("ascend_grip",
            new AscendItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(735))
                    .withTooltip(null, 1));




    // Shattered Sword
    public static final Item SHATTERED_SWORD = registerItem("shattered_sword",
            new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON).maxCount(1).fireproof()));

    // Crowned Edge
    public static final Item CROWNED_EDGE = registerItem("crowned_edge",
            new CrownedEdgeItem(ModToolMaterials.CROWNED_EDGE, 3, -2.3F, 6,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.RARE).fireproof()));

    // True Crowned Edge
    public static final Item TRUE_CROWNED_EDGE = registerItem("true_crowned_edge",
            new TrueCrownedEdgeItem(ModToolMaterials.TRUE_EDGE, 5, -2.2F, 8,
                    new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC).fireproof()));

    // The Atlas of Frosyni
    public static final Item ATLAS = registerItem("atlas",
            new AtlasItem(new FabricItemSettings().group(ItemGroup.TOOLS).rarity(Rarity.EPIC).fireproof().maxCount(1)));



    // Special Brews
    public static final Item SLEEPING_BREW = registerItem("sleeping_brew",
            new SleepingBrewItem(new FabricItemSettings().group(ItemGroup.BREWING).rarity(Rarity.UNCOMMON).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item SANITY_BREW = registerItem("sanity_brew",
            new SanityBrewItem(new FabricItemSettings().group(ItemGroup.BREWING).rarity(Rarity.UNCOMMON).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item MANIFEST_BREW = registerItem("manifest_brew",
            new ManifestBrewItem(new FabricItemSettings().group(ItemGroup.BREWING).rarity(Rarity.EPIC).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item DREAM_EYE = registerItem("dream_eye",
            new DreamEye(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)));

    // Icy Needle
    public static final Item ICY_NEEDLE = registerItem("icy_needle",
            new IcyNeedle(new FabricItemSettings().group(ItemGroup.MISC)));

    // Sanity Salve
    public static final Item SANITY_SALVE = registerItem("sanity_salve",
            new SanitySalveItem(new FabricItemSettings().group(ItemGroup.TOOLS)).withTooltip(null, 2));

    // Tainted Pearl
    public static final Item TAINTED_PEARL = registerItem("tainted_pearl",
            new TaintedPearlItem(new FabricItemSettings().group(ItemGroup.MISC)));

    // Spark Powder
    public static final Item FLAME_POWDER = registerItem("flame_powder",
            new FlamePowderItem(new FabricItemSettings().group(ItemGroup.TOOLS)).withTooltip(null, 2));

    // Codex Memorium
    public static final Item BOOK_OF_DREAMS = registerItem("book_of_dreams",
            new CodexItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1)).withTooltip(null, 2));

    // Magic Apples
    public static final Item SUCCULENT_APPLE = registerItem("succulent_apple",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.SUCCULENT_APPLE)));

    public static final Item POISON_APPLE = registerItem("poison_apple",
            new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.POISON_APPLE)));

    // Attribute Crystals
    public static final Item VITAL_HEART = registerItem("vital_heart",
            new AttributePermItem(new FabricItemSettings().group(ItemGroup.TOOLS),
                    5, EntityAttributes.GENERIC_MAX_HEALTH, 2d));

    public static final Item ECHO_STAR = registerItem("echo_star",
            new AttributePermItem(new FabricItemSettings().group(ItemGroup.TOOLS),
                    1, ModAttributes.PLAYER_MAX_MANA, 25d));

    public static final Item BLAZE_SPIRIT = registerItem("blaze_spirit",
            new AttributePermItem(new FabricItemSettings().group(ItemGroup.TOOLS),
                    1, ModAttributes.PLAYER_MANA_REGEN, 2d));

    public static final Item CELESTIUM_HEART = registerItem("celestium_heart",
            new AttributePermItem(new FabricItemSettings().group(ItemGroup.TOOLS),
                    1, EntityAttributes.GENERIC_MAX_HEALTH, 10d));

    // Crystal Feather
    public static final Item CRYSTAL_FEATHER = registerItem("crystal_feather",
            new CrystalFeatherItem(new FabricItemSettings().group(ItemGroup.TOOLS)));

    // Fruit of Fidi
    public static final Item FIDI_FRUIT = registerItem("fidi_fruit",
            new FidiFruitItem(new FabricItemSettings().group(ItemGroup.FOOD).food(ModFoodComponents.DRAGONFRUIT)));


    // Essences
    public static final Item ESSENCE_IMAGINATION = registerItem("essence_imagination",
            new EssenceItem(1, new FabricItemSettings().group(ItemGroup.TOOLS)).withTooltip(null, 1));



    // Snowbell Seeds
    public static final Item SNOWBELL_SEEDS = registerItem("snowbell_seeds",
            new AliasedBlockItem(ModBlocks.SNOWBELL, new FabricItemSettings().group(ItemGroup.MISC)));

    // Water Artichoke Seeds
    public static final Item LOTUS_SEEDS = registerItem("lotus_seeds",
            new AliasedPlaceableOnWaterItem(ModBlocks.LOTUS, new FabricItemSettings().group(ItemGroup.MISC)));

    // Applethorn Seeds
    public static final Item APPLETHORN_SEEDS = registerItem("applethorn_seeds",
            new AliasedBlockItem(ModBlocks.APPLETHORN, new FabricItemSettings().group(ItemGroup.MISC)));

    // Wither Blossom Seeds
    public static final Item WITHER_BLOSSOM_SEEDS = registerItem("wither_blossom_seeds",
            new AliasedBlockItem(ModBlocks.WITHER_BLOSSOM, new FabricItemSettings().group(ItemGroup.MISC)));

    // Dragonfruit Seeds
    public static final Item DRAGONFRUIT_SEEDS = registerItem("dragonfruit_seeds",
            new AliasedBlockItem(ModBlocks.DRAGONFRUIT, new FabricItemSettings().group(ItemGroup.MISC)));



    // Attuned Amethyst
    public static final Item ATTUNED_SHARD = registerItem("attuned_shard",
            new AttunedShardItem(500, new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));
    public static final Item CHARGED_SHARD = registerItem("charged_shard",
            new ChargedShardItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));




    // Mystic Staff
    public static final Item MYSTIC_STAFF = registerItem("mystic_staff",
            new MysticStaffItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(69)));



    // Brews
    public static final Item BREW_INGESTED = registerItem("brew_ingested",
            new BrewIngestedItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16)));
    public static final Item BREW_SPLASH = registerItem("brew_splash",
            new BrewThrownItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16), .8f, BrewSplashEntity::new));
    public static final Item BREW_LINGERING = registerItem("brew_lingering",
            new BrewThrownItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16), .35f, BrewLingeringEntity::new));
    public static final Item BREW_GAS = registerItem("brew_gas",
            new BrewThrownItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16), .15f, BrewGasEntity::new));
    public static final Item BREW_LIQUID = registerItem("brew_liquid",
            new BrewThrownItem(new FabricItemSettings().group(ItemGroup.BREWING).maxCount(16), .25f, BrewLiquidEntity::new));

    // Spring Vials
    public static final Item WATER_VIAL = registerItem("water_vial",
            new SpringVialItem(Fluids.WATER, 3694022, new FabricItemSettings().group(ItemGroup.MISC).maxCount(16)));
    public static final Item LAVA_VIAL = registerItem("lava_vial",
            new SpringVialItem(Fluids.LAVA, 0xd65b11, new FabricItemSettings().group(ItemGroup.MISC).maxCount(16)));
    public static final Item SPIRIT_VIAL = registerItem("spirit_vial",
            new SpringVialItem(ModFluids.STILL_FLOWING_SPIRIT, 0x00F0F0, new FabricItemSettings().group(ItemGroup.MISC).maxCount(16)));
    public static final Item SORROW_VIAL = registerItem("sorrow_vial",
            new SpringVialItem(ModFluids.STILL_SORROW, 0x1E1C32, new FabricItemSettings().group(ItemGroup.MISC).maxCount(16)));

    // Cloud stuff
    public static final Item CLOUD_SWORD = registerItem("cloud_sword",
            new SwordItem(ModToolMaterials.CLOUD, 3, -2.4F,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CLOUD_PICKAXE = registerItem("cloud_pickaxe",
            new CloudPickaxeItem(ModToolMaterials.CLOUD, 1, -2.8F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item CLOUD_AXE = registerItem("cloud_axe",
            new ModAxeItem(ModToolMaterials.CLOUD, 5.0F, -3.0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item CLOUD_SHOVEL = registerItem("cloud_shovel",
            new ModShovelItem(ModToolMaterials.CLOUD, 1.5F, -3F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item CLOUD_HOE = registerItem("cloud_hoe",
            new ModHoeItem(ModToolMaterials.CLOUD, -3, 0F,
                    new FabricItemSettings().group(ItemGroup.TOOLS)));
    public static final Item CLOUD_HELMET = registerItem("cloud_helmet",
            new CloudArmorItem(ModArmorMaterials.CLOUD, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CLOUD_CHESTPLATE = registerItem("cloud_chestplate",
            new CloudArmorItem(ModArmorMaterials.CLOUD, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CLOUD_LEGGINGS = registerItem("cloud_leggings",
            new CloudArmorItem(ModArmorMaterials.CLOUD, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item CLOUD_BOOTS = registerItem("cloud_boots",
            new CloudArmorItem(ModArmorMaterials.CLOUD, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));

    public static final Item MUSIC_DISC_STORM = registerItem("music_disc_storm",
            new MusicDiscItem(15, ModSoundEvents.RECORD_STORM, new FabricItemSettings().group(ItemGroup.MISC), 240));
    public static final Item MUSIC_DISC_MIRE_MENTAL = registerItem("music_disc_mire_mental",
            new MusicDiscItem(14, ModSoundEvents.RECORD_MIRE_MENTAL, new FabricItemSettings().group(ItemGroup.MISC), 255));

    /*
     * Item Registration
     * */
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
    }

    public static void registerAllItems() {
        System.out.println("Registering items for " + MODID);
    }
}
