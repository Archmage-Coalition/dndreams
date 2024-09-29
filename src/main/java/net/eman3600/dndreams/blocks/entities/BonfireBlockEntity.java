package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.BonfireBlock;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.eman3600.dndreams.blocks.energy.BonfireBlock.STRONG;

public class BonfireBlockEntity extends BlockEntity
        implements Clearable {

    private int delayTicks = 0;
    private final DefaultedList<ItemStack> ritualItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private ItemStack ritualFocus = ItemStack.EMPTY;

    public BonfireBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BONFIRE_ENTITY, pos, state);
    }






    public static void tick(World world, BlockPos blockPos, BlockState blockState, BonfireBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {

        if (getCachedState().get(STRONG)) {
            delayTicks--;
            updateListeners();

            if (delayTicks <= 0) {
                world.setBlockState(pos, getCachedState().with(STRONG, false));
            }
        }
    }



    @Environment(EnvType.CLIENT)
    public static void tickClient(World world, BlockPos blockPos, BlockState blockState, BonfireBlockEntity entity) {
        try {
            ClientWorld client = (ClientWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tickClient(client);
        } catch (ClassCastException ignored) {}
    }

    @Environment(EnvType.CLIENT)
    private void tickClient(ClientWorld world) {
        int i;
        Random random = world.random;
        if (random.nextFloat() < (getCachedState().get(STRONG) ? 0.11f: 0.05f)) {
            for (i = 0; i < random.nextInt(2) + 2; ++i) {
                CampfireBlock.spawnSmokeParticle(world, pos, getCachedState().get(STRONG), getCachedState().get(STRONG));
            }
        }
    }




    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("DelayTicks", delayTicks);
        Inventories.writeNbt(nbt, this.ritualItems, true);
        NbtCompound focus = new NbtCompound();
        this.ritualFocus.writeNbt(focus);
        nbt.put("FocusItem", focus);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.delayTicks = nbt.getInt("DelayTicks");
        this.ritualItems.clear();
        Inventories.readNbt(nbt, this.ritualItems);
        this.ritualFocus = ItemStack.fromNbt(nbt.getCompound("FocusItem"));
    }

    @Override
    public void clear() {
        this.ritualItems.clear();
        this.ritualFocus = ItemStack.EMPTY;
        updateListeners();
    }

    public DefaultedList<ItemStack> getRitualItems() {
        return ritualItems;
    }

    public int nextAvailableSlot() {
        for (int i = 0; i < ritualItems.size(); i++) {
            if (ritualItems.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public void removeItems() {

        if (world == null) return;

        ItemScatterer.spawn(world, pos, getRitualItems());
        ItemScatterer.spawn(world, pos, DefaultedList.ofSize(1, getRitualFocus()));

        clear();
    }

    public ItemStack getRitualFocus() {
        return ritualFocus;
    }

    public void setRitualFocus(ItemStack stack) {
        this.ritualFocus = stack;
        updateListeners();
    }

    public void setRitualItem(ItemStack stack, int slot) {
        this.ritualItems.set(slot, stack);
        updateListeners();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.ritualItems, true);
        NbtCompound focusNbt = new NbtCompound();
        this.ritualFocus.writeNbt(focusNbt);
        nbtCompound.put("FocusItem", focusNbt);
        return nbtCompound;
    }
}
