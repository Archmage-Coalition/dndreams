package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.AttunementChamberBlock;
import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.eman3600.dndreams.initializers.ModItems;
import net.eman3600.dndreams.items.charge.AbstractChargeItem;
import net.eman3600.dndreams.screen.AttunementScreenHandler;
import net.eman3600.dndreams.screen.slot.AttunementBurnSlot;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AttunementChamberBlockEntity extends AbstractPowerStorageBlockEntity implements AbstractPowerReceiver, NamedScreenHandlerFactory, ImplementedInventory {
    public static final int GIVE_RANGE = 16;
    public static final List<BlockPos> GIVE_OFFSETS = BlockPos.stream(-GIVE_RANGE, -GIVE_RANGE, -GIVE_RANGE, GIVE_RANGE, GIVE_RANGE, GIVE_RANGE).map(BlockPos::toImmutable).toList();

    private int cooldownTicks = 0;

    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(2, ItemStack.EMPTY);

    public AttunementChamberBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ATTUNEMENT_CHAMBER_ENTITY, pos, state, GIVE_OFFSETS);
    }

    @Override
    public boolean needsPower() {
        return getPower() < getMaxPower() && getCachedState().get(AttunementChamberBlock.POWERED);
    }

    @Override
    public int powerRequest() {
        return 5;
    }

    @Override
    public int getMaxPower() {
        return inventory.get(0) == ItemStack.EMPTY ? 0 : 500;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("cooldown", cooldownTicks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        cooldownTicks = nbt.getInt("cooldown");
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.dndreams.attunement_chamber");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new AttunementScreenHandler(syncId, inv, ImplementedInventory.of(inventory));
    }

    @Override
    public int getPower() {
        ItemStack stack = inventory.get(0);

        if (stack.getItem() instanceof AbstractChargeItem item) return item.getCharge(stack);

        return 0;
    }

    @Override
    public boolean addPower(int amount) {
        ItemStack stack = inventory.get(0);

        if (stack.getItem() instanceof AbstractChargeItem item) {
            boolean bl = !item.canAffordCharge(stack, 500);
            inventory.set(0, item.charge(stack, amount));

            markDirty();
            return bl;
        } else if (stack.getItem() == Items.AMETHYST_SHARD) {
            ItemStack shard = ModItems.ATTUNED_SHARD.getDefaultStack();

            inventory.set(0, shard);

            markDirty();
            return addPower(amount);
        }

        return false;
    }

    @Override
    public boolean canAfford(int amount) {
        ItemStack stack = inventory.get(0);

        if (stack.getItem() instanceof AbstractChargeItem item) {
            return item.canAffordCharge(stack, amount);
        }

        return false;
    }

    @Override
    public boolean usePower(int amount) {
        ItemStack stack = inventory.get(0);

        if (stack.getItem() instanceof AbstractChargeItem item && canAfford(amount)) {
            inventory.set(0, item.discharge(stack, amount));
            markDirty();
            return true;
        }

        return false;
    }

    @Override
    public void setPower(int amount) {
        inventory.set(0, Items.AMETHYST_SHARD.getDefaultStack());
        addPower(amount);
        markDirty();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, AttunementChamberBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {
        ItemStack burn = inventory.get(1);

        if (getPower() < getMaxPower() && cooldownTicks <= 0 && AttunementBurnSlot.ITEM_TO_ENERGY.containsKey(burn.getItem())) {
            addPower(AttunementBurnSlot.ITEM_TO_ENERGY.getOrDefault(burn.getItem(), 0));

            burn.decrement(1);
            markDirty();
            cooldownTicks = 5;
        } else if (cooldownTicks > 0) {
            cooldownTicks--;
        }

        if (inventory.get(0).getItem() instanceof AbstractChargeItem && !getCachedState().get(AttunementChamberBlock.POWERED)) {
            donate(world);
        }
    }
}
