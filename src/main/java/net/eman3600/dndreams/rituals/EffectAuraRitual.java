package net.eman3600.dndreams.rituals;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.eman3600.dndreams.rituals.setup.SustainedRitual;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EffectAuraRitual extends SustainedRitual {
    private final StatusEffect effect;
    private final int range;
    private final int strength;

    public EffectAuraRitual(StatusEffect effect, int range, int strength, int initialCost, int sustainedCost, Ring... rings) {
        super(initialCost, sustainedCost, rings);
        this.effect = effect;
        this.range = range * 2;
        this.strength = strength;
    }


    @Override
    public boolean onCast(ServerWorld world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void onCease(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {}

    @Override
    public void tickSustained(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        if (world instanceof ServerWorld serverWorld && blockEntity.duration % 20 == 0) {
            Box box = Box.of(Vec3d.ofCenter(pos), range, range, range);
            for (PlayerEntity player : serverWorld.getNonSpectatingEntities(PlayerEntity.class, box)) {

                player.addStatusEffect(new StatusEffectInstance(effect, 120, strength, true, false, true));
            }
        }
    }

    @Override
    public boolean canSustain(World world, BlockPos pos, SoulCandleBlockEntity blockEntity) {
        return true;
    }
}
