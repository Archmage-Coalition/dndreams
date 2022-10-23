package net.eman3600.dndreams.blocks.entities;

import dev.architectury.utils.Env;
import net.eman3600.dndreams.blocks.candle.CosmicFountainBlock;
import net.eman3600.dndreams.blocks.candle.CosmicFountainPoleBlock;
import net.eman3600.dndreams.blocks.candle.CosmicPortalBlock;
import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.cardinal_components.ManaComponent;
import net.eman3600.dndreams.initializers.*;
import net.eman3600.dndreams.networking.packet_s2c.EnergyParticlePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.eman3600.dndreams.blocks.candle.CosmicFountainBlock.GIVE_RANGE;

public class CosmicFountainBlockEntity extends BlockEntity {
    private int ticks = 0;
    private int power = 0;
    private int length = 0;
    private int maxPower = 0;
    private int rate = 5;

    private static final Box eRange = new Box(-GIVE_RANGE,-GIVE_RANGE,-GIVE_RANGE,GIVE_RANGE,GIVE_RANGE,GIVE_RANGE);

    public static int MINIMUM_POWER = 50;
    public static int VERY_MAX_POWER = 10000;

    public static int COSMIC_RATE = 5;


    public CosmicFountainBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COSMIC_FOUNTAIN_ENTITY, pos, state);


    }

    public Box boxRange() {
        return eRange.offset(pos);
    }

    public int getPower() {
        return power;
    }

    public int getLength() {
        return length;
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

    public void recalculateCapacity(World world) {
        int capacity = 0;
        int multiplier = COSMIC_RATE;
        int l = findLength(world, false);

        for (BlockPos blockPos: CosmicFountainBlock.SEARCH_OFFSETS) {
            if (CosmicFountainBlock.isSoulPower(world, pos.down(l), blockPos)) {
                BlockState state = world.getBlockState(pos.down(l).add(blockPos));

                capacity += worth(state);
            }
        }

        for (BlockPos blockPos: CosmicFountainBlock.COSMIC_AUGMENT_OFFSETS) {
            if (CosmicFountainBlock.isCosmicAugment(world, pos.down(l - 1), blockPos)) {
                BlockState state = world.getBlockState(pos.down(l - 1).add(blockPos));

                multiplier += multiplicity(state);
            }
        }

        maxPower = Math.min(capacity, VERY_MAX_POWER);
        rate = multiplier;
    }

    private int worth(BlockState state) {
        if (state.isOf(Blocks.SOUL_FIRE) || state.isOf(Blocks.SCULK)) return 10;
        if (state.isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) return 15;

        return 25;
    }

    private int multiplicity(BlockState state) {
        return 2;
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
        power = 0;
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
        try {
            ServerWorld server = (ServerWorld) world;

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    @Environment(EnvType.CLIENT)
    public static void tickClient(World world, BlockPos blockPos, BlockState blockState, CosmicFountainBlockEntity entity) {
        try {
            ClientWorld client = (ClientWorld) world;

            entity.tickClient(client);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {
        ticks++;

        main: if (getCachedState().get(CosmicFountainBlock.FUNCTIONAL)) {
            if (ticks % 20 == 0) {
                recalculateCapacity(world);
                addPower(rate);

                if (!isValid(world, true) || maxPower < MINIMUM_POWER) {
                    disable(world);
                    break main;
                }
            }

            if (ticks % 5 == 0) {
                for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, boxRange())) {
                    InfusionComponent infusion = EntityComponents.INFUSION.get(player);

                    if (infusion.infused() && infusion.getPower() < infusion.getPowerMax() && usePower(10)) {
                        infusion.chargePower(.4f);

                        for (ServerPlayerEntity viewer: world.getPlayers()) {
                            EnergyParticlePacket.send(viewer, pos, player);
                        }
                    }
                }
            }
        } else {
            if (ticks % 20 == 0) {
                recalculateCapacity(world);
                if (isValid(world, false) && maxPower >= MINIMUM_POWER) enable(world);
            }
        }

        if (ticks >= 20 * 600) {
            ticks = 0;
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Environment(EnvType.CLIENT)
    private void tickClient(ClientWorld world) {
        if (power >= maxPower && getCachedState().get(CosmicFountainBlock.FUNCTIONAL)) {
            System.out.println("Found");
            Random random = world.random;

            for (BlockPos blockPos: CosmicFountainBlock.PORTAL_OFFSETS) {
                if (random.nextInt(6) != 0 || !CosmicFountainBlock.isCosmicPortal(world, pos.down(length), blockPos)) continue;

                BlockPos inversePos = blockPos.multiply(-1).up(length);
                BlockPos inverseBlockPos = blockPos.add(pos).down(length);

                ((CosmicFountainBlock) ModBlocks.COSMIC_FOUNTAIN).displayEnchantParticle(world, inverseBlockPos, inversePos, ModParticles.COSMIC_ENERGY);
            }
        }
    }

    public boolean addPower(int amount) {
        if (power >= maxPower) {
            power = maxPower;
            return false;
        }
        power = Math.min(maxPower, power + amount);
        return true;
    }

    public boolean canAfford(int amount) {
        return power >= amount;
    }

    public boolean usePower(int amount) {
        if (!canAfford(amount)) return false;
        power -= amount;
        return true;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("ticks", ticks);
        nbt.putInt("power", power);
        nbt.putInt("length", length);
        nbt.putInt("max_power", maxPower);
        nbt.putInt("rate", rate);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        ticks = nbt.getInt("ticks");
        power = nbt.getInt("power");
        length = nbt.getInt("length");
        maxPower = nbt.getInt("max_power");
        rate = nbt.getInt("rate");
    }

    public int getMaxPower() {
        return maxPower;
    }

    public int getRate() {
        return rate;
    }
}
