package net.eman3600.dndreams.initializers.world;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.world.ModConfiguredFeatures;
import net.eman3600.dndreams.initializers.world.ModFeatures;
import net.eman3600.dndreams.world.OreFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

public class ModPlacedFeatures {


    public static final RegistryEntry<PlacedFeature> DREAMWOOD_PLACED = PlacedFeatures.register("dreamwood_placed",
            ModConfiguredFeatures.DREAMWOOD_SPAWN, VegetationPlacedFeatures.modifiers(
                    PlacedFeatures.createCountExtraModifier(0, 0.05f, 1)));

    public static final RegistryEntry<PlacedFeature> EOS_PLACED = PlacedFeatures.register("eos_placed",
            ModConfiguredFeatures.EOS_SPAWN, VegetationPlacedFeatures.modifiers(
                    PlacedFeatures.createCountExtraModifier(2, 0.25f, 1)));

    public static final RegistryEntry<PlacedFeature> HELIOS_PLACED = PlacedFeatures.register("helios_placed",
            ModConfiguredFeatures.HELIOS_SPAWN, VegetationPlacedFeatures.modifiers(
                    PlacedFeatures.createCountExtraModifier(2, 0.25f, 1)));

    public static final RegistryEntry<PlacedFeature> SELENE_PLACED = PlacedFeatures.register("selene_placed",
            ModConfiguredFeatures.SELENE_SPAWN, VegetationPlacedFeatures.modifiers(
                    PlacedFeatures.createCountExtraModifier(2, 0.25f, 1)));




    public static final RegistryEntry<PlacedFeature> HELLSLATE_ORE_PLACED = PlacedFeatures.register("hellslate_ore_placed",
            ModFeatures.HELLSLATE_ORE, OreFeatures.modifiersWithCount(9, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-20), YOffset.aboveBottom(110))));

    public static final RegistryEntry<PlacedFeature> HELLSLATE_ORE_COMMON_PLACED = PlacedFeatures.register("hellslate_ore_common_placed",
            ModFeatures.HELLSLATE_ORE_COMMON, OreFeatures.modifiersWithCount(12, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-20), YOffset.aboveBottom(110))));

    public static final RegistryEntry<PlacedFeature> CELESTIUM_ORE_PLACED = PlacedFeatures.register("celestium_ore_placed",
            ModFeatures.CELESTIUM_ORE, OreFeatures.modifiersWithCount(5, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(10), YOffset.aboveBottom(80))));






}
