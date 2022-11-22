package net.eman3600.dndreams.world;

import net.eman3600.dndreams.util.ModTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class OreFeatures {
    public static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    public static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    public static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }

    public static final RuleTest BASE_END_STONE = new TagMatchRuleTest(ModTags.BASE_STONE_END);
    public static final RuleTest BASE_SAND_NETHER = new TagMatchRuleTest(ModTags.BASE_SAND_NETHER);
    public static final RuleTest BASE_SOIL_NETHER = new TagMatchRuleTest(ModTags.BASE_SOIL_NETHER);
}