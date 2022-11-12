package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.properties.BrewStage;
import net.eman3600.dndreams.blocks.properties.BrewType;
import net.eman3600.dndreams.blocks.properties.CauldronState;
import net.eman3600.dndreams.blocks.renderer.RefinedCauldronBlockEntityRenderer;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.mixin_interfaces.ItemEntityInterface;
import net.eman3600.dndreams.recipe.CauldronRecipe;
import net.eman3600.dndreams.recipe.ExtractionMethod;
import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.util.data.EnhancementEntry;
import net.eman3600.dndreams.util.data.EnhancementType;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.eman3600.dndreams.util.data.CapacityEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RefinedCauldronBlockEntity extends BlockEntity implements AbstractPowerReceiver, ImplementedInventory {
    public static final int WATER_COLOR = 0x385DC6;
    public static final int RUINED_COLOR = 0xF800F8;

    public static final HashMap<Item, CapacityEntry> CAPACITIES = new HashMap<>();
    public static final HashMap<Item, EnhancementEntry> ENHANCEMENTS = new HashMap<>();

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
        } else if (!world.getBlockState(pos.down()).isIn(ModTags.HEAT_BLOCKS) && boilingTime > 1) boilingTime--;

        if (level > 1) {
            if (cauldronState == CauldronState.CRAFTING && brewTicks++ > 100) {
                Optional<CauldronRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.CAULDRON, this, world);

                if (optional.isPresent() && optional.get().fullyMatches(this, world)) {
                    CauldronRecipe recipe = optional.get();

                    Vec3d vec = Vec3d.ofCenter(pos.up());

                    ItemStack output = recipe.craft(this);
                    ItemEntity item = new ItemEntity(world, vec.x, vec.y, vec.z, output);
                    if (item instanceof ItemEntityInterface access) {
                        access.markUnbrewable();
                    }

                    world.spawnEntity(item);

                    reset(world);
                } else {
                    brewTicks = 0;
                }
            }


            List<ItemEntity> entities = world.getNonSpectatingEntities(ItemEntity.class, dropArea);

            for (ItemEntity itemEntity: entities) {
                ItemStack stack = itemEntity.getStack();

                if ((boilingTime >= 80 && cauldronState == CauldronState.IDLE) || stack.isOf(ModItems.WOOD_ASH) || stack.isOf(ModItems.ARCHFUEL)) {
                    attemptAdd(stack, itemEntity, world);
                    markDirty();
                    world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    public static void tickClient(World world, BlockPos blockPos, BlockState blockState, RefinedCauldronBlockEntity entity) {
        try {
            ClientWorld client = (ClientWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tickClient(client);
        } catch (ClassCastException ignored) {}
    }

    private void tickClient(ClientWorld client) {
        if (isBoiling()) {
            float fluidHeight = RefinedCauldronBlockEntityRenderer.HEIGHT[level];
            float width = 0.35f;

            if (getCauldronState() == CauldronState.CRAFTING) {

                world.addParticle(new DustParticleEffect(new Vec3f(((getColor() >> 16) & 0xff) / 255f, ((getColor() >> 8) & 0xff) / 255f, (getColor() & 0xff) / 255f), 1), pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), 0, 0, 0);
            }

            world.addParticle(ModParticles.CAULDRON_BUBBLE, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), ((getColor() >> 16) & 0xff) / 255f, ((getColor() >> 8) & 0xff) / 255f, (getColor() & 0xff) / 255f);
        }
    }

    private List<Item> itemList() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < ingredients; i++) items.add(inventory.get(i).getItem());
        return items;
    }

    private Inventory explodeable() {
        DefaultedList<ItemStack> stacks = DefaultedList.copyOf(ItemStack.EMPTY, inventory.toArray(new ItemStack[0]));

        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).getItem().hasRecipeRemainder()) {
                stacks.set(i, ItemStack.EMPTY);
            }
        }

        return ImplementedInventory.of(stacks);
    }

    private void attemptAdd(ItemStack stack, ItemEntity itemEntity, ServerWorld world) {
        if (inventory.get(0).isOf(ModItems.WOOD_ASH)) inventory.clear();
        if (itemEntity instanceof ItemEntityInterface access && access.isUnbrewable()) return;

        if (stack.isOf(ModItems.WOOD_ASH)) {
            reset(world);

            stack.decrement(1);
            if (stack.getCount() <= 0) itemEntity.kill();

            return;
        } else if (stack.isOf(ModItems.ARCHFUEL)) {
            ItemScatterer.spawn(world, pos.up(), explodeable());
            reset(world);

            stack.decrement(1);
            if (stack.getCount() <= 0) itemEntity.kill();

            return;
        }

        if (itemList().contains(stack.getItem()) || ingredients >= inventory.size() || stack.getItem() instanceof BucketItem) return;

        ItemStack addition = stack.copy();
        addition.setCount(1);

        if (addition.getItem().hasRecipeRemainder()) {
            ItemEntity remains = new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), addition.getItem().getRecipeRemainder().getDefaultStack());

            if (remains instanceof ItemEntityInterface access) {
                access.markUnbrewable();
            }

            world.spawnEntity(remains);
        }

        inventory.set(ingredients, addition);
        ingredients++;

        stack.decrement(1);
        if (stack.getCount() <= 0) itemEntity.kill();

        randomizeColor(world);
        if (getBrewType() == BrewType.CRAFT) {
            Optional<CauldronRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.CAULDRON, this, world);

            if (optional.isPresent()) {
                CauldronRecipe recipe = optional.get();

                if (recipe.fullyMatches(this, world)) {
                    color = recipe.color;

                    if (recipe.method == ExtractionMethod.NONE) {
                        cauldronState = CauldronState.CRAFTING;
                        brewTicks = 0;
                    }
                }
            } else {
                cauldronState = CauldronState.RUINED;
                color = RUINED_COLOR;
            }
        } else if (getBrewType() == BrewType.BREW) {
            if (BrewStage.fromStack(addition, world) == BrewStage.BASE && getBrewStage() == BrewStage.BASE) {
                color = 0x133bad;
                return;
            }

            cauldronState = CauldronState.RUINED;
            color = RUINED_COLOR;
        }
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

    public void reset(ServerWorld world) {
        for (int i = 0; i < ingredients; i++) {
            inventory.set(i, ModItems.WOOD_ASH.getDefaultStack());
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }

        ingredients = 0;
        power = 0;
        level = 0;
        boilingTime = 0;
        color = WATER_COLOR;
        cauldronState = CauldronState.IDLE;
        spoilTicks = 0;
        brewTicks = 0;

        markDirty();
    }

    public ItemStack take(ItemStack bottler, ServerWorld world) {
        markDirty();

        if (getBrewType() == BrewType.CRAFT) {
            Optional<CauldronRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.CAULDRON, this, world);

            if (optional.isPresent() && optional.get().fullyMatches(this, world)) {
                CauldronRecipe recipe = optional.get();

                if (!recipe.method.correctExtractor(bottler)) return ItemStack.EMPTY;

                ItemStack result = recipe.craft(this);
                int drainage = MathHelper.clamp(4 - result.getCount(), 1, 3);
                result.setCount(1);

                level -= drainage;
                if (level <= 0) reset(world);

                world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
                return result;
            }
        }

        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
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
        return boilingTime >= 80 && level > 0;
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
        if (inventory.get(0).isOf(ModItems.WOOD_ASH)) inventory.clear();

        if (inventory.get(0).isEmpty()) return BrewType.WATER;
        else if (cauldronState == CauldronState.RUINED) return BrewType.RUINED;
        else if (inventory.get(0).isOf(Items.NETHER_WART)) return BrewType.BREW;
        else return BrewType.CRAFT;
    }

    public BrewStage getBrewStage() {
        if (getBrewType() == BrewType.BREW) {
            if (ingredients == 1) return BrewStage.BASE;

        }

        return null;
    }


    public static void registerCapacityModifier(Item item, int capacity, int power) {
        if (CAPACITIES.containsKey(item)) return;

        CAPACITIES.put(item, new CapacityEntry(capacity, power));
    }

    public static void registerEnhancement(Item item, EnhancementType type, int power) {
        if (ENHANCEMENTS.containsKey(item)) return;

        ENHANCEMENTS.put(item, new EnhancementEntry(type, power));
    }



    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
}
