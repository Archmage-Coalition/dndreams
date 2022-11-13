package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.properties.BrewStage;
import net.eman3600.dndreams.blocks.properties.BrewType;
import net.eman3600.dndreams.blocks.properties.CauldronState;
import net.eman3600.dndreams.blocks.renderer.RefinedCauldronBlockEntityRenderer;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.items.consumable.brew.AbstractBrewItem;
import net.eman3600.dndreams.mixin_interfaces.ItemEntityInterface;
import net.eman3600.dndreams.recipe.ApothecaryRecipe;
import net.eman3600.dndreams.recipe.CauldronRecipe;
import net.eman3600.dndreams.recipe.ExtractionMethod;
import net.eman3600.dndreams.util.ModTags;
import net.eman3600.dndreams.util.data.DistributionType;
import net.eman3600.dndreams.util.data.EnhancementEntry;
import net.eman3600.dndreams.util.data.EnhancementType;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.eman3600.dndreams.util.data.CapacityEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    private int powerRequirement = 0;
    private int spoilTicks = 0;
    private int brewTicks = 0;
    private int color = WATER_COLOR;
    private CauldronState cauldronState = CauldronState.IDLE;
    public final Box dropArea;


    public RefinedCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFINED_CAULDRON_ENTITY, pos, state);

        dropArea = new Box(.1, .6, .1, .9, .8, .9).offset(pos);
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
            } else if (cauldronState == CauldronState.BREWING) {
                if (canAfford(powerRequirement)) {
                    if (brewTicks++ > 30) cauldronState = CauldronState.FINISHED;
                } else if (spoilTicks++ > 5) {
                    cauldronState = CauldronState.IDLE;
                    power = 0;
                    powerRequirement = 0;
                    spoilTicks = 0;
                }

                markDirty();
                world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
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

    private void tickClient(ClientWorld world) {
        if (isBoiling()) {
            float fluidHeight = RefinedCauldronBlockEntityRenderer.HEIGHT[level];
            float width = 0.35f;

            if (getCauldronState() == CauldronState.CRAFTING || getCauldronState() == CauldronState.BREWING) {

                world.addParticle(new DustParticleEffect(new Vec3f(((getColor() >> 16) & 0xff) / 255f, ((getColor() >> 8) & 0xff) / 255f, (getColor() & 0xff) / 255f), 1), pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), 0, 0, 0);
            } else if (getCauldronState() == CauldronState.FINISHED) {

                world.addParticle(ModParticles.CAULDRON_SPARKLE, pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -width, width), ((getColor() >> 16) & 0xff) / 255f, ((getColor() >> 8) & 0xff) / 255f, (getColor() & 0xff) / 255f);
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

        if ((itemList().contains(stack.getItem()) && !stack.isOf(Items.FERMENTED_SPIDER_EYE)) || ingredients >= inventory.size() || stack.getItem() instanceof BucketItem) return;

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
                } else {
                    randomizeColor(world);
                }
            } else {
                cauldronState = CauldronState.RUINED;
                color = RUINED_COLOR;
            }
        } else if (getBrewType() == BrewType.BREW) {

            if (getBrewStage() == BrewStage.BASE) {
                color = 0x133bad;
                return;
            } else if (getBrewStage() == BrewStage.CAPACITY) {
                alterColor(-12, -12, -12);
                return;
            } else if (getBrewStage() == BrewStage.EFFECT) {
                if (inventory.get(ingredients - 1).isOf(Items.FERMENTED_SPIDER_EYE) && BrewStage.fromStack(inventory.get(ingredients - 2), world) != BrewStage.EFFECT) {
                    cauldronState = CauldronState.RUINED;
                    color = RUINED_COLOR;
                    return;
                }
                color = getMixColor();
                return;
            } else if (getBrewStage() == BrewStage.ENHANCEMENT) {
                EnhancementType enhancementType = ENHANCEMENTS.get(inventory.get(ingredients - 1).getItem()).type;
                if (enhancementType == EnhancementType.LENGTH) {
                    alterColor(30, 0, 0);
                } else if (enhancementType == EnhancementType.AMPLIFIER) {
                    alterColor(20, 30, 0);
                }
                return;
            } else if (getBrewStage() == BrewStage.DISTRIBUTION) {
                DistributionType distributionType;
                try {
                    distributionType = DistributionType.asMap().get(inventory.get(ingredients - 1).getItem());
                } catch (Exception e) {
                    cauldronState = CauldronState.RUINED;
                    color = RUINED_COLOR;
                    return;
                }

                if (distributionType == DistributionType.SPLASH) {
                    alterColor(-20, -20, -20);
                } else if (distributionType == DistributionType.LINGERING) {
                    alterColor(20, 20, 20);
                } else if (distributionType == DistributionType.LIQUID) {
                    alterColor(-20, -20, 60);
                } else if (distributionType == DistributionType.GAS) {
                    alterColor(-20, 60, -10);
                }
                return;
            }

            cauldronState = CauldronState.RUINED;
            color = RUINED_COLOR;
        }
    }

    public void randomizeColor(ServerWorld world) {
        try {
            color = world.random.nextInt() & (0xFFFFFF);
        } catch (NullPointerException ignored) {}
    }

    public void alterColor(int r, int g, int b) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;

        red = MathHelper.clamp(red + r, 0, 0xFF);
        green = MathHelper.clamp(green + g, 0, 0xFF);
        blue = MathHelper.clamp(blue + b, 0, 0xFF);

        color = (red << 16) | (green << 8) | (blue);
    }

    public int getMixColor() {
        List<StatusEffectInstance> list = new ArrayList<>();

        for (int i = 0; i < ingredients; i++) {
            DefaultedList<ItemStack> stacks = DefaultedList.ofSize(2, ItemStack.EMPTY);
            stacks.set(0, inventory.get(i));

            try {
                if (inventory.get(i + 1).isOf(Items.FERMENTED_SPIDER_EYE)) {
                    stacks.set(1, inventory.get(i + 1));
                }
            } catch (IndexOutOfBoundsException ignored) {}

            Optional<ApothecaryRecipe> optional = world.getRecipeManager().getFirstMatch(ModRecipeTypes.APOTHECARY, ImplementedInventory.of(stacks), world);

            if (optional.isPresent()) {
                ApothecaryRecipe recipe = optional.get();

                list.add(new StatusEffectInstance(recipe.effect));
            }
        }

        return PotionUtil.getColor(list);
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
        return powerRequirement;
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
        powerRequirement = 0;

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
        } else if (getBrewType() == BrewType.BREW) {

            if (!bottler.isOf(Items.GLASS_BOTTLE)) return ItemStack.EMPTY;

            DistributionType distribution;
            try {
                distribution = DistributionType.asMap().get(inventory.get(ingredients - 1).getItem());

                if (distribution == null) {
                    distribution = DistributionType.INGESTED;
                }
            } catch (NoSuchElementException e) {
                distribution = DistributionType.INGESTED;
            }

            if (distribution.brewItem instanceof AbstractBrewItem item) {
                int drainage = distribution.levels;
                ItemStack result = item.fromBrewing(inventory, world);
                NbtCompound nbt = result.getNbt();

                int tier = nbt.getInt("amplifier") + nbt.getInt("length");
                int rank = PotionUtil.getCustomPotionEffects(nbt).size();

                if ((tier > 3 && rank > 1) || tier > 5) drainage++;


                level -= drainage;
                if (level <= 0) reset(world);

                world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
                return result;
            }
        }

        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        return ItemStack.EMPTY;
    }

    public void beginStir() {
        cauldronState = CauldronState.BREWING;

        spoilTicks = -30;
        brewTicks = 0;

        powerRequirement = getBrewPowerRequirement();
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

        powerRequirement = getBrewPowerRequirement();
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

    public int getBrewPowerRequirement() {
        if (getBrewType() == BrewType.BREW) {
            int requirement = 0;

            for (int i = 0; i < ingredients; i++) {
                ItemStack stack = inventory.get(i);
                Item item = stack.getItem();

                BrewStage itemStage = BrewStage.fromStack(stack, world);

                if (itemStage == BrewStage.CAPACITY) requirement += CAPACITIES.get(item).power;
                else if (itemStage == BrewStage.EFFECT && item != Items.FERMENTED_SPIDER_EYE) {
                    Inventory inv = ImplementedInventory.of(DefaultedList.ofSize(2, ItemStack.EMPTY));
                    try {
                        inv.setStack(0, stack);
                        if (inventory.get(i + 1).isOf(Items.FERMENTED_SPIDER_EYE)) {
                            inv.setStack(1, inventory.get(i + 1));
                        }
                    } catch (IndexOutOfBoundsException ignored) {}

                    Optional<ApothecaryRecipe> optional = world.getRecipeManager().getFirstMatch(ModRecipeTypes.APOTHECARY,
                            inv, world);
                    if (optional.isPresent()) {
                        requirement += optional.get().power;
                    }
                }
                else if (itemStage == BrewStage.ENHANCEMENT) requirement += ENHANCEMENTS.get(item).power;
            }

            return requirement;
        }

        return 0;
    }

    public BrewStage getBrewStage() {
        if (getBrewType() == BrewType.BREW) {
            int capacity = 3;
            BrewStage stage = BrewStage.BASE;

            for (int i = 0; i < ingredients; i++) {
                ItemStack stack = inventory.get(i);
                Item item = stack.getItem();

                BrewStage itemStage = BrewStage.fromStack(stack, world);
                HashMap<BrewStage, Integer> map = BrewStage.toStepMap();

                if (itemStage == BrewStage.CAPACITY) capacity += CAPACITIES.get(item).capacity;
                else if (itemStage == BrewStage.EFFECT && item != Items.FERMENTED_SPIDER_EYE) {
                    Inventory inv = ImplementedInventory.of(DefaultedList.ofSize(2, ItemStack.EMPTY));
                    try {
                        inv.setStack(0, stack);
                        if (inventory.get(i + 1).isOf(Items.FERMENTED_SPIDER_EYE)) {
                            inv.setStack(1, inventory.get(i + 1));
                        }
                    } catch (IndexOutOfBoundsException ignored) {}

                    Optional<ApothecaryRecipe> optional = world.getRecipeManager().getFirstMatch(ModRecipeTypes.APOTHECARY,
                            inv, world);
                    if (optional.isPresent()) {
                        capacity -= optional.get().capacity;
                    }
                }
                else if (itemStage == BrewStage.ENHANCEMENT) capacity--;
                else if (itemStage == BrewStage.DISTRIBUTION) capacity -= DistributionType.asMap().get(item).capacity;

                if (itemStage == stage && stage != BrewStage.DISTRIBUTION) continue;

                if (itemStage != null && map.get(itemStage) == map.get(stage) + 1) {
                    stage = itemStage;
                    continue;
                } else if (itemStage == BrewStage.EFFECT && stage == BrewStage.BASE) {
                    stage = itemStage;
                    continue;
                } else if (itemStage == BrewStage.DISTRIBUTION && stage == BrewStage.EFFECT) {
                    stage = itemStage;
                    continue;
                }

                return null;
            }

            return capacity >= 0 ? stage : null;
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

    public boolean isBrewDone() {
        BrewStage stage = getBrewStage();

        return stage != null && stage != BrewStage.BASE && stage != BrewStage.CAPACITY;
    }



    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return false;
    }
}
