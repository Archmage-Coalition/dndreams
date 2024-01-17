package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class InsightTableBlockEntity extends BlockEntity implements AbstractPowerReceiver {

    private int power = 0;

    public InsightTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INSIGHT_TABLE_ENTITY, pos, state);
    }

    @Override
    public boolean addPower(int amount) {

        if (power >= getMaxPower()) {
            power = getMaxPower();
            return false;
        }
        power = Math.min(getMaxPower(), power + amount);
        markDirty();
        return true;
    }

    @Override
    public void setPower(int amount) {
        power = Math.min(amount, getMaxPower());
        markDirty();
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getMaxPower() {
        return 500;
    }

    @Override
    public boolean usePower(int amount) {
        if (canAfford(amount)) {

            power -= amount;
            markDirty();
            return true;
        }
        return false;
    }

    @Override
    public boolean needsPower() {
        return power < getMaxPower();
    }

    @Override
    public int powerRequest() {
        return 5;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("power", power);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("power")) power = nbt.getInt("power");
    }
}
