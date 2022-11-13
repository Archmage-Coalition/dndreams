package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.recipe.RefineryRecipe;
import net.eman3600.dndreams.screen.RefineryScreenHandler;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class RefineryBlockEntity extends BlockEntity implements AbstractPowerReceiver, NamedScreenHandlerFactory, ImplementedInventory {

    private int power = 1;
    private int maxPower = 1;
    private int refineTicks = 0;
    private int updateTicks = 0;
    private int requiredTicks = 0;
    private RefineryRecipe recipe = null;
    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(7, ItemStack.EMPTY);
    private final PropertyDelegate delegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> power;
                case 1 -> maxPower;
                case 2 -> refineTicks;
                case 3 -> requiredTicks;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> power = value;
                case 1 -> maxPower = value;
                case 2 -> refineTicks = value;
                case 3 -> requiredTicks = value;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public RefineryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFINERY_ENTITY, pos, state);
    }

    @Override
    public boolean needsPower() {
        return getPower() < getMaxPower();
    }

    @Override
    public int powerRequest() {
        return recipe != null ? 10 : 1;
    }

    @Override
    public int getMaxPower() {
        return recipe != null ? recipe.powerCost : 1;
    }

    private boolean isRefining() {
        return recipe != null;
    }

    public RefineryRecipe getRecipe() {
        return recipe;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("power", power);
        nbt.putInt("max_power", maxPower);
        nbt.putInt("update_ticks", updateTicks);
        nbt.putInt("ticks", refineTicks);
        nbt.putInt("required_ticks", requiredTicks);

        if (world != null && !world.isClient) nbt.putString("recipe", getRecipeName());
    }

    private String getRecipeName() {
        if (recipe == null) return "none";

        Identifier id = recipe.getId();
        return id.toString();
    }

    private RefineryRecipe fromRecipeName(String name) {
        if (name.equals("none")) return null;

        assert world != null && !world.isClient;
        Optional optional = Objects.requireNonNull(world.getServer()).getRecipeManager().get(Identifier.tryParse(name));

        if (optional.isPresent() && optional.get() instanceof RefineryRecipe recipe) {
            return recipe;
        }
        return null;
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

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);

        power = nbt.getInt("power");
        maxPower = nbt.getInt("max_power");
        updateTicks = nbt.getInt("update_ticks");

        refineTicks = nbt.getInt("ticks");
        requiredTicks = nbt.getInt("required_ticks");

        if (world != null && !world.isClient) recipe = fromRecipeName(nbt.getString("recipe"));
    }

    public boolean updateRecipe(boolean reset) {
        if (recipe != null && (power < 1 || !canCraft())) {
            recipe = null;
            refineTicks = 0;
            requiredTicks = 0;
            power = Math.min(1, power);
            maxPower = 1;

            updateRefiningState();
            markDirty();

            return true;
        }

        if (this.world instanceof ServerWorld world) {
            Optional<RefineryRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.REFINERY, this, world);

            if (optional.isPresent() && recipe != optional.get()) {
                recipe = optional.get();
                requiredTicks = recipe.refineTime;
                maxPower = recipe.powerCost;

                if (reset) {
                    refineTicks = 0;
                    power = Math.min(1, power);

                    updateRefiningState();
                }
                markDirty();

                return true;
            } else if (optional.isEmpty()) {
                recipe = null;
                refineTicks = 0;
                requiredTicks = 0;
                power = Math.min(1, power);
                maxPower = 1;

                updateRefiningState();
                markDirty();

                return true;
            }
        }

        return false;
    }



    @Override
    public Text getDisplayName() {
        return Text.translatable("container.dndreams.refinery");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RefineryScreenHandler(syncId, inv, this, delegate);
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public boolean addPower(int amount) {
        updateTicks = 0;
        if (power >= getMaxPower()) {
            power = getMaxPower();
            return false;
        }
        power = Math.min(getMaxPower(), power + amount);
        markDirty();
        return true;
    }

    @Override
    public boolean canAfford(int amount) {
        return getPower() >= amount;
    }

    @Override
    public boolean usePower(int amount) {
        if (!canAfford(amount)) return false;
        power -= amount;
        return true;
    }

    @Override
    public void setPower(int amount) {
        power = MathHelper.clamp(amount, 0, getMaxPower());
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, RefineryBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    public void updateRefiningState() {
        if (world == null || world.isClient) return;

        if (isRefining() && !getCachedState().get(Properties.LIT)) {
            world.setBlockState(pos, getCachedState().with(Properties.LIT, true));
        } else if (!isRefining() && getCachedState().get(Properties.LIT)) {
            world.setBlockState(pos, getCachedState().with(Properties.LIT, false));
        }
    }

    private void tick(ServerWorld world) {

        if (recipe != null) {
            if (!recipe.matches(ImplementedInventory.of(inventory), world)) {
                updateRecipe(true);
                return;
            }

            if (needsPower()) {
                updateTicks++;
                markDirty();

                if (updateTicks > 5) {
                    power = Math.max(0, power - 10);
                    if (power < 1) {
                        updateRecipe(true);
                    }
                }
            } else {
                if (refineTicks++ >= recipe.refineTime) {
                    craft();
                }
                markDirty();
            }
        } else if (power > 0) {
            if (updateTicks++ >= 20) {
                updateTicks = 0;

                if (updateRecipe(true)) {
                    updateTicks = -30;
                }
            }
        }
    }

    private void craft() {
        refineTicks = 0;

        if (canCraft()) {
            int jars = recipe.jarsRequired(ImplementedInventory.of(inventory));

            if (jars > 0) {
                inventory.get(6).decrement(jars);
            } else if (jars < 0) {
                inventory.set(6, merge(inventory.get(6), new ItemStack(ModItems.AMETHYST_JAR, -jars)));
            }

            for (int i = 0; i < 4; i++) {
                if (!inventory.get(i).isEmpty()) inventory.get(i).decrement(1);
            }

            inventory.set(4, merge(inventory.get(4), recipe.output.copy()));
            if (!recipe.byproduct.isEmpty()) {
                inventory.set(5, merge(inventory.get(5), recipe.byproduct.copy()));
            }
        }

        recipe = null;
        updateRecipe(true);
    }


    private boolean canCraft() {
        return recipe != null && canMerge(inventory.get(4), recipe.output.copy()) && (recipe.byproduct.isEmpty() || canMerge(inventory.get(5), recipe.byproduct()));
    }

    private boolean canMerge(ItemStack stack, ItemStack merger) {
        if (stack.isEmpty()) return true;

        return stack.isItemEqualIgnoreDamage(merger) && stack.getCount() + merger.getCount() <= stack.getMaxCount();
    }

    private ItemStack merge(ItemStack stack, ItemStack merger) {
        if (!canMerge(stack, merger)) return ItemStack.EMPTY;

        if (stack.isEmpty()) return merger;

        ItemStack result = stack.copy();
        result.setCount(stack.getCount() + merger.getCount());

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if (slot == 6 && side != Direction.UP) {
            return stack.isOf(ModItems.AMETHYST_JAR);
        }

        return slot < 4 && side == Direction.UP;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return slot == 4 || slot == 5;
    }
}
