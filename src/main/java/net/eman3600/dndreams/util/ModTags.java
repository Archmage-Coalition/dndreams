package net.eman3600.dndreams.util;

import net.eman3600.dndreams.Initializer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;

public class ModTags {
    /* TAGS */
    public static final TagKey<Block> BASE_STONE_END = ofBlock("base_stone_end");
    public static final TagKey<Block> BASE_SAND_NETHER = ofBlock("base_sand_nether");
    public static final TagKey<Block> BASE_SOIL_NETHER = ofBlock("base_soil_nether");
    public static final TagKey<Block> DREAMWOOD_LOGS = ofBlock("dreamwood_logs");
    public static final TagKey<Block> DREAMWOOD = ofBlock("dreamwood");
    public static final TagKey<Block> SUBSTANTIAL = ofBlock("substantial");
    public static final TagKey<Block> DEEPSLATE_FRAME = ofBlock("deepslate_frame");
    public static final TagKey<Block> SOUL_POWER = ofBlock("soul_power");
    public static final TagKey<Block> COSMIC_AUGMENTS = ofBlock("cosmic_augments");

    public static final TagKey<Fluid> FLOWING_SPIRIT = ofFluid("flowing_spirit");

    public static final TagKey<Biome> GEN_HELLSLATE = ofBiome("gen_hellslate");
    public static final TagKey<Biome> GEN_HELLSLATE_COMMON = ofBiome("gen_hellslate_common");

    public static final TagKey<Structure> TAINTED_PEARL_LOCATED = ofStructure("tainted_pearl_located");

    public static final TagKey<Item> WEAVING_ITEMS = ofItem("weaving_items");
    public static final TagKey<Item> MANA_USING_TOOLS = ofItem("mana_using_tools");
    public static final TagKey<Item> POWER_USING_TOOLS = ofItem("power_using_tools");

    public static final TagKey<EntityType<?>> SUBSTANTIAL_ENTITIES = ofEntity("substantial");

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
