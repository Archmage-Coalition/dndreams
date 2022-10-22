package net.eman3600.dndreams.blocks.entities;

import com.google.common.collect.ImmutableList;
import net.eman3600.dndreams.blocks.candle.CosmicFountainBlock;
import net.eman3600.dndreams.blocks.candle.CosmicFountainPoleBlock;
import net.eman3600.dndreams.blocks.candle.CosmicPortalBlock;
import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.eman3600.dndreams.initializers.ModBlocks;
import net.eman3600.dndreams.initializers.WorldComponents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CosmicFountainBlockEntity extends BlockEntity {
    private int ticks = 0;
    private int power = 0;
    private int length = 0;

    public static int MAX_POWER = 5000;
    public static int COSMIC_RATE = 25;


    public CosmicFountainBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COSMIC_FOUNTAIN_ENTITY, pos, state);
    }

    public int getPower() {
        return power;
    }

    public int findLength(World world, boolean mustBeActive) {
        int count = 0;
        BlockPos test = pos.down();

        while(!world.isOutOfHeightLimit(test) && world.getBlockState(test).getBlock() == ModBlocks.COSMIC_FOUNTAIN_POLE) {
            if (mustBeActive && !world.getBlockState(test).get(CosmicFountainPoleBlock.FUNCTIONAL)) break;
            count++;
            test = test.down();
        }

        return count;
    }

    public boolean isValid(World world, boolean mustBeActive) {
        if (!WorldComponents.BOSS_STATE.get(world.getScoreboard()).dragonSlain()) return false;

        int l = findLength(world, mustBeActive);
        if (l < 3 || l > 10) return false;

        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                if ((Math.abs(i) == 2) && (Math.abs(j) == 2)) continue;

                BlockPos test = pos.down(l + 1).add(i, 0, j);

                if (world.getBlockState(test).getBlock() != Blocks.END_STONE) return false;
            }
        }

        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                BlockPos test = pos.down(l).add(i, 0, j);

                if (i == 0 && j == 0) continue;
                if ((Math.abs(i) >= 2) && (Math.abs(j) >= 2)) {
                    if (Math.abs(i) == 2 && Math.abs(i) == Math.abs(j) && world.getBlockState(test).getBlock() != Blocks.END_STONE) return false;
                    continue;
                }

                if (Math.abs(i) > 2 || Math.abs(j) > 2) {
                    if (world.getBlockState(test).getBlock() != Blocks.END_STONE) return false;
                    continue;
                }

                if (mustBeActive ? world.getBlockState(test).getBlock() != ModBlocks.COSMIC_PORTAL : !world.isAir(test)) return false;
            }
        }

        return true;
    }



    public void disable(World world) {
        world.setBlockState(pos, getCachedState().with(CosmicFountainBlock.FUNCTIONAL, false));

        for (int i = 1; i <= length; i++) {
            try {
                CosmicFountainPoleBlockEntity entity = (CosmicFountainPoleBlockEntity) world.getBlockEntity(pos.down(i));

                if (entity == null) {
                    throw new NullPointerException();
                }

                entity.disable(world);
            } catch (ClassCastException | NullPointerException ignored) {}
        }

        ((CosmicPortalBlock)ModBlocks.COSMIC_PORTAL).scatterBreak(world, pos.down(length));

        ticks = 0;
        length = 0;
    }

    public void enable(World world) {
        ticks = 0;
        length = findLength(world, false);

        world.setBlockState(pos, getCachedState().with(CosmicFountainBlock.FUNCTIONAL, true));

        for (int i = 1; i <= length; i++) {
            try {
                CosmicFountainPoleBlockEntity entity = (CosmicFountainPoleBlockEntity) world.getBlockEntity(pos.down(i));

                if (entity == null) {
                    throw new NullPointerException();
                }

                entity.enable(world, getCachedState(), i);
            } catch (ClassCastException | NullPointerException ignored) {}
        }

        ((CosmicPortalBlock)ModBlocks.COSMIC_PORTAL).open(world, pos.down(length));
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, CosmicFountainBlockEntity entity) {
        entity.tick(world);
    }

    private void tick(World world) {
        ticks++;

        if (getCachedState().get(CosmicFountainBlock.FUNCTIONAL)) {
            if (ticks % 20 == 0) {
                int mult = 1;

                addPower(mult * COSMIC_RATE);
            }

            if (ticks % 60 == 0) {
                if (!isValid(world, true)) disable(world);
            }
        } else {
            if (ticks % 60 == 0) {
                if (isValid(world, false)) enable(world);
            }
        }

        if (ticks >= 20 * 600) {
            ticks = 0;
        }
    }

    public boolean addPower(int amount) {
        if (power >= 5000) return false;
        power = Math.min(5000, power + amount);
        return true;
    }

    public int blocksInRange(WorldAccess world, Block block) {
        List<BlockState> states = world.getStatesInBox(new Box(pos.add(16, 17 - length, 16), pos.add(-16, -15 - length, -16))).toList();

        List<BlockState> found = new ArrayList<>();

        for (int i = states.size() - 1; i >= 0; i--) {
            if (states.get(i).getBlock() == block) {
                found.add(states.get(i));
            }
        }

        return found.size();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("ticks", ticks);
        nbt.putInt("power", power);
        nbt.putInt("length", length);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        ticks = nbt.getInt("ticks");
        power = nbt.getInt("power");
        length = nbt.getInt("length");
    }
}
