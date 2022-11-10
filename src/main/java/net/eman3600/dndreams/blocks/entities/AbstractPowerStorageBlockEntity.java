package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.CosmicFountainBlock;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public abstract class AbstractPowerStorageBlockEntity extends BlockEntity {
    private int power = 0;
    public final List<BlockPos> search;

    public AbstractPowerStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, List<BlockPos> search) {
        super(type, pos, state);
        this.search = search;
    }

    /**
     * Will find all power receivers in range that require power and deliver power if possible.
     * @apiNote If this BlockEntity implements AbstractPowerReceiver, it will only donate power if it does not request power itself.
     */
    public void donate(ServerWorld world) {
        if (this instanceof AbstractPowerReceiver receiver && receiver.needsPower()) return;

        for (BlockPos blockPos: search) {
            try {
                AbstractPowerReceiver receiver = CosmicFountainBlock.findPowerReceiver(world, pos, blockPos);
                if (receiver == null || receiver == this) continue;

                if (receiver.needsPower() && usePower(receiver.powerRequest())) {
                    receiver.addPower(receiver.powerRequest());

                    BlockPos inversePos = blockPos.multiply(-1);
                    BlockPos inverseBlockPos = blockPos.add(pos);

                    ((CosmicFountainBlock) ModBlocks.COSMIC_FOUNTAIN).displayEnchantParticle(world, inverseBlockPos, inversePos, ModParticles.COSMIC_ENERGY, 2);

                    markDirty();
                }
            } catch (ClassCastException ignored) {}
        }
    }


    public boolean canAfford(int amount) {
        return power >= amount;
    }

    /**
     * Uses the requested power, if available.
     * @return True if able to use power, false otherwise.
     * */
    public boolean usePower(int amount) {
        if (!canAfford(amount)) return false;
        power -= amount;
        return true;
    }

    public boolean addPower(int amount) {
        if (power >= getMaxPower()) {
            power = getMaxPower();
            return false;
        }
        power = Math.min(getMaxPower(), power + amount);
        return true;
    }

    public abstract int getMaxPower();

    public int getPower() {
        return power;
    }

    public void setPower(int amount) {
        power = MathHelper.clamp(amount, 0, getMaxPower());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("power", power);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        power = nbt.getInt("power");
    }
}
