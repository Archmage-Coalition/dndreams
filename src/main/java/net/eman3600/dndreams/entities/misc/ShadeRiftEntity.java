package net.eman3600.dndreams.entities.misc;

import net.eman3600.dndreams.blocks.spreadable.ShadeMossBlock;
import net.eman3600.dndreams.cardinal_components.DarkStormComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShadeRiftEntity extends Entity {

    public static final int SEARCH_RANGE = 48;
    private static final Box SEARCH_BOX = new Box(-SEARCH_RANGE,-SEARCH_RANGE,-SEARCH_RANGE,SEARCH_RANGE,SEARCH_RANGE,SEARCH_RANGE);

    public static final int TOUCH_RANGE = 2;
    public static final List<BlockPos> TOUCH_OFFSETS = BlockPos.stream(-TOUCH_RANGE, -TOUCH_RANGE, -TOUCH_RANGE, TOUCH_RANGE, TOUCH_RANGE, TOUCH_RANGE).map(BlockPos::toImmutable).toList();

    public static TrackedData<Boolean> RECEDING = DataTracker.registerData(ShadeRiftEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Boolean> FIRST_SPREAD = DataTracker.registerData(ShadeRiftEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static TrackedData<Integer> BLOCKS_LEFT = DataTracker.registerData(ShadeRiftEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Integer> SHADES = DataTracker.registerData(ShadeRiftEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Integer> MAX_SHADES = DataTracker.registerData(ShadeRiftEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static TrackedData<Integer> DELAY_TICKS = DataTracker.registerData(ShadeRiftEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private final List<List<ReplacedState>> replacedStates = new ArrayList<>();

    public ShadeRiftEntity(EntityType<?> type, World world) {
        super(type, world);
        noClip = true;
        getDataTracker().set(BLOCKS_LEFT, world.random.nextBetween(200, 400));
    }

    public ShadeRiftEntity(World world) {
        this(ModEntities.SHADE_RIFT, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (world.isClient) return;

        DarkStormComponent storm = WorldComponents.DARK_STORM.get(world.getScoreboard());

        boolean shouldRecede = !storm.shouldRain();

        if (getDataTracker().get(DELAY_TICKS) > 0) tickDown();
        else {

            if (getDataTracker().get(RECEDING) || shouldRecede) {

                getDataTracker().set(DELAY_TICKS, world.random.nextBetween(2, 4));

                if (replacedStates.size() > 0) {

                    List<ReplacedState> previousChunk = replacedStates.get(replacedStates.size() - 1);

                    for (int i = previousChunk.size() - 1; i >= 0; i--) {
                        ReplacedState state = previousChunk.get(i);

                        revertState(state);
                        receiveBlock();
                    }

                    replacedStates.remove(replacedStates.size() - 1);
                }
            } else {

                if (getDataTracker().get(BLOCKS_LEFT) > 0) {

                    getDataTracker().set(DELAY_TICKS, world.random.nextBetween(2, 6));

                    List<ReplacedState> nextChunk = new ArrayList<>();

                    if (getDataTracker().get(FIRST_SPREAD)) {

                        getDataTracker().set(FIRST_SPREAD, false);
                        ReplacedState state = tryReplace(getBlockPos().down());

                        if (state != null) nextChunk.add(state);
                    } else if (replacedStates.size() > 0) {
                        List<ReplacedState> previousChunk = replacedStates.get(replacedStates.size() - 1);

                        if (previousChunk.isEmpty()) dataTracker.set(BLOCKS_LEFT, 0);
                        else for (ReplacedState previous: previousChunk) {

                            for (Direction direction: Direction.values()) {

                                if (replacedStates.size() > 3 && world.random.nextInt(100) < replacedStates.size()) break;

                                BlockPos pos = previous.pos.offset(direction);
                                pos = findExposed(pos);

                                if (pos != null) {

                                    ReplacedState state = tryReplace(pos);

                                    if (state != null) {
                                        nextChunk.add(state);
                                        spendBlock();
                                    }
                                }
                            }
                        }
                    }

                    replacedStates.add(nextChunk);
                } else {

                    getDataTracker().set(DELAY_TICKS, 20);
                }
            }

            if (replacedStates.isEmpty()) {

                discard();
            }
        }
    }

    public void updateMoss() {

        for (List<ReplacedState> chunk: replacedStates) {

            for (int i = chunk.size() - 1; i >= 0; i--) {
                ReplacedState state = chunk.get(i);

                if (!world.getBlockState(state.pos).isOf(ModBlocks.SHADE_MOSS)) {

                    revertState(state);
                    chunk.remove(i);
                }
            }
        }
    }

    public void tickDown() {
        getDataTracker().set(DELAY_TICKS, getDataTracker().get(DELAY_TICKS) - 1);
    }

    public void spendBlock() {
        getDataTracker().set(BLOCKS_LEFT, getDataTracker().get(BLOCKS_LEFT) - 1);
    }

    public void receiveBlock() {
        getDataTracker().set(BLOCKS_LEFT, getDataTracker().get(BLOCKS_LEFT) + 1);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(RECEDING, false);
        getDataTracker().startTracking(FIRST_SPREAD, true);
        getDataTracker().startTracking(BLOCKS_LEFT, 150);
        getDataTracker().startTracking(DELAY_TICKS, 10);
        getDataTracker().startTracking(SHADES, 0);
        getDataTracker().startTracking(MAX_SHADES, 5);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

        DataTracker tracker = getDataTracker();

        if (nbt.contains("Receding")) {
            tracker.set(RECEDING, nbt.getBoolean("Receding"));
        }
        if (nbt.contains("FirstSpread")) {
            tracker.set(FIRST_SPREAD, nbt.getBoolean("FirstSpread"));
        }
        if (nbt.contains("BlocksLeft")) {
            tracker.set(BLOCKS_LEFT, nbt.getInt("BlocksLeft"));
        }
        if (nbt.contains("Shades")) {
            tracker.set(SHADES, nbt.getInt("Shades"));
        }
        if (nbt.contains("MaxShades")) {
            tracker.set(MAX_SHADES, nbt.getInt("MaxShades"));
        }
        if (nbt.contains("DelayTicks")) {
            tracker.set(DELAY_TICKS, nbt.getInt("DelayTicks"));
        }

        replacedStates.clear();

        NbtList chunks = nbt.getList("ReplacedStates", NbtElement.LIST_TYPE);

        for (int i = 0; i < chunks.size(); i++) {
            NbtList chunk = chunks.getList(i);
            List<ReplacedState> list = new ArrayList<>();

            for (int j = 0; j < chunk.size(); j++) {

                ReplacedState state = new ReplacedState(chunk.getCompound(j));
                list.add(state);
            }

            replacedStates.add(list);
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

        DataTracker tracker = getDataTracker();

        nbt.putBoolean("Receding", tracker.get(RECEDING));
        nbt.putBoolean("FirstSpread", tracker.get(FIRST_SPREAD));
        nbt.putInt("BlocksLeft", tracker.get(BLOCKS_LEFT));
        nbt.putInt("Shades", tracker.get(SHADES));
        nbt.putInt("MaxShades", tracker.get(MAX_SHADES));
        nbt.putInt("DelayTicks", tracker.get(DELAY_TICKS));

        NbtList chunks = new NbtList();

        for (List<ReplacedState> list: replacedStates) {

            NbtList chunk = new NbtList();

            for (ReplacedState state: list) {

                chunk.add(state.toNbt());
            }

            chunks.add(chunk);
        }

        nbt.put("ReplacedStates", chunks);
    }

    private ReplacedState replaceBlock(BlockPos pos) {

        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, ModBlocks.SHADE_MOSS.getDefaultState().with(ShadeMossBlock.TORMENTED, true), Block.NOTIFY_LISTENERS);

        BlockPos surfacePos = pos.up();
        BlockState surfaceState = world.getBlockState(surfacePos);

        if (surfaceState.isOf(Blocks.GRASS)) {
            world.setBlockState(surfacePos, ModBlocks.SHADE_GRASS.getDefaultState(), Block.NOTIFY_LISTENERS);
        } else if (surfaceState.isOf(Blocks.FERN)) {
            world.setBlockState(surfacePos, ModBlocks.SHADE_FERN.getDefaultState(), Block.NOTIFY_LISTENERS);
        } else if (surfaceState.isAir()) {

            int i = world.random.nextInt(50);

            if (i >= 31) {
                world.setBlockState(surfacePos, ModBlocks.SHADE_WEED.getDefaultState(), Block.NOTIFY_LISTENERS);
            } else if (i <= 1) {
                world.setBlockState(surfacePos, ModBlocks.SHADE_SHROOM.getDefaultState(), Block.NOTIFY_LISTENERS);
            }/* else if (i == 2) {
                BlockState bush = ModBlocks.SHADE_BUSH.getDefaultState();
                ShadeSaplingBlock sapling = (ShadeSaplingBlock)ModBlocks.SHADE_BUSH;

                if (sapling.canGrow(world, world.random, surfacePos, bush)) {
                    sapling.grow((ServerWorld) world, world.random, surfacePos, bush);
                }
            }*/
        }

        return new ReplacedState(pos, state);
    }

    private ReplacedState tryReplace(BlockPos pos) {

        BlockState state = world.getBlockState(pos);

        if (!state.hasBlockEntity() && state.isFullCube(world, pos) && state.getHardness(world, pos) <= 6 && state.getHardness(world, pos) >= 0 && !state.isIn(ModTags.SHADE_MOSS_IMMUNE)) {
            return replaceBlock(pos);
        }

        return null;
    }

    private void revertState(ReplacedState state) {

        BlockPos surfacePos = state.pos.up();
        BlockState surfaceState = world.getBlockState(surfacePos);

        if (surfaceState.isOf(ModBlocks.SHADE_GRASS)) {
            world.setBlockState(surfacePos, Blocks.GRASS.getDefaultState(), Block.NOTIFY_LISTENERS);
        } else if (surfaceState.isOf(ModBlocks.SHADE_FERN)) {
            world.setBlockState(surfacePos, Blocks.FERN.getDefaultState(), Block.NOTIFY_LISTENERS);
        } else if (surfaceState.isOf(ModBlocks.SHADE_WEED) || surfaceState.isOf(ModBlocks.SHADE_SHROOM)) {
            world.removeBlock(surfacePos, false);
        }

        world.setBlockState(state.pos, state.state, Block.NOTIFY_ALL);
    }

    @Nullable
    public BlockPos findExposed(BlockPos pos) {

        int i = 0;
        int tries = 3;

        while (tries > 0) {
            tries--;

            BlockPos test = pos.offset(Direction.UP, i);

            if (!world.isInBuildLimit(test) || !isInRange(test)) return null;

            if (!world.isAir(test) && world.getFluidState(test).isEmpty() && isExposed(test)) {
                return test;
            } else if (world.isAir(test) || !world.getFluidState(test).isEmpty()) {
                i--;
            } else {
                i++;
            }
        }

        return null;
    }

    public boolean isExposed(BlockPos pos) {

        return isExposed(world, pos);
    }

    public static boolean isExposed(WorldAccess world, BlockPos pos) {

        for (Direction direction: Direction.values()) {
            if (!world.getBlockState(pos.offset(direction)).isOpaqueFullCube(world, pos)) return true;
        }

        return false;
    }

    public boolean isInRange(BlockPos pos) {

        BlockPos difference = getBlockPos().subtract(pos);

        return Math.abs(difference.getX()) < ShadeMossBlock.SEARCH_RANGE && Math.abs(difference.getY()) < ShadeMossBlock.SEARCH_RANGE && Math.abs(difference.getZ()) < ShadeMossBlock.SEARCH_RANGE;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        passenger.stopRiding();
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends ShadeRiftEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos test = pos.down();
        return !world.getBlockState(test).isAir() && world.getFluidState(test).isEmpty() && isExposed(world, test);
    }

    private static class ReplacedState {
        private final BlockPos pos;
        private final BlockState state;

        private ReplacedState(BlockPos pos, BlockState state) {
            this.pos = pos;
            this.state = state;
        }

        private ReplacedState(NbtCompound nbt) {
            this(new BlockPos(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z")), Block.getStateFromRawId(nbt.getInt("State")));
        }

        public BlockState getState() {
            return state;
        }

        public BlockPos getPos() {
            return pos;
        }

        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();

            nbt.putInt("State", Block.getRawIdFromState(state));
            nbt.putInt("X", pos.getX());
            nbt.putInt("Y", pos.getY());
            nbt.putInt("Z", pos.getZ());

            return nbt;
        }
    }
}
