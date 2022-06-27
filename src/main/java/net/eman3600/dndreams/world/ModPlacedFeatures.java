package net.eman3600.dndreams.world;

import net.eman3600.dndreams.initializers.ModFeatures;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

public class ModPlacedFeatures {
    public static final RegistryEntry<PlacedFeature> HELLSLATE_ORE_PLACED = PlacedFeatures.register("hellslate_ore_placed",
            ModFeatures.HELLSLATE_ORE, OreFeatures.modifiersWithCount(9, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-20), YOffset.aboveBottom(110))));

    public static final RegistryEntry<PlacedFeature> HELLSLATE_ORE_COMMON_PLACED = PlacedFeatures.register("hellslate_ore_common_placed",
            ModFeatures.HELLSLATE_ORE_COMMON, OreFeatures.modifiersWithCount(12, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(-20), YOffset.aboveBottom(110))));

    public static final RegistryEntry<PlacedFeature> CELESTIUM_ORE_PLACED = PlacedFeatures.register("celestium_ore_placed",
            ModFeatures.CELESTIUM_ORE, OreFeatures.modifiersWithCount(5, HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(10), YOffset.aboveBottom(80))));
}
