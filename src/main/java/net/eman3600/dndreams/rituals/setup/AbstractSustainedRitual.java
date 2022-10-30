package net.eman3600.dndreams.rituals.setup;

import net.eman3600.dndreams.blocks.entities.SoulCandleBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractSustainedRitual extends AbstractRitual {
    private final int sustainedCost;


    public AbstractSustainedRitual(int initialCost, int sustainedCost, Ring... rings) {
        super(initialCost, rings);
        this.sustainedCost = sustainedCost;
    }

    @Override
    public boolean isSustained() {
        return true;
    }

    public int getSustainedCost() {
        return sustainedCost;
    }

    public abstract void onCease(World world, BlockPos pos, SoulCandleBlockEntity blockEntity);
    public abstract void tickSustained(World world, BlockPos pos, SoulCandleBlockEntity blockEntity);
    public abstract boolean canSustain(World world, BlockPos pos, SoulCandleBlockEntity blockEntity);
}
