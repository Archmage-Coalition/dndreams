package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.properties.BrewType;
import net.eman3600.dndreams.blocks.properties.CauldronState;
import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.eman3600.dndreams.initializers.ModItems;
import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RefinedCauldronBlockEntity extends BlockEntity implements AbstractPowerReceiver, ImplementedInventory {
    public static int WATER_COLOR = 0x385DC6;

    private DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(20, ItemStack.EMPTY);
    private int ingredients = 0;
    private int level = 0;
    private int boilingTime = 0;
    private int power = 0;
    private int spoilTicks = 0;
    private int brewTicks = 0;
    private int color = WATER_COLOR;
    private CauldronState cauldronState = CauldronState.IDLE;
    public final Box dropArea;


    public RefinedCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFINED_CAULDRON_ENTITY, pos, state);

        dropArea = new Box(-.9, .6, -.9, .9, .8, .9).offset(pos);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, RefinedCauldronBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {
        if (level > 1 && boilingTime < 80 && world.getBlockState(pos.down()).isIn(ModTags.HEAT_BLOCKS)) {
            boilingTime++;
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }

        if (level > 1) {
            List<ItemEntity> entities = world.getNonSpectatingEntities(ItemEntity.class, dropArea);

            for (ItemEntity itemEntity: entities) {
                ItemStack stack = itemEntity.getStack();

                if ((boilingTime >= 80 && cauldronState == CauldronState.IDLE) || stack.isOf(ModItems.WOOD_ASH)) {
                    attemptAdd(stack, itemEntity, world);
                    markDirty();
                    world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    private List<Item> itemList() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < ingredients; i++) items.add(inventory.get(i).getItem());
        return items;
    }

    private void attemptAdd(ItemStack stack, ItemEntity itemEntity, ServerWorld world) {
        if (stack.isOf(ModItems.WOOD_ASH)) {
            reset();

            stack.decrement(1);
            if (stack.getCount() <= 0) itemEntity.kill();

            return;
        }

        if (itemList().contains(stack.getItem()) || ingredients >= inventory.size()) return;

        ItemStack addition = stack.copy();
        addition.setCount(1);

        inventory.set(ingredients, addition);
        ingredients++;

        stack.decrement(1);
        if (stack.getCount() <= 0) itemEntity.kill();

        randomizeColor(world);
    }

    private void randomizeColor(ServerWorld world) {
        try {
            color = world.random.nextInt() & (0xFFFFFF);
        } catch (NullPointerException ignored) {}
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public boolean addPower(int amount) {
        spoilTicks = 0;

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
        return 0;
    }

    @Override
    public boolean usePower(int amount) {
        if (!canAfford(amount)) return false;
        power -= amount;
        return true;
    }

    @Override
    public boolean needsPower() {
        return power < getMaxPower();
    }

    @Override
    public int powerRequest() {
        return 5;
    }

    public void reset() {
        ingredients = 0;
        power = 0;
        level = 0;
        boilingTime = 0;
        color = WATER_COLOR;
        cauldronState = CauldronState.IDLE;
        spoilTicks = 0;
        brewTicks = 0;
        inventory = DefaultedList.ofSize(20, ItemStack.EMPTY);

        markDirty();
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public ItemStack take() {
        markDirty();


        return ItemStack.EMPTY;
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);

        nbt.putInt("power", power);
        nbt.putInt("ingredients", ingredients);
        nbt.putInt("level", level);
        nbt.putInt("boiling_time", boilingTime);
        nbt.putInt("spoil_ticks", spoilTicks);
        nbt.putInt("brew_ticks", brewTicks);
        nbt.putInt("color", color);

        nbt.putString("cauldron_state", cauldronState.asString());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);

        ingredients = nbt.getInt("ingredients");
        power = nbt.getInt("power");
        level = nbt.getInt("level");
        boilingTime = nbt.getInt("boiling_time");
        spoilTicks = nbt.getInt("spoil_ticks");
        brewTicks = nbt.getInt("brew_ticks");
        color = nbt.getInt("color");

        cauldronState = CauldronState.fromString(nbt.getString("cauldron_state"));
    }

    public void fill(int levels) {
        level = Math.min(level + levels, 3);
        markDirty();
    }

    public int getLevel() {
        return level;
    }
    public int getBoilingTime() {
        return boilingTime;
    }
    public boolean isBoiling() {
        return boilingTime >= 80;
    }
    public int getColor() {
        return color;
    }
    public int getSpoilTicks() {
        return spoilTicks;
    }
    public CauldronState getCauldronState() {
        return cauldronState;
    }
    public BrewType getBrewType() {
        System.out.println(inventory);

        if (inventory.get(0).isEmpty()) return BrewType.WATER;
        else if (cauldronState == CauldronState.RUINED) return BrewType.RUINED;
        else if (inventory.get(0).isOf(Items.NETHER_WART)) return BrewType.BREW;
        else return BrewType.CRAFT;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
}
