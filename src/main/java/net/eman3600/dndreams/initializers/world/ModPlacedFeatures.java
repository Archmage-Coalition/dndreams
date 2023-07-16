package net.eman3600.dndreams.initializers.world;

import net.eman3600.dndreams.world.OreFeatures;
import net.eman3600.dndreams.world.feature.haven.SmallIslandFeature;
import net.eman3600.dndreams.world.feature.haven.StarSmallIslandFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModPlacedFeatures {


    public static final RegistryEntry<PlacedFeature> DREAMWOOD_PLACED = PlacedFeatures.register("dreamwood_placed",
            ModConfiguredFeatures.DREAMWOOD_SPAWN, VegetationPlacedFeatures.modifiers(
                    PlacedFeatures.createCountExtraModifier(0, 0.05f, 1)));

    public static final RegistryEntry<PlacedFeature> PRISTINE_PLACED = PlacedFeatures.register("pristine_placed",
            ModConfiguredFeatures.PRISTINE_SPAWN, VegetationPlacedFeatures.modifiers(
                    PlacedFeatures.createCountExtraModifier(2, 0.25f, 1)));



    public static final RegistryEntry<PlacedFeature> GOLDEN_GRASS = PlacedFeatures.register("patch_golden_grass", ModConfiguredFeatures.GOLDEN_GRASS, VegetationPlacedFeatures.modifiers(7));

    public static final RegistryEntry<PlacedFeature> HAVEN_GRASS = PlacedFeatures.register("patch_haven_grass", ModConfiguredFeatures.HAVEN_GRASS, VegetationPlacedFeatures.modifiers(7));

    public static final RegistryEntry<PlacedFeature> STAR_GRASS = PlacedFeatures.register("patch_star_grass", ModConfiguredFeatures.STAR_GRASS, VegetationPlacedFeatures.modifiers(5));




    public static final RegistryEntry<PlacedFeature> HELLSLATE_ORE_PLACED = PlacedFeatures.register("hellslate_ore_placed",
            ModFeatures.HELLSLATE_ORE, OreFeatures.modifiersWithCount(9, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-20), YOffset.aboveBottom(110))));

    public static final RegistryEntry<PlacedFeature> HELLSLATE_ORE_COMMON_PLACED = PlacedFeatures.register("hellslate_ore_common_placed",
            ModFeatures.HELLSLATE_ORE_COMMON, OreFeatures.modifiersWithCount(12, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-20), YOffset.aboveBottom(110))));

    public static final RegistryEntry<PlacedFeature> VITAL_ORE_PLACED = PlacedFeatures.register("vital_ore_placed",
            ModFeatures.VITAL_ORE, OreFeatures.modifiersWithCount(90, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop())));

    public static final RegistryEntry<PlacedFeature> CELESTIUM_ORE_PLACED = PlacedFeatures.register("celestium_ore_placed",
            ModFeatures.CELESTIUM_ORE, OreFeatures.modifiersWithCount(5, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(10), YOffset.aboveBottom(80))));





    public static final BCLFeature<SmallIslandFeature, DefaultFeatureConfig> SMALL_ISLAND = registerRawGen("small_island", inlineBuild("small_island", new SmallIslandFeature()), 50);

    public static final BCLFeature<StarSmallIslandFeature, DefaultFeatureConfig> STAR_SMALL_ISLAND = registerRawGen("star_small_island", inlineBuild("star_small_island", new StarSmallIslandFeature()), 50);








    private static <F extends Feature<DefaultFeatureConfig>> BCLFeature<F, DefaultFeatureConfig> registerRawGen(
            String name,
            F feature,
            int density
    ) {
        return registerRawGen(name, feature, DefaultFeatureConfig.INSTANCE, density);
    }

    private static <F extends Feature<FC>, FC extends FeatureConfig> BCLFeature<F, FC> registerRawGen(
            String name,
            F feature,
            FC config,
            int chance
    ) {
        return registerChanced(name, GenerationStep.Feature.RAW_GENERATION, feature, config, chance);
    }

    private static <F extends Feature<FC>, FC extends FeatureConfig> BCLFeature<F, FC> registerChanced(
            String name,
            GenerationStep.Feature decoration,
            F feature,
            FC config,
            int chance
    ) {
        return
                BCLFeatureBuilder
                        .start(new Identifier(MODID, name), feature)
                        .configuration(config)
                        .buildAndRegister()
                        .place()
                        .decoration(decoration)
                        .onceEvery(chance)
                        .squarePlacement()
                        .onlyInBiome()
                        .buildAndRegister();
    }




    public static <F extends Feature<FC>, FC extends FeatureConfig> F inlineBuild(String name, F feature) {
        Identifier l = new Identifier(MODID, name);
        if (Registry.FEATURE.containsId(l)) {
            return (F) Registry.FEATURE.get(l);
        }
        return BCLFeature.register(l, feature);
    }
}
