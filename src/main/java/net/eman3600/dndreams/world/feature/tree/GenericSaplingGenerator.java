package net.eman3600.dndreams.world.feature.tree;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class GenericSaplingGenerator extends SaplingGenerator {

    private final RegistryEntry<? extends ConfiguredFeature<?, ?>> tree;

    public GenericSaplingGenerator(RegistryEntry<? extends ConfiguredFeature<?, ?>> tree) {
        this.tree = tree;
    }


    @Nullable
    @Override
    protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return tree;
    }
}
