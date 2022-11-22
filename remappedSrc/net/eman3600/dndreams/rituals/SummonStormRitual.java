package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.AbstractSustainedRitual;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SummonStormRitual extends AbstractSustainedRitual {
    public SummonStormRitual() {
        super(1000, 200, new Ring(Ring.INNER_RING, CandleTuning.OVERWORLD), new Ring(Ring.MIDDLE_RING, CandleTuning.OVERWORLD));
    }

    @Override
    public boolean onCast(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        return !world.isThundering();
    }

    @Override
    public void onCease(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        if (world instanceof ServerWorld serverWorld) {
            int duration = blockEntity.duration;

            serverWorld.setWeather(0, duration * 30, true, true);
        }
    }

    @Override
    public void tickSustained(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        if (world instanceof ServerWorld serverWorld) {
            Vec3d vec = Vec3d.ofCenter(pos);

            serverWorld.spawnParticles(ParticleTypes.END_ROD, vec.x, vec.y, vec.z, 5, 0.5, 0.5, 0.5, 0.1);
        }
    }

    @Override
    public boolean canSustain(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        return blockEntity.duration < 1200;
    }
}
