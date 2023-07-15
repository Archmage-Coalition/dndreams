package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MadMossSourceBlockEntity extends AbstractReplacementBlockEntity {

    public static final int SEARCH_RANGE = 32;
    private static final Box SEARCH_BOX = new Box(-SEARCH_RANGE,-SEARCH_RANGE,-SEARCH_RANGE,SEARCH_RANGE,SEARCH_RANGE,SEARCH_RANGE);

    private int maxShades = 4;
    private int shades = 0;
    private int nextCheckTicks = 20;
    private int blocks = 40;

    private boolean initialSpread = true;

    public MadMossSourceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAD_MOSS_SOURCE_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("CheckTicks", nextCheckTicks);
        nbt.putInt("Shades", shades);
        nbt.putInt("MaxShades", maxShades);
        nbt.putInt("Blocks", blocks);
        nbt.putBoolean("InitialSpread", initialSpread);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        nextCheckTicks = nbt.getInt("CheckTicks");
        shades = nbt.getInt("Shades");
        maxShades = nbt.getInt("MaxShades");
        blocks = nbt.getInt("Blocks");
        initialSpread = nbt.getBoolean("InitialSpread");
    }



    public static void tick(World world, BlockPos blockPos, BlockState blockState, MadMossSourceBlockEntity entity) {
        entity.tick(world);
    }

    @Override
    protected void tick(World world) {
        super.tick(world);

        nextCheckTicks--;

        if (nextCheckTicks <= 0) {
            nextCheckTicks = world.getRandom().nextBetween(20, 60);

            Box box = SEARCH_BOX.offset(getPos());
            boolean tormented = false;

            for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, box)) {

                TormentComponent torment = EntityComponents.TORMENT.get(player);

                if (torment.getAttunedSanity() < 25) {
                    tormented = true;
                    break;
                }
            }

            if (!tormented) {

                revert();
                return;
            }

            if (initialSpread) {
                for (int i = 0; i < 4; i++) {
                    Direction direction = Direction.fromHorizontal(i);

                    BlockPos spreadPos = getPos().offset(direction);

                    spreadPos = MadMossBlockEntity.findExposed(world, spreadPos);

                    if (spreadPos != null && MadMossBlockEntity.canReplace(world, spreadPos)) {
                        MadMossBlockEntity.replace(world, spreadPos, getPos());
                    }
                }

                initialSpread = false;
            }


        }

        markDirty();
    }

    public static boolean canReplace(World world, BlockPos pos) {

        return MadMossBlockEntity.canReplace(world, pos);
    }

    public static void replace(World world, BlockPos pos) {

        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, ModBlocks.MAD_MOSS_SOURCE.getDefaultState(), Block.NOTIFY_LISTENERS);
        if (world.getBlockEntity(pos) instanceof MadMossSourceBlockEntity entity) {

            entity.setReplacedState(state);
        }
    }

    public int getMaxShades() {
        return maxShades;
    }

    public void setMaxShades(int maxShades) {
        this.maxShades = maxShades;
        markDirty();
    }

    public int getShades() {
        return shades;
    }

    public void setShades(int shades) {
        this.shades = shades;
        markDirty();
    }

    public int getNextCheckTicks() {
        return nextCheckTicks;
    }

    public void setNextCheckTicks(int nextCheckTicks) {
        this.nextCheckTicks = nextCheckTicks;
        markDirty();
    }

    public int getBlocks() {
        return blocks;
    }

    public void removeBlock() {
        this.blocks--;
        markDirty();
    }
}
