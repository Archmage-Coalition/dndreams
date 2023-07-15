package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MadMossBlockEntity extends AbstractReplacementBlockEntity {

    private BlockPos sourcePos = new BlockPos(0, 0, 0);
    private int ticks = 10;

    public MadMossBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAD_MOSS_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        NbtList list = toNbtList(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ());
        nbt.put("SourcePos", list);
        nbt.putInt("Ticks", ticks);

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtList list = nbt.getList("SourcePos", NbtElement.INT_TYPE);
        sourcePos = new BlockPos(list.getInt(0), list.getInt(1), list.getInt(2));
        ticks = nbt.getInt("Ticks");
    }



    public static void tick(World world, BlockPos blockPos, BlockState blockState, MadMossBlockEntity entity) {
        entity.tick(world);
    }

    @Override
    protected void tick(World world) {
        super.tick(world);

        ticks--;
        if (ticks <= 0) {
            ticks = 10;

            BlockEntity entity = world.getBlockEntity(sourcePos);

            if (entity instanceof MadMossSourceBlockEntity source) {

                int i = 4;

                while (source.getBlocks() > 0 && i-- >= 0) {
                    Direction direction = Direction.values()[world.random.nextInt(6)];

                    BlockPos spreadPos = getPos().offset(direction);

                    spreadPos = findExposed(world, spreadPos);

                    if (spreadPos != null && canReplace(world, spreadPos)) {
                        replace(world, spreadPos, sourcePos);
                        source.removeBlock();
                        break;
                    }
                }
            } else {
                revert();
                return;
            }
        }

        markDirty();
    }

    public static boolean canReplace(World world, BlockPos pos) {

        BlockState state = world.getBlockState(pos);

        return !state.hasBlockEntity() && state.isFullCube(world, pos) && state.getHardness(world, pos) <= 6 && state.getHardness(world, pos) >= 0 && !state.isIn(ModTags.MAD_MOSS_IMMUNE);
    }

    public static void replace(World world, BlockPos pos, BlockPos sourcePos) {

        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, ModBlocks.MAD_MOSS.getDefaultState(), Block.NOTIFY_LISTENERS);
        if (world.getBlockEntity(pos) instanceof MadMossBlockEntity entity) {

            entity.setReplacedState(state);
            entity.setSourcePos(sourcePos);
        }
    }

    public void setSourcePos(BlockPos pos) {
        sourcePos = pos;
        markDirty();
    }

    public BlockPos getSourcePos() {
        return sourcePos;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
        markDirty();
    }

    @Nullable
    public static BlockPos findExposed(World world, BlockPos pos) {

        for (int i = 1; i > -2; i--) {

            BlockPos test = pos.offset(Direction.UP, i);

            if (world.isInBuildLimit(test) && !world.isAir(test) && isExposed(world, test)) {
                return test;
            }
        }

        return null;
    }

    public static boolean isExposed(World world, BlockPos pos) {

        for (Direction direction: Direction.values()) {
            if (world.isAir(pos.offset(direction))) return true;
        }

        return false;
    }
}
