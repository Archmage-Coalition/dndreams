package net.eman3600.dndreams.rituals.setup;

import net.eman3600.dndreams.blocks.energy.EchoCandleBlock;
import net.eman3600.dndreams.blocks.energy.RitualCandleBlock;
import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;

import java.util.List;

public abstract class Ritual {
    private final RegistryEntry.Reference<Ritual> entry;

    private final int cost;
    private final Ring[] rings;


    public Ritual(int cost, Ring... rings) {
        this.entry = RitualRegistry.REGISTRY.createEntry(this);
        this.cost = cost;
        this.rings = rings;
    }

    public RegistryEntry.Reference<Ritual> reference() {
        return entry;
    }

    public static Ritual ofID(Identifier id) {
        return RitualRegistry.REGISTRY.get(id);
    }

    public Ring[] getRings() {
        return rings;
    }

    public int cost() {
        return cost;
    }

    public boolean matches(World world, BlockPos pos, boolean lit) {
        for (Ring ring: rings) {
            if (!ring.matches(world, pos, lit)) return false;
        }
        return true;
    }

    public String getTranslationKey() {
        return Util.createTranslationKey("ritual", RitualRegistry.REGISTRY.getId(this));
    }


    /**
     * Called when the ritual is cast by the Soul Candle.
     * @return Whether the casting succeeded and should consume the materials.
     * */
    public abstract boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity);


    public static class Ring {
        private final List<BlockPos> offsets;
        private final CandleTuning tuning;

        public static final List<BlockPos> INNER_RING = BlockPos.stream(-2, 0, -2, 2, 0 ,2).filter(pos -> {
            int x = Math.abs(pos.getX());
            int z = Math.abs(pos.getZ());

            if (x == z) return false;
            if (x == 0 || z == 0) return false;

            return x == 2 || z == 2;
        }).map(BlockPos::toImmutable).toList();

        public static final List<BlockPos> MIDDLE_RING = BlockPos.stream(-4, 0, -4, 4, 0 ,4).filter(pos -> {
            int x = Math.abs(pos.getX());
            int z = Math.abs(pos.getZ());

            if (x == z && x == 3) return true;
            if ((x == 4 && z == 0) || (x == 0 && z == 4)) return true;

            return false;
        }).map(BlockPos::toImmutable).toList();

        public static final List<BlockPos> OUTER_RING = BlockPos.stream(-7, 0, -7, 7, 0 ,7).filter(pos -> {
            int x = Math.abs(pos.getX());
            int z = Math.abs(pos.getZ());

            if (z > x) {
                int a = z;
                z = x;
                x = a;
            }

            if (x == 7 && z == 0) return true;
            if (x == 6 && z == 2) return true;
            if (x == 5 && z == x) return true;

            return false;
        }).map(BlockPos::toImmutable).toList();


        public Ring(List<BlockPos> offsets) {
            this(offsets, CandleTuning.NONE);
        }

        public Ring(List<BlockPos> offsets, CandleTuning tuning) {
            this.offsets = offsets;
            this.tuning = tuning;
        }

        public boolean matches(World world, BlockPos pos, boolean lit) {
            for (BlockPos offset: offsets) {
                BlockPos test = pos.add(offset);

                if (!tuning.matches(world, test, lit)) return false;
            }

            return true;
        }


        public CandleTuning tuning() {
            return tuning;
        }

        public boolean anyTuning() {
            return tuning == CandleTuning.NONE;
        }

        public List<BlockPos> ringOffsets() {
            return offsets;
        }
    }

    public enum CandleTuning implements StringIdentifiable {
        NONE("none", ParticleTypes.SMOKE),
        OVERWORLD("overworld", ParticleTypes.HAPPY_VILLAGER),
        NETHER("nether", ParticleTypes.FLAME),
        END("end", ParticleTypes.DRAGON_BREATH);

        private final ParticleEffect particle;

        public boolean matches(World world, BlockPos test, boolean lit) {
            return (world.getBlockState(test).getBlock() instanceof EchoCandleBlock && (this == NONE || world.getBlockState(test).get(EchoCandleBlock.TUNING) == this) && (!lit || world.getBlockState(test).get(RitualCandleBlock.LIT)));
        }

        private final String name;

        CandleTuning(String name, ParticleEffect particle) {
            this.name = name;
            this.particle = particle;
        }

        @Override
        public String asString() {
            return name;
        }

        public ParticleEffect particle() {
            return particle;
        }
    }
}
