package net.eman3600.dndreams.util;

import net.eman3600.dndreams.Initializer;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;

public class ModTags {
    /* TAGS */
    public static final TagKey<Block> BASE_STONE_END = ofBlock("base_stone_end");
    public static final TagKey<Block> BASE_MARBLE_END = ofBlock("base_marble_end");
    public static final TagKey<Block> BASE_SAND_NETHER = ofBlock("base_sand_nether");
    public static final TagKey<Block> BASE_SOIL_NETHER = ofBlock("base_soil_nether");
    public static final TagKey<Block> SUBSTANTIAL = ofBlock("substantial");
    public static final TagKey<Block> MARBLES = ofBlock("marbles");
    public static final TagKey<Block> DEEPSLATE_FRAME = ofBlock("deepslate_frame");
    public static final TagKey<Block> SOUL_POWER = ofBlock("soul_power");
    public static final TagKey<Block> COSMIC_AUGMENTS = ofBlock("cosmic_augments");
    public static final TagKey<Block> COSMIC_BASE = ofBlock("cosmic_base");
    public static final TagKey<Block> END_STONES = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "end_stones"));
    public static final TagKey<Block> HEAT_BLOCKS = ofBlock("heat_blocks");
    public static final TagKey<Block> HAVEN_DIRT = ofBlock("haven_dirt");
    public static final TagKey<Block> BASE_STONE_HAVEN = ofBlock("base_stone_haven");
    public static final TagKey<Block> SHADE_MOSS_IMMUNE = ofBlock("shade_moss_immune");
    public static final TagKey<Block> CURSE_FIRES = ofBlock("curse_fires");
    public static final TagKey<Block> CURSE_CAMPFIRES = ofBlock("curse_campfires");

    public static final TagKey<Fluid> FLOWING_SPIRIT = ofFluid("flowing_spirit");
    public static final TagKey<Fluid> SORROW = ofFluid("sorrow");
    public static final TagKey<Fluid> HYDRO = ofFluid("hydro");

    public static final TagKey<Biome> GEN_HELLSLATE = ofBiome("gen_hellslate");
    public static final TagKey<Biome> GEN_HELLSLATE_COMMON = ofBiome("gen_hellslate_common");
    public static final TagKey<Biome> SHROUDED = ofBiome("shrouded");

    public static final TagKey<Structure> TAINTED_PEARL_LOCATED = ofStructure("tainted_pearl_located");
    public static final TagKey<Structure> ENSHROUDED = ofStructure("enshrouded");

    public static final TagKey<Item> SMELTING_TOOLS = ofItem("smelting_tools");
    public static final TagKey<Item> AUTO_REPAIRING_TOOLS = ofItem("auto_repairing_tools");
    public static final TagKey<Item> MANA_BUFFERING_TOOLS = ofItem("mana_buffering_tools");
    public static final TagKey<Item> SANITY_REPAIRING_TOOLS = ofItem("sanity_repairing_tools");
    public static final TagKey<Item> INSANITY_REPAIRING_TOOLS = ofItem("insanity_repairing_tools");
    public static final TagKey<Item> GROUND_REPAIRING_TOOLS = ofItem("ground_repairing_tools");
    public static final TagKey<Item> FAST_GROUND_REPAIRING_TOOLS = ofItem("fast_ground_repairing_tools");
    public static final TagKey<Item> DREAM_EXCLUSIVE = ofItem("dream_exclusive");
    public static final TagKey<Item> CORRUPTORS = ofItem("corruptors");
    public static final TagKey<Item> ATLAS = ofItem("atlas_items");
    public static final TagKey<Item> QUIVERS = ofItem("quivers");

    public static final TagKey<EntityType<?>> SUBSTANTIAL_ENTITIES = ofEntity("substantial");
    public static final TagKey<EntityType<?>> GLOOM_ENTITIES = ofEntity("gloom");
    public static final TagKey<EntityType<?>> ROT_IMMUNE_ENTITIES = ofEntity("rot_immune");
    public static final TagKey<EntityType<?>> GLOOM_PROJECTILE_ENTITIES = ofEntity("gloom_projectiles");

    /* TAG REGISTRIES */
    private static TagKey<Block> ofBlock(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(Initializer.MODID, id));
    }

    private static TagKey<Fluid> ofFluid(String id) {
        return TagKey.of(Registry.FLUID_KEY, new Identifier(Initializer.MODID, id));
    }

    private static TagKey<Item> ofItem(String id) {
        return TagKey.of(Registry.ITEM_KEY, new Identifier(Initializer.MODID, id));
    }

    private static TagKey<Biome> ofBiome(String id) {
        return TagKey.of(Registry.BIOME_KEY, new Identifier(Initializer.MODID, id));
    }

    private static TagKey<Structure> ofStructure(String id) {
        return TagKey.of(Registry.STRUCTURE_KEY, new Identifier(Initializer.MODID, id));
    }

    private static TagKey<EntityType<?>> ofEntity(String id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Initializer.MODID, id));
    }


    public static void registerTags() {
        System.out.println("Registering tags for " + Initializer.MODID);
    }
}
