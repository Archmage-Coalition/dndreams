package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.CosmicFountainBlock;
import net.eman3600.dndreams.blocks.energy.CosmicFountainPoleBlock;
import net.eman3600.dndreams.blocks.energy.CosmicPortalBlock;
import net.eman3600.dndreams.blocks.energy.RitualCandleBlock;
import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.eman3600.dndreams.blocks.energy.CosmicFountainBlock.GIVE_RANGE;

public class CosmicFountainBlockEntity extends AbstractPowerStorageBlockEntity {
    private int ticks = 0;
    private int length = 0;
    private int maxPower = 0;
    private int rate = 5;

    private static final Box eRange = new Box(-GIVE_RANGE,-GIVE_RANGE,-GIVE_RANGE,GIVE_RANGE,GIVE_RANGE,GIVE_RANGE);

    public static int MINIMUM_POWER = 50;
    public static int VERY_MAX_POWER = 10000;

    public static int COSMIC_RATE = 5;


    public CosmicFountainBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COSMIC_FOUNTAIN_ENTITY, pos, state, CosmicFountainBlock.GIVE_OFFSETS);
    }

    public Box boxRange() {
        return eRange.offset(pos);
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

                if (!world.getBlockState(test).isIn(ModTags.END_STONES)) return false;
            }
        }



        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                BlockPos test = pos.down(l).add(i, 0, j);

                if (i == 0 && j == 0) continue;
                if ((Math.abs(i) >= 2) && (Math.abs(j) >= 2)) {
                    if (Math.abs(i) == 2 && Math.abs(i) == Math.abs(j) && !world.getBlockState(test).isIn(ModTags.END_STONES)) return false;
                    continue;
                }

                if (Math.abs(i) > 2 || Math.abs(j) > 2) {
                    if (!world.getBlockState(test).isIn(ModTags.END_STONES)) return false;
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
        if (state.getBlock() == ModBlocks.COSMIC_CANDLE) return state.get(RitualCandleBlock.LIT) ? 4 : 0;

        return 1;
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
        setPower(0);
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
            if (world == null) throw new ClassCastException();

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
            if (getPower() >= getMaxPower()) {
                Random random = world.random;

                for (BlockPos blockPos: CosmicFountainBlock.PORTAL_OFFSETS) {
                    if (random.nextInt(10) != 0 || !CosmicFountainBlock.isCosmicPortal(world, pos.down(length), blockPos)) continue;

                    BlockPos inversePos = blockPos.multiply(-1).up(length);
                    BlockPos inverseBlockPos = blockPos.add(pos).down(length);

                    ((CosmicFountainBlock) ModBlocks.COSMIC_FOUNTAIN).displayEnchantParticle(world, inverseBlockPos, inversePos, ModParticles.COSMIC_ENERGY);
                }
            }

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

                        ((CosmicFountainBlock) ModBlocks.COSMIC_FOUNTAIN).displayEnchantParticle(world, pos, player, ModParticles.COSMIC_ENERGY, 3);
                    }
                }
            }

            donate(world);
        } else {
            if (ticks % 20 == 0) {
                recalculateCapacity(world);
                if (isValid(world, false) && getMaxPower() >= MINIMUM_POWER) enable(world);
            }
        }

        if (ticks >= 20 * 600) {
            ticks = 0;
        }
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Environment(EnvType.CLIENT)
    private void tickClient(ClientWorld world) {

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("ticks", ticks);
        nbt.putInt("length", length);
        nbt.putInt("max_power", maxPower);
        nbt.putInt("rate", rate);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        ticks = nbt.getInt("ticks");
        length = nbt.getInt("length");
        maxPower = nbt.getInt("max_power");
        rate = nbt.getInt("rate");
    }

    @Override
    public int getMaxPower() {
        return maxPower;
    }

    public int getRate() {
        return rate;
    }
}
