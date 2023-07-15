package net.eman3600.dndreams.blocks.entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractReplacementBlockEntity extends BlockEntity {

    private BlockState replacedState = Blocks.AIR.getDefaultState();

    public AbstractReplacementBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected void tick(World world) {

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        int i = Block.getRawIdFromState(replacedState);
        nbt.putInt("State", i);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        int i = nbt.getInt("State");
        replacedState = Block.getStateFromRawId(i);
    }

    public void setReplacedState(BlockState state) {

        replacedState = state;
        markDirty();
    }

    public BlockState getReplacedState() {

        return replacedState;
    }

    public void revert() {
        if (world != null) {
            world.setBlockState(getPos(), replacedState, Block.NOTIFY_ALL);
        } else {
            throw new NullPointerException("World not found for " + this);
        }
    }

    public static NbtList toNbtList(double... values) {
        NbtList nbtList = new NbtList();

        for (double d : values) {
            nbtList.add(NbtDouble.of(d));
        }

        return nbtList;
    }

    public static NbtList toNbtList(int... values) {
        NbtList nbtList = new NbtList();

        for (int i : values) {
            nbtList.add(NbtInt.of(i));
        }

        return nbtList;
    }
}
