package net.eman3600.dndreams.util;

import net.eman3600.dndreams.Initializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class ModTags {
    /* TAGS */
    public static final TagKey<Block> BASE_STONE_END = ofBlock("base_stone_end");
    public static final TagKey<Block> BASE_SAND_NETHER = ofBlock("base_sand_nether");
    public static final TagKey<Block> BASE_SOIL_NETHER = ofBlock("base_soil_nether");

    public static final TagKey<Biome> GEN_HELLSLATE = ofBiome("gen_hellslate");
    public static final TagKey<Biome> GEN_HELLSLATE_COMMON = ofBiome("gen_hellslate_common");
    public static final TagKey<Biome> GEN_SCULK_ORE = ofBiome("gen_sculk_ore");

    public static final TagKey<Structure> BLIGHT_POWDER_LOCATED = ofStructure("blight_powder_located");

    public static final TagKey<Item> WEAVING_ITEM = ofItem("weaving_items");

    /* TAG REGISTRIES */
    private static TagKey<Block> ofBlock(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(Initializer.MODID, id));
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
}
