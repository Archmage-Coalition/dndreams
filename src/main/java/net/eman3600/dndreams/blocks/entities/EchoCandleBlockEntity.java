package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.eman3600.dndreams.blocks.energy.RitualCandleBlock.LIT;

public class EchoCandleBlockEntity extends BlockEntity implements AbstractPowerReceiver {
    private SoulCandleBlockEntity entity = null;
    private BlockPos linkPos = pos;


    public EchoCandleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ECHO_CANDLE_ENTITY, pos, state);
    }

    @Override
    public boolean addPower(int amount) {
        return entity != null && entity.addPower(amount);
    }

    @Override
    public void setPower(int amount) {
        if (entity == null) return;
        entity.setPower(amount);
    }

    @Override
    public int getPower() {
        return entity != null ? entity.getPower() : 0;
    }

    @Override
    public int getMaxPower() {
        return entity != null ? entity.getMaxPower() : 0;
    }

    @Override
    public boolean usePower(int amount) {
        return entity != null && entity.usePower(amount);
    }

    @Override
    public boolean needsPower() {
        return entity != null && entity.isCasting() && entity.needsPower();
    }

    @Override
    public int powerRequest() {
        return 1;
    }

    public void extinguish(ServerWorld world) {
        linkPos = pos;
        entity = null;

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

        if (entity != null && (entity.isCasting() || entity.isSustained()) && world.getBlockEntity(linkPos) == entity) {
            if (!getCachedState().get(LIT)) world.setBlockState(pos, getCachedState().with(LIT, true));
        } else if (getCachedState().get(LIT) || (entity != null && !entity.isCasting() && !entity.isSustained())) {
            extinguish(world);
        }
    }

    public void link(SoulCandleBlockEntity link) {
        entity = link;
        linkPos = link.getPos();
        markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("linkX", linkPos.getX());
        nbt.putInt("linkY", linkPos.getY());
        nbt.putInt("linkZ", linkPos.getZ());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        BlockPos test = new BlockPos(nbt.getInt("linkX"),nbt.getInt("linkY"),nbt.getInt("linkZ"));

        if (world != null && world.getBlockEntity(test) instanceof SoulCandleBlockEntity e) {
            link(e);
        } else {
            entity = null;
            linkPos = pos;
        }
    }
}
