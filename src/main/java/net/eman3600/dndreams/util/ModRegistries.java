package net.eman3600.dndreams.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModAttributes;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.ModItems;
import net.eman3600.dndreams.items.consumable.MutandisExtremisItem;
import net.eman3600.dndreams.items.consumable.MutandisItem;
import net.eman3600.dndreams.items.consumable.MutandisOneirosItem;
import net.eman3600.dndreams.mixin_interfaces.ComposterBlockAccess;
import net.eman3600.dndreams.screen.slot.AttunementBurnSlot;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
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
        registerMutables();
        registerEnergyFuels();
    }

    public static void registerEnergyFuels() {
        AttunementBurnSlot.putFuel(Items.REDSTONE, 5);
        AttunementBurnSlot.putFuel(Items.GLOWSTONE_DUST, 8);
        AttunementBurnSlot.putFuel(Items.SUGAR, 2);
        AttunementBurnSlot.putFuel(Items.GUNPOWDER, 5);
        AttunementBurnSlot.putFuel(Items.BLAZE_POWDER, 20);

        AttunementBurnSlot.putFuel(ModItems.SCULK_POWDER, 30);
        AttunementBurnSlot.putFuel(ModItems.DREAM_POWDER, 30);
        AttunementBurnSlot.putFuel(ModItems.INFERNAL_RESIDUE, 20);
        AttunementBurnSlot.putFuel(ModItems.NIGHTMARE_FUEL, 40);
        AttunementBurnSlot.putFuel(ModItems.WOOD_ASH, 5);
    }

    private static void registerMutables() {
        MutandisItem.registerMutable("foliage", Blocks.GRASS);
        MutandisItem.registerMutable("foliage", Blocks.FERN);
        MutandisItem.registerMutable("foliage", Blocks.RED_MUSHROOM);
        MutandisItem.registerMutable("foliage", Blocks.BROWN_MUSHROOM);
        MutandisItem.registerMutable("foliage", Blocks.DANDELION);
        MutandisItem.registerMutable("foliage", Blocks.POPPY);
        MutandisItem.registerMutable("foliage", Blocks.BLUE_ORCHID);
        MutandisItem.registerMutable("foliage", Blocks.ALLIUM);
        MutandisItem.registerMutable("foliage", Blocks.AZURE_BLUET);
        MutandisItem.registerMutable("foliage", Blocks.RED_TULIP);
        MutandisItem.registerMutable("foliage", Blocks.ORANGE_TULIP);
        MutandisItem.registerMutable("foliage", Blocks.WHITE_TULIP);
        MutandisItem.registerMutable("foliage", Blocks.PINK_TULIP);
        MutandisItem.registerMutable("foliage", Blocks.OXEYE_DAISY);
        MutandisItem.registerMutable("foliage", Blocks.CORNFLOWER);
        MutandisItem.registerMutable("foliage", Blocks.LILY_OF_THE_VALLEY);

        MutandisItem.registerMutable("sapling", Blocks.OAK_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.BIRCH_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.SPRUCE_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.ACACIA_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.DARK_OAK_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.JUNGLE_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.MANGROVE_PROPAGULE);
        MutandisItem.registerMutable("sapling", ModBlocks.SAKURA_SAPLING);
        MutandisItem.registerMutable("sapling", ModBlocks.JAPANESE_MAPLE_SAPLING);

        MutandisItem.registerMutable("crop", Blocks.WHEAT);
        MutandisItem.registerMutable("crop", Blocks.CARROTS);
        MutandisItem.registerMutable("crop", Blocks.POTATOES);
        MutandisItem.registerMutable("crop", Blocks.BEETROOTS);
        MutandisItem.registerMutable("crop", Blocks.PUMPKIN_STEM);
        MutandisItem.registerMutable("crop", Blocks.MELON_STEM);
        MutandisItem.registerMutable("crop", ModBlocks.SNOWBELL_CROP);
        MutandisItem.registerMutable("crop", ModBlocks.WATER_ARTICHOKE);


        MutandisExtremisItem.registerMutable(ModBlocks.SCULK_WOOD_SAPLING);
        MutandisExtremisItem.registerMutable(Blocks.SCULK_SENSOR);
        MutandisExtremisItem.registerMutable(Blocks.SCULK_SHRIEKER);
        MutandisExtremisItem.registerMutable(Blocks.SCULK_CATALYST);

        MutandisExtremisItem.registerMutable(Blocks.CRIMSON_FUNGUS);
        MutandisExtremisItem.registerMutable(Blocks.WARPED_FUNGUS);
        MutandisExtremisItem.registerMutable(Blocks.CRIMSON_ROOTS);
        MutandisExtremisItem.registerMutable(Blocks.WARPED_ROOTS);
        MutandisExtremisItem.registerMutable(Blocks.NETHER_SPROUTS);
        MutandisExtremisItem.registerMutable(Blocks.WITHER_ROSE);

        MutandisExtremisItem.registerMutable(ModBlocks.EOS_GRASS);
        MutandisExtremisItem.registerMutable(ModBlocks.SELENE_GRASS);
        MutandisExtremisItem.registerMutable(ModBlocks.HELIOS_GRASS);
        MutandisExtremisItem.registerMutable(ModBlocks.EOS_SAPLING);
        MutandisExtremisItem.registerMutable(ModBlocks.SELENE_SAPLING);
        MutandisExtremisItem.registerMutable(ModBlocks.HELIOS_SAPLING);

        MutandisExtremisItem.registerMutable(Blocks.NETHER_WART);
        MutandisExtremisItem.registerMutable(Blocks.SUGAR_CANE);
        MutandisExtremisItem.registerMutable(Blocks.CACTUS);


        MutandisOneirosItem.registerMutable(ModBlocks.DREAMWOOD_SAPLING);
    }

    private static void registerSculkTransform() {
        SCULK_TRANSFORM.put(BlockTags.LOGS, ModBlocks.SCULK_WOOD_LOG);
        SCULK_TRANSFORM.put(BlockTags.PLANKS, ModBlocks.SCULK_WOOD_PLANKS);
        SCULK_TRANSFORM.put(BlockTags.LEAVES, ModBlocks.SCULK_WOOD_LEAVES);
        SCULK_TRANSFORM.put(BlockTags.SAPLINGS, ModBlocks.SCULK_WOOD_SAPLING);
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

        StrippableBlockRegistry.register(ModBlocks.SELENE_WOOD, ModBlocks.SELENE_STRIPPED_WOOD);
        StrippableBlockRegistry.register(ModBlocks.SELENE_LOG, ModBlocks.SELENE_STRIPPED_LOG);

        StrippableBlockRegistry.register(ModBlocks.HELIOS_WOOD, ModBlocks.HELIOS_STRIPPED_WOOD);
        StrippableBlockRegistry.register(ModBlocks.HELIOS_LOG, ModBlocks.HELIOS_STRIPPED_LOG);

        StrippableBlockRegistry.register(ModBlocks.EOS_WOOD, ModBlocks.EOS_STRIPPED_WOOD);
        StrippableBlockRegistry.register(ModBlocks.EOS_LOG, ModBlocks.EOS_STRIPPED_LOG);
    }

    private static void registerFlammableBlocks() {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();

        registry.add(ModBlocks.SAKURA_LOG, 5, 5);
        registry.add(ModBlocks.SAKURA_WOOD, 5, 5);
        registry.add(ModBlocks.STRIPPED_SAKURA_LOG, 5, 5);
        registry.add(ModBlocks.STRIPPED_SAKURA_WOOD, 5, 5);

        registry.add(ModBlocks.SAKURA_PLANKS, 5, 20);
        registry.add(ModBlocks.SAKURA_SLAB, 5, 20);
        registry.add(ModBlocks.SAKURA_LEAVES, 30, 60);


        registry.add(ModBlocks.DREAMWOOD_LOG, 10, 10);
        registry.add(ModBlocks.DREAMWOOD, 10, 10);
        registry.add(ModBlocks.DREAMWOOD_LOG, 10, 10);
        registry.add(ModBlocks.STRIPPED_DREAMWOOD, 10, 10);

        registry.add(ModBlocks.DREAMWOOD_PLANKS, 10, 40);
        registry.add(ModBlocks.DREAMWOOD_SLAB, 10, 40);
        registry.add(ModBlocks.DREAMWOOD_LEAVES, 60, 120);


        registry.add(ModBlocks.JAPANESE_MAPLE_LOG, 5, 5);
        registry.add(ModBlocks.JAPANESE_MAPLE_WOOD, 5, 5);
        registry.add(ModBlocks.STRIPPED_JAPANESE_MAPLE_LOG, 5, 5);
        registry.add(ModBlocks.STRIPPED_JAPANESE_MAPLE_WOOD, 5, 5);

        registry.add(ModBlocks.JAPANESE_MAPLE_PLANKS, 5, 10);
        registry.add(ModBlocks.JAPANESE_MAPLE_SLAB, 5, 10);
        registry.add(ModBlocks.JAPANESE_MAPLE_LEAVES, 30, 60);


        registry.add(ModBlocks.SELENE_LOG, 5, 5);
        registry.add(ModBlocks.SELENE_WOOD, 5, 5);
        registry.add(ModBlocks.SELENE_STRIPPED_LOG, 5, 5);
        registry.add(ModBlocks.SELENE_STRIPPED_WOOD, 5, 5);

        registry.add(ModBlocks.SELENE_PLANKS, 5, 10);
        registry.add(ModBlocks.SELENE_SLAB, 5, 10);
        registry.add(ModBlocks.SELENE_LEAVES, 30, 60);


        registry.add(ModBlocks.HELIOS_LOG, 10, 10);
        registry.add(ModBlocks.HELIOS_WOOD, 10, 10);
        registry.add(ModBlocks.HELIOS_STRIPPED_LOG, 10, 10);
        registry.add(ModBlocks.HELIOS_STRIPPED_WOOD, 10, 10);

        registry.add(ModBlocks.HELIOS_PLANKS, 10, 20);
        registry.add(ModBlocks.HELIOS_SLAB, 10, 20);
        registry.add(ModBlocks.HELIOS_LEAVES, 60, 120);


        registry.add(ModBlocks.EOS_LOG, 5, 5);
        registry.add(ModBlocks.EOS_WOOD, 5, 5);
        registry.add(ModBlocks.EOS_STRIPPED_LOG, 5, 5);
        registry.add(ModBlocks.EOS_STRIPPED_WOOD, 5, 5);

        registry.add(ModBlocks.EOS_SLAB, 5, 10);
        registry.add(ModBlocks.EOS_PLANKS, 5, 10);
        registry.add(ModBlocks.EOS_LEAVES, 30, 60);
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

}
