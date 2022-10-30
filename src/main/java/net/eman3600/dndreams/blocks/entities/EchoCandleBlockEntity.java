package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.RitualCandleBlock;
import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import static net.eman3600.dndreams.blocks.energy.RitualCandleBlock.LIT;

public class EchoCandleBlockEntity extends BlockEntity implements AbstractPowerReceiver {
    private int power = 0;
    private SoulCandleBlockEntity entity = null;
    private BlockPos linkPos = pos;


    public EchoCandleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ECHO_CANDLE_ENTITY, pos, state);
    }

    @Override
    public boolean addPower(int amount) {
        if (power >= getMaxPower()) {
            power = getMaxPower();
            return false;
        }
        power = Math.min(getMaxPower(), power + amount);
        return true;
    }

    @Override
    public void setPower(int amount) {
        power = MathHelper.clamp(amount, 0, getMaxPower());
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getMaxPower() {
        return 10;
    }

    @Override
    public boolean usePower(int amount) {
        if (canAfford(amount)) {
            power -= amount;
            return true;
        }
        return false;
    }

    @Override
    public boolean needsPower() {
        return (getPower() < getMaxPower()) && entity != null && entity.isCasting();
    }

    @Override
    public int powerRequest() {
        return 2;
    }

    public void extinguish(ServerWorld world) {
        linkPos = pos;
        entity = null;
        power = 0;

        for (ServerPlayerEntity player: world.getPlayers()) {
            world.playSound(player, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
        }

        world.setBlockState(pos, getCachedState().with(LIT, false));
    }


    public static void tick(World world, BlockPos blockPos, BlockState blockState, EchoCandleBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {

        if (power >= getMaxPower() && entity != null && entity.isCasting() && world.getBlockEntity(linkPos) == entity) {
            if (!getCachedState().get(LIT)) world.setBlockState(pos, getCachedState().with(LIT, true));
        } else if (getCachedState().get(LIT) || (entity != null && !entity.isCasting())) {
            extinguish(world);
        }
    }

    public void link(SoulCandleBlockEntity link) {
        entity = link;
        linkPos = link.getPos();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("power", power);
        nbt.putInt("linkX", linkPos.getX());
        nbt.putInt("linkY", linkPos.getY());
        nbt.putInt("linkZ", linkPos.getZ());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        power = nbt.getInt("power");

        BlockPos test = new BlockPos(nbt.getInt("linkX"),nbt.getInt("linkY"),nbt.getInt("linkZ"));

        if (world != null && world.getBlockEntity(test) instanceof SoulCandleBlockEntity e) {
            link(e);
        } else {
            entity = null;
            linkPos = pos;
        }
    }
}
