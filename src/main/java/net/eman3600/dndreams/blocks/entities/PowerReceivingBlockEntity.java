package net.eman3600.dndreams.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class PowerReceivingBlockEntity extends BlockEntity {
    public PowerReceivingBlockEntity(BlockEntityType<? extends PowerReceivingBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract boolean addPower(int amount);
    public abstract int getPower();
    public abstract boolean canAfford(int amount);
    public abstract boolean usePower(int amount);

    public abstract boolean needsPower();
}
