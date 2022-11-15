package net.eman3600.dndreams.world.feature.tree.sapling_generator;

import net.minecraft.block.sapling.LargeTreeSaplingGenerator;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class GenericSaplingGenerator extends SaplingGenerator {

    private final Function<Random, RegistryEntry<? extends ConfiguredFeature<?, ?>>> tree;

    public GenericSaplingGenerator(Function<Random, RegistryEntry<? extends ConfiguredFeature<?, ?>>> tree) {
        this.tree = tree;
    }

    @Nullable
    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return tree.apply(random);
    }
}
