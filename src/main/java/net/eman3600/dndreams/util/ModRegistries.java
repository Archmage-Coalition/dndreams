package net.eman3600.dndreams.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.blocks.entities.RefinedCauldronBlockEntity;
import net.eman3600.dndreams.cardinal_components.ShockComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.mobs.TormentorEntity;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.items.consumable.MutandisExtremisItem;
import net.eman3600.dndreams.items.consumable.MutandisItem;
import net.eman3600.dndreams.items.consumable.MutandisOneirosItem;
import net.eman3600.dndreams.mixin_interfaces.ComposterBlockAccess;
import net.eman3600.dndreams.mixin_interfaces.ItemStackAccess;
import net.eman3600.dndreams.screens.slot.AttunementBurnSlot;
import net.eman3600.dndreams.util.data.EnhancementType;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.item.FoodComponents;
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
        registerApothecary();
        registerInsanityPredicates();
        registerInsanityMobAuras();
        registerShockPredicates();
        registerFoodSanities();
        registerRepairPredicates();
    }

    private static void registerRepairPredicates() {

        ItemStackAccess.registerRepairPredicate(ModTags.SANITY_REPAIRING_TOOLS, (stack, player) -> {

            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return (int)((1 - torment.getAttunedSanity()/100f) * 120 + 40);
        });

        ItemStackAccess.registerRepairPredicate(ModTags.INSANITY_REPAIRING_TOOLS, (stack, player) -> {

            TormentComponent torment = EntityComponents.TORMENT.get(player);

            return (int)(torment.getAttunedSanity()/100f * 70 + 10);
        });

        ItemStackAccess.registerRepairPredicate(ModTags.GROUND_REPAIRING_TOOLS, (stack, player) -> player.isOnGround() ? 160 : -1);
    }

    public static void registerEnergyFuels() {
        AttunementBurnSlot.putFuel(Items.REDSTONE, 10);
        AttunementBurnSlot.putFuel(Items.GLOWSTONE_DUST, 15);
        AttunementBurnSlot.putFuel(Items.SUGAR, 3);
        AttunementBurnSlot.putFuel(Items.GUNPOWDER, 7);
        AttunementBurnSlot.putFuel(Items.BLAZE_POWDER, 50);
        AttunementBurnSlot.putFuel(Items.LAPIS_LAZULI, 25);

        AttunementBurnSlot.putFuel(ModItems.SCULK_POWDER, 50);
        AttunementBurnSlot.putFuel(ModItems.DREAM_POWDER, 90);
        AttunementBurnSlot.putFuel(ModItems.STARDUST, 120);
        AttunementBurnSlot.putFuel(ModItems.INFERNAL_RESIDUE, 50);
        AttunementBurnSlot.putFuel(ModItems.NIGHTMARE_FUEL, 100);
        AttunementBurnSlot.putFuel(ModItems.CRYSTAL_MIX, 150);
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
        MutandisItem.registerMutable("foliage", ModBlocks.EMBER_MOSS);

        MutandisItem.registerMutable("sapling", Blocks.OAK_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.BIRCH_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.SPRUCE_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.ACACIA_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.DARK_OAK_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.JUNGLE_SAPLING);
        MutandisItem.registerMutable("sapling", Blocks.MANGROVE_PROPAGULE);
        MutandisItem.registerMutable("sapling", Blocks.AZALEA);
        MutandisItem.registerMutable("sapling", Blocks.FLOWERING_AZALEA);
        MutandisItem.registerMutable("sapling", ModBlocks.SAKURA_SAPLING);
        MutandisItem.registerMutable("sapling", ModBlocks.JAPANESE_MAPLE_SAPLING);
        MutandisItem.registerMutable("sapling", ModBlocks.SCULK_WOOD_SAPLING);

        MutandisItem.registerMutable("crop", Blocks.WHEAT);
        MutandisItem.registerMutable("crop", Blocks.CARROTS);
        MutandisItem.registerMutable("crop", Blocks.POTATOES);
        MutandisItem.registerMutable("crop", Blocks.BEETROOTS);
        MutandisItem.registerMutable("crop", Blocks.PUMPKIN_STEM);
        MutandisItem.registerMutable("crop", Blocks.MELON_STEM);
        MutandisItem.registerMutable("crop", Blocks.SWEET_BERRY_BUSH);
        MutandisItem.registerMutable("crop", ModBlocks.SNOWBELL);
        MutandisItem.registerMutable("crop", ModBlocks.LOTUS);


        MutandisExtremisItem.registerMutable(Blocks.CRIMSON_FUNGUS);
        MutandisExtremisItem.registerMutable(Blocks.WARPED_FUNGUS);
        MutandisExtremisItem.registerMutable(Blocks.CRIMSON_ROOTS);
        MutandisExtremisItem.registerMutable(Blocks.WARPED_ROOTS);
        MutandisExtremisItem.registerMutable(Blocks.NETHER_SPROUTS);

        MutandisExtremisItem.registerMutable(ModBlocks.GOLDEN_GRASS);
        MutandisExtremisItem.registerMutable(ModBlocks.PRISTINE_SAPLING);

        MutandisExtremisItem.registerMutable(Blocks.NETHER_WART);
        MutandisExtremisItem.registerMutable(Blocks.SUGAR_CANE);
        MutandisExtremisItem.registerMutable(Blocks.CACTUS);


        MutandisOneirosItem.registerMutable(ModBlocks.DREAMWOOD_SAPLING);
        MutandisOneirosItem.registerMutable(ModBlocks.WITHER_BLOSSOM);
        MutandisOneirosItem.registerMutable(Blocks.WITHER_ROSE);
        MutandisOneirosItem.registerMutable(ModBlocks.APPLETHORN);
        MutandisOneirosItem.registerMutable(ModBlocks.HAVEN_GRASS);
        MutandisOneirosItem.registerMutable(ModBlocks.STAR_GRASS);
        MutandisOneirosItem.registerMutable(ModBlocks.HAVEN_SAPLING);
        MutandisOneirosItem.registerMutable(ModBlocks.PINE_SAPLING);
        MutandisOneirosItem.registerMutable(ModBlocks.STAR_SAPLING);
    }

    private static void registerApothecary() {
        RefinedCauldronBlockEntity.registerCapacityModifier(ModItems.LIQUID_VOID, 2, 100);
        RefinedCauldronBlockEntity.registerCapacityModifier(ModItems.WITHER_BUD, 2, 100);
        RefinedCauldronBlockEntity.registerCapacityModifier(ModItems.DISTILLED_SPIRIT, 3, 150);

        RefinedCauldronBlockEntity.registerEnhancement(Items.REDSTONE, EnhancementType.LENGTH, 100);
        RefinedCauldronBlockEntity.registerEnhancement(ModItems.CRYSTAL_MIX, EnhancementType.LENGTH, 100);
        RefinedCauldronBlockEntity.registerEnhancement(ModItems.STARDUST, EnhancementType.LENGTH, 100);

        RefinedCauldronBlockEntity.registerEnhancement(Items.GLOWSTONE_DUST, EnhancementType.AMPLIFIER, 200);
        RefinedCauldronBlockEntity.registerEnhancement(ModItems.REFINED_EVIL, EnhancementType.AMPLIFIER, 200);
        RefinedCauldronBlockEntity.registerEnhancement(ModItems.STAR_FRUIT, EnhancementType.AMPLIFIER, 200);
    }

    private static void registerInsanityPredicates() {
        TormentComponent.registerPredicate(player -> -.5f * ModArmorMaterials.getEquipCount(player, ModArmorMaterials.CELESTIUM));
        TormentComponent.registerPredicate((player, torment) -> torment.isAttuned() ? -3f : 0);
        TormentComponent.registerPredicate(player -> .5f * ModArmorMaterials.getEquipCount(player, ModArmorMaterials.TORMITE));
        TormentComponent.registerPredicate(player -> player.world.getLightLevel(player.getBlockPos(), player.world.getAmbientDarkness()) < 1 ? 4f : 0);
        TormentComponent.registerPredicate(player -> WorldComponents.BLOOD_MOON.get(player.world).isBloodMoon() ? 2f : 0);
        TormentComponent.registerPredicate(player -> -player.getFrozenTicks() / 10f);
        TormentComponent.registerPredicate((player, torment) -> torment.getShroud() > 0 ? 2.5f : 0);
        TormentComponent.registerPredicate(player -> player.hasStatusEffect(ModStatusEffects.HAUNTED) ? 180f : 0f);
    }

    private static void registerInsanityMobAuras() {
        TormentComponent.registerInsanityMob(LivingEntity::isUndead, 4f, 10f);
        TormentComponent.registerInsanityMob(entity -> entity.getType() == EntityType.ENDERMAN && WorldComponents.BOSS_STATE.get(entity.world.getScoreboard()).dragonSlain(), -3f, 10f);
        TormentComponent.registerInsanityMob(entity -> entity.getType() == EntityType.ENDERMAN && !WorldComponents.BOSS_STATE.get(entity.world.getScoreboard()).dragonSlain(), 6f, 10f);
        TormentComponent.registerInsanityMob(entity -> entity.getType() == EntityType.WARDEN, 15f, 40f);
        TormentComponent.registerInsanityMob(entity -> entity instanceof MerchantEntity, -10f, 10f);
        TormentComponent.registerInsanityMob(entity -> entity.getType() == EntityType.CAT, -5f, 16f);
        TormentComponent.registerInsanityMob(entity -> entity instanceof TormentorEntity tormentor && tormentor.isCorporeal(), 10f, 20f);
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

        registry.add(ModItems.NIGHTMARE_FUEL, 4800);
        registry.add(ModItems.ARCHFUEL, 3200);
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

        StrippableBlockRegistry.register(ModBlocks.PRISTINE_WOOD, ModBlocks.STRIPPED_PRISTINE_WOOD);
        StrippableBlockRegistry.register(ModBlocks.PRISTINE_LOG, ModBlocks.STRIPPED_PRISTINE_LOG);

        StrippableBlockRegistry.register(ModBlocks.SHADE_WOOD, ModBlocks.STRIPPED_SHADE_WOOD);
        StrippableBlockRegistry.register(ModBlocks.SHADE_LOG, ModBlocks.STRIPPED_SHADE_LOG);

        StrippableBlockRegistry.register(ModBlocks.HAVEN_WOOD, ModBlocks.HAVEN_STRIPPED_WOOD);
        StrippableBlockRegistry.register(ModBlocks.HAVEN_LOG, ModBlocks.HAVEN_STRIPPED_LOG);

        StrippableBlockRegistry.register(ModBlocks.PINE_WOOD, ModBlocks.PINE_STRIPPED_WOOD);
        StrippableBlockRegistry.register(ModBlocks.PINE_LOG, ModBlocks.PINE_STRIPPED_LOG);

        StrippableBlockRegistry.register(ModBlocks.STAR_WOOD, ModBlocks.STAR_STRIPPED_WOOD);
        StrippableBlockRegistry.register(ModBlocks.STAR_LOG, ModBlocks.STAR_STRIPPED_LOG);
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


        registry.add(ModBlocks.HAVEN_LOG, 5, 5);
        registry.add(ModBlocks.HAVEN_WOOD, 5, 5);
        registry.add(ModBlocks.HAVEN_STRIPPED_LOG, 5, 5);
        registry.add(ModBlocks.HAVEN_STRIPPED_WOOD, 5, 5);

        registry.add(ModBlocks.HAVEN_SLAB, 5, 10);
        registry.add(ModBlocks.HAVEN_PLANKS, 5, 10);
        registry.add(ModBlocks.HAVEN_LEAVES, 30, 60);


        registry.add(ModBlocks.PINE_LOG, 5, 5);
        registry.add(ModBlocks.PINE_WOOD, 5, 5);
        registry.add(ModBlocks.PINE_STRIPPED_LOG, 5, 5);
        registry.add(ModBlocks.PINE_STRIPPED_WOOD, 5, 5);

        registry.add(ModBlocks.PINE_SLAB, 5, 10);
        registry.add(ModBlocks.PINE_PLANKS, 5, 10);
        registry.add(ModBlocks.PINE_LEAVES, 30, 60);


        registry.add(ModBlocks.STAR_LOG, 5, 5);
        registry.add(ModBlocks.STAR_WOOD, 5, 5);
        registry.add(ModBlocks.STAR_STRIPPED_LOG, 5, 5);
        registry.add(ModBlocks.STAR_STRIPPED_WOOD, 5, 5);

        registry.add(ModBlocks.STAR_SLAB, 5, 10);
        registry.add(ModBlocks.STAR_PLANKS, 5, 10);
        registry.add(ModBlocks.STAR_LEAVES, 30, 60);
    }

    private static void registerCompostables() {
        ComposterBlockAccess access = (ComposterBlockAccess) Blocks.COMPOSTER;
        access.registerCompostable(.3f, ModItems.SNOWBELL_SEEDS);
        access.registerCompostable(.3f, ModItems.LOTUS_SEEDS);
        access.registerCompostable(.65f, ModItems.LOTUS_FLOWER);
        access.registerCompostable(.3f, ModBlocks.SAKURA_SAPLING);
        access.registerCompostable(.65f, ModBlocks.DREAMWOOD_SAPLING);
        access.registerCompostable(.3f, ModBlocks.JAPANESE_MAPLE_SAPLING);
        access.registerCompostable(.65f, ModBlocks.SCULK_WOOD_SAPLING);

        access.registerCompostable(.3f, ModItems.APPLETHORN_SEEDS);
        access.registerCompostable(.65f, ModItems.SUCCULENT_APPLE);
        access.registerCompostable(.65f, ModItems.POISON_APPLE);
        access.registerCompostable(1f, ModItems.DRAGONFRUIT);
        access.registerCompostable(.65f, ModItems.STAR_FRUIT);
        access.registerCompostable(.3f, ModItems.WITHER_BLOSSOM_SEEDS);
        access.registerCompostable(.5f, ModItems.DRAGONFRUIT_SEEDS);
    }

    private static void registerShockPredicates() {
        ShockComponent.registerChargePredicate(entity -> !entity.isOnGround());
        ShockComponent.registerChargePredicate(entity -> entity instanceof CreeperEntity);
    }

    private static void registerFoodSanities() {
        ModFoodComponents.registerSanityFood(FoodComponents.APPLE, 3);
        ModFoodComponents.registerSanityFood(FoodComponents.BAKED_POTATO, 3);
        ModFoodComponents.registerSanityFood(FoodComponents.BEEF, -4);
        ModFoodComponents.registerSanityFood(FoodComponents.BEETROOT, 0.5f);
        ModFoodComponents.registerSanityFood(FoodComponents.BEETROOT_SOUP, 3);
        ModFoodComponents.registerSanityFood(FoodComponents.BREAD, 3);
        ModFoodComponents.registerSanityFood(FoodComponents.CARROT, 1);
        ModFoodComponents.registerSanityFood(FoodComponents.CHICKEN, -4);
        ModFoodComponents.registerSanityFood(FoodComponents.CHORUS_FRUIT, 3);
        ModFoodComponents.registerSanityFood(FoodComponents.COD, -6);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKED_BEEF, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKED_CHICKEN, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKED_COD, 4.5f);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKED_MUTTON, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKED_PORKCHOP, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKED_RABBIT, 4.5f);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKED_SALMON, 4.5f);
        ModFoodComponents.registerSanityFood(FoodComponents.COOKIE, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.DRIED_KELP, 0.25f);
        ModFoodComponents.registerSanityFood(FoodComponents.ENCHANTED_GOLDEN_APPLE, 25);
        ModFoodComponents.registerSanityFood(FoodComponents.GOLDEN_APPLE, 12);
        ModFoodComponents.registerSanityFood(FoodComponents.GOLDEN_CARROT, 2);
        ModFoodComponents.registerSanityFood(FoodComponents.HONEY_BOTTLE, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.MELON_SLICE, 1);
        ModFoodComponents.registerSanityFood(FoodComponents.MUSHROOM_STEW, 3);
        ModFoodComponents.registerSanityFood(FoodComponents.MUTTON, -4);
        ModFoodComponents.registerSanityFood(FoodComponents.POISONOUS_POTATO, -16);
        ModFoodComponents.registerSanityFood(FoodComponents.PORKCHOP, -4);
        ModFoodComponents.registerSanityFood(FoodComponents.POTATO, 1);
        ModFoodComponents.registerSanityFood(FoodComponents.PUFFERFISH, -16);
        ModFoodComponents.registerSanityFood(FoodComponents.PUMPKIN_PIE, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.RABBIT, -6);
        ModFoodComponents.registerSanityFood(FoodComponents.RABBIT_STEW, 6);
        ModFoodComponents.registerSanityFood(FoodComponents.ROTTEN_FLESH, -16);
        ModFoodComponents.registerSanityFood(FoodComponents.SALMON, -6);
        ModFoodComponents.registerSanityFood(FoodComponents.SPIDER_EYE, -16);
        ModFoodComponents.registerSanityFood(FoodComponents.SUSPICIOUS_STEW, -4);
        ModFoodComponents.registerSanityFood(FoodComponents.SWEET_BERRIES, 3);
        ModFoodComponents.registerSanityFood(FoodComponents.GLOW_BERRIES, -2);
        ModFoodComponents.registerSanityFood(FoodComponents.TROPICAL_FISH, -6);
    }

}
