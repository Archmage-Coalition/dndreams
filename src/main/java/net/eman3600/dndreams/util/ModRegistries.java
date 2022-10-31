package net.eman3600.dndreams.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModAttributes;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModItems;
import net.eman3600.dndreams.mixin_interfaces.ComposterBlockAccess;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;

public class ModRegistries {
    public static final Object2ObjectMap<TagKey<Block>, Block> SCULK_TRANSFORM = new Object2ObjectOpenHashMap<>();



    public static void register() {
        registerSculkTransform();
        registerFuels();
        registerFlammableBlocks();
        registerStrippables();
        registerCompostables();
        registerAttributes();
    }

    private static void registerSculkTransform() {
        SCULK_TRANSFORM.put(BlockTags.LOGS, ModBlocks.SAKURA_LOG);
        SCULK_TRANSFORM.put(BlockTags.PLANKS, ModBlocks.SAKURA_PLANKS);
        SCULK_TRANSFORM.put(BlockTags.LEAVES, ModBlocks.SAKURA_LEAVES);
        SCULK_TRANSFORM.put(BlockTags.SAPLINGS, ModBlocks.SAKURA_SAPLING);
    }

    private static void registerFuels() {
        Initializer.LOGGER.info("Registering fuels for " + Initializer.MODID);

        FuelRegistry registry = FuelRegistry.INSTANCE;

        registry.add(ModItems.NIGHTMARE_FUEL, 3200);
    }

    private static void registerStrippables() {
        StrippableBlockRegistry.register(ModBlocks.DREAMWOOD, ModBlocks.STRIPPED_DREAMWOOD);
        StrippableBlockRegistry.register(ModBlocks.DREAMWOOD_LOG, ModBlocks.STRIPPED_DREAMWOOD_LOG);

        StrippableBlockRegistry.register(ModBlocks.SAKURA_WOOD, ModBlocks.STRIPPED_SAKURA_WOOD);
        StrippableBlockRegistry.register(ModBlocks.SAKURA_LOG, ModBlocks.STRIPPED_SAKURA_LOG);

        StrippableBlockRegistry.register(ModBlocks.SCULK_WOOD, ModBlocks.STRIPPED_SCULK_WOOD);
        StrippableBlockRegistry.register(ModBlocks.SCULK_WOOD_LOG, ModBlocks.STRIPPED_SCULK_WOOD_LOG);

        StrippableBlockRegistry.register(ModBlocks.JAPANESE_MAPLE_WOOD, ModBlocks.STRIPPED_JAPANESE_MAPLE_WOOD);
        StrippableBlockRegistry.register(ModBlocks.JAPANESE_MAPLE_LOG, ModBlocks.STRIPPED_JAPANESE_MAPLE_LOG);
    }

    private static void registerFlammableBlocks() {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();

        registry.add(ModBlocks.DREAMWOOD_LOG, 5, 5);
        registry.add(ModBlocks.DREAMWOOD, 5, 5);
        registry.add(ModBlocks.STRIPPED_DREAMWOOD_LOG, 5, 5);
        registry.add(ModBlocks.STRIPPED_DREAMWOOD, 5, 5);

        registry.add(ModBlocks.DREAMWOOD_PLANKS, 5, 20);
        registry.add(ModBlocks.DREAMWOOD_SLAB, 5, 20);
        registry.add(ModBlocks.DREAMWOOD_LEAVES, 30, 60);

        registry.add(ModBlocks.SAKURA_LOG, 10, 10);
        registry.add(ModBlocks.SAKURA_WOOD, 10, 10);
        registry.add(ModBlocks.STRIPPED_SAKURA_LOG, 10, 10);
        registry.add(ModBlocks.STRIPPED_SAKURA_WOOD, 10, 10);

        registry.add(ModBlocks.SAKURA_PLANKS, 10, 40);
        registry.add(ModBlocks.SAKURA_SLAB, 10, 40);
        registry.add(ModBlocks.SAKURA_LEAVES, 60, 120);

        registry.add(ModBlocks.JAPANESE_MAPLE_LOG, 5, 5);
        registry.add(ModBlocks.JAPANESE_MAPLE_WOOD, 5, 5);
        registry.add(ModBlocks.STRIPPED_JAPANESE_MAPLE_LOG, 5, 5);
        registry.add(ModBlocks.STRIPPED_JAPANESE_MAPLE_WOOD, 5, 5);

        registry.add(ModBlocks.JAPANESE_MAPLE_PLANKS, 5, 10);
        registry.add(ModBlocks.JAPANESE_MAPLE_SLAB, 5, 10);
        registry.add(ModBlocks.JAPANESE_MAPLE_LEAVES, 30, 60);
    }

    private static void registerCompostables() {
        ComposterBlockAccess access = (ComposterBlockAccess) Blocks.COMPOSTER;
        access.registerCompostable(.3f, ModItems.SNOWBELL_SEEDS);
        access.registerCompostable(.3f, ModItems.WATER_ARTICHOKE_SEEDS);
        access.registerCompostable(.65f, ModItems.WATER_ARTICHOKE_GLOBE);
        access.registerCompostable(.3f, ModBlocks.SAKURA_SAPLING);
        access.registerCompostable(.65f, ModBlocks.DREAMWOOD_SAPLING);
        access.registerCompostable(.3f, ModBlocks.JAPANESE_MAPLE_SAPLING);
        access.registerCompostable(.65f, ModBlocks.SCULK_WOOD_SAPLING);
    }

    private static void registerAttributes() {

    }
}
