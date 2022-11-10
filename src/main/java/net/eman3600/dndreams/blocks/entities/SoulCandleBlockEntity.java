package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.items.WaystoneItem;
import net.eman3600.dndreams.items.interfaces.RitualRemainItem;
import net.eman3600.dndreams.recipe.RitualRecipe;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.rituals.setup.AbstractRitual.CandleTuning;
import net.eman3600.dndreams.rituals.setup.AbstractRitual.Ring;
import net.eman3600.dndreams.rituals.setup.AbstractSustainedRitual;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.eman3600.dndreams.blocks.energy.RitualCandleBlock.LIT;

public class SoulCandleBlockEntity extends BlockEntity implements AbstractPowerReceiver {
    public int power;
    public AbstractRitual ritual = null;
    public DefaultedList<ItemStack> items = DefaultedList.ofSize(0);

    private BlockPos boundPos = pos;

    private final Box itemReach = new Box(-3, -1, -3, 3, 1, 3).offset(pos);
    private final Box playerReach = new Box(-5, -1, -5, 5, 1, 5).offset(pos);

    private int ticks = 0;
    private int ticksPowerless = 0;

    private boolean casting = false;
    private boolean sustained = false;


    public SoulCandleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUL_CANDLE_ENTITY, pos, state);
    }


    public static void tick(World world, BlockPos blockPos, BlockState blockState, SoulCandleBlockEntity entity) {
        try {
            ServerWorld server = (ServerWorld) world;
            if (world == null) throw new ClassCastException();

            entity.tick(server);
        } catch (ClassCastException ignored) {}
    }

    private void tick(ServerWorld world) {
        lit: if (getCachedState().get(LIT)) {
            if (!casting && !sustained) {

                if (identifyRitual(world, findRitualInventory(world)) && ringsMatch(false)) {
                    beginRitual(world);
                } else {
                    if (ritual != null) {
                        for (Ring ring: ritual.getRings()) {
                            for (BlockPos offset: ring.ringOffsets()) {
                                BlockPos candlePos = pos.add(offset);
                                Vec3d vec = Vec3d.ofCenter(candlePos);

                                if (!ring.tuning().matches(world, candlePos, false)) world.spawnParticles(ring.tuning().particle(), vec.x, vec.y, vec.z, 5, 0.1d, 0.1d, .1d, 0);
                            }
                        }

                        for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, playerReach)) {
                            player.sendMessage(Text.translatable("ritual.dndreams.error.rings"), true);
                        }
                    } else {
                        for (List<BlockPos> offsets: new List[] {Ring.INNER_RING, Ring.MIDDLE_RING, Ring.OUTER_RING}) {
                            for (BlockPos offset: offsets) {
                                BlockPos candlePos = pos.add(offset);
                                Vec3d vec = Vec3d.ofCenter(candlePos);

                                if (!CandleTuning.NONE.matches(world, candlePos, false)) world.spawnParticles(CandleTuning.NONE.particle(), vec.x, vec.y, vec.z, 5, 0.1d, 0.1d, .1d, 0);
                            }
                        }

                        for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, playerReach)) {
                            player.sendMessage(Text.translatable("ritual.dndreams.error.unknown"), true);
                        }
                    }
                    deactivate(true);
                }

            } else if (casting) {


                if (!ringsMatch(false)) {
                    failRitual();
                    break lit;
                }

                if (power >= getMaxPower()) {
                    if (ticks++ > 60) castRitual(world);
                } else {
                    ticksPowerless++;

                    if (ticksPowerless > 5) {
                        for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, playerReach)) {
                            player.sendMessage(Text.translatable("ritual.dndreams.error.powerless"), true);
                        }
                        failRitual();
                    }
                }

            } else if (ritual instanceof AbstractSustainedRitual susRite) {

                susRite.tickSustained(world, boundPos, this);

                if (ticks++ >= 20) {
                    ticks = 0;

                    if (!usePower(susRite.getSustainedCost()) || !ringsMatch(true)) {
                        endRitual(world);
                    }
                }
            }
        } else {
            if (casting || sustained) deactivate(false);
        }
    }

    public void setBoundPos(BlockPos boundPos) {
        this.boundPos = boundPos;
    }

    public boolean isCasting() {
        return casting;
    }

    public boolean isSustained() {
        return sustained;
    }

    @Override
    public boolean addPower(int amount) {
        if (power >= getMaxPower()) {
            power = getMaxPower();
            return false;
        }
        power = Math.min(getMaxPower(), power + amount);

        ticksPowerless = 0;
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
        if (casting && ritual != null) return ritual.cost();
        if (sustained && ritual instanceof AbstractSustainedRitual susRite) return susRite.getSustainedCost() * 3;
        return 0;
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
        return (getPower() < getMaxPower()) && (sustained || casting);
    }

    @Override
    public int powerRequest() {
        if (sustained && ritual instanceof AbstractSustainedRitual susRite && susRite.getSustainedCost() > 100) return susRite.getSustainedCost() / 20 + 1;
        return 5;
    }

    public Inventory getInv() {
        return getInv(items);
    }

    public Inventory getInv(List<ItemStack> items) {
        Inventory inv = new SimpleInventory(items.size());

        for (int i = 0; i < items.size(); i++) {
            inv.setStack(i, items.get(i));
        }

        return inv;
    }

    public void clearItems() {
        items = DefaultedList.ofSize(0);
    }

    public void deactivate(boolean extinguish) {
        if (world instanceof ServerWorld serverWorld) {
            if (extinguish) {
                world.setBlockState(pos, getCachedState().with(LIT, false));

                for (ServerPlayerEntity player: serverWorld.getPlayers()) {
                    serverWorld.playSound(player, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
                }
            }

            if (casting) failRitual();
            else endRitual(serverWorld);
        }
    }

    public void failRitual() {
        if (world instanceof ServerWorld) {
            ItemScatterer.spawn(world, pos, items);
            clearItems();

            power = 0;

            casting = false;
            sustained = false;

            ritual = null;
            ticks = 0;

            boundPos = pos;

            world.setBlockState(pos, getCachedState().with(LIT, false));
        }
    }

    public void scatterRemains(ServerWorld world) {
        List<ItemStack> remains = new ArrayList<>();

        for (ItemStack stack: items) {
            if (stack.getItem() instanceof RitualRemainItem item) {
                remains.add(item.getRitualRemains(ritual, this, stack));
            } else if (stack.getItem().hasRecipeRemainder()) {
                assert stack.getItem().getRecipeRemainder() != null;
                remains.add(stack.getItem().getRecipeRemainder().getDefaultStack());
            }
        }

        DefaultedList<ItemStack> finalList = DefaultedList.copyOf(ItemStack.EMPTY, remains.toArray(new ItemStack[0]));

        ItemScatterer.spawn(world, pos, finalList);
        clearItems();
    }

    private void castRitual(ServerWorld world) {
        boolean bl = ritual.onCast(world, boundPos, this);

        if (!bl) {
            world.setBlockState(pos, getCachedState().with(LIT, false));
            deactivate(false);
            for (PlayerEntity player: world.getNonSpectatingEntities(PlayerEntity.class, playerReach)) {
                player.sendMessage(Text.translatable("ritual.dndreams.error.improper"), true);
            }
            return;
        }

        casting = false;
        ticks = 0;

        if (ritual instanceof AbstractSustainedRitual) {
            sustained = true;
        } else {
            ritual = null;
            scatterRemains(world);

            world.setBlockState(pos, getCachedState().with(LIT, false));
        }

        power = getMaxPower();
    }

    public void beginRitual(ServerWorld world) {
        List<ItemEntity> entities = findRitualIngredients(world);

        boundPos = pos;

        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(entities.size(), ItemStack.EMPTY);

        for (int i = 0; i < entities.size(); i++) {
            ItemStack stack = entities.get(i).getStack().copy();
            stack.setCount(1);

            entities.get(i).getStack().decrement(1);
            if (entities.get(i).getStack().getCount() < 1) {
                entities.get(i).kill();
            }

            stacks.set(i, stack);
        }

        items = stacks;


        casting = true;
        ticks = 0;
        ticksPowerless = -30;

        for (Ring ring: ritual.getRings()) {
            for (BlockPos offset: ring.ringOffsets()) {
                if (world.getBlockEntity(pos.add(offset)) instanceof EchoCandleBlockEntity entity) {
                    entity.link(this);
                }
            }
        }
    }

    public void endRitual(ServerWorld world) {
        if (sustained && ritual instanceof AbstractSustainedRitual susRite) {
            susRite.onCease(world, boundPos, this);

            scatterRemains(world);
        }

        ritual = null;
        sustained = false;
        casting = false;
        power = 0;
        ticks = 0;
        ticksPowerless = 0;
        boundPos = pos;
        clearItems();
    }

    public boolean ringsMatch(boolean lit) {
        if (ritual != null) return ritual.matches(world, pos, lit);

        return false;
    }



    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("power", power);
        Inventories.writeNbt(nbt, items);
        nbt.putInt("ticks", ticks);
        nbt.putInt("ticks_powerless", ticksPowerless);

        try {
            nbt.putString("ritual", RitualRegistry.REGISTRY.getId(ritual).toString());
        } catch (NullPointerException e) {
            nbt.putString("ritual", "dndreams:none");
        }

        nbt.putBoolean("casting", casting);
        nbt.putBoolean("sustained", sustained);

        WaystoneItem.writeBoundPos(nbt, boundPos);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        power = nbt.getInt("power");
        Inventories.readNbt(nbt, items);
        ticks = nbt.getInt("ticks");
        ticksPowerless = nbt.getInt("ticks_powerless");

        ritual = RitualRegistry.REGISTRY.get(Identifier.tryParse(nbt.getString("ritual")));

        if (ritual != null) {
            casting = nbt.getBoolean("casting");
            sustained = nbt.getBoolean("sustained");
        } else {
            sustained = false;
            casting = false;
        }

        boundPos = WaystoneItem.readBoundPos(nbt, pos);
    }

    public boolean identifyRitual(ServerWorld world, Inventory inv) {
        Optional<RitualRecipe> recipe = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.RITUAL, inv, world);

        recipe.ifPresent(ritualRecipe -> ritual = ritualRecipe.getRitual());

        return ritual != null;
    }

    public Inventory findRitualInventory(ServerWorld world) {
        List<ItemStack> stacks = new ArrayList<>();

        for (ItemEntity entity: findRitualIngredients(world)) {
            stacks.add(entity.getStack());
        }

        return getInv(stacks);
    }

    public List<ItemEntity> findRitualIngredients(ServerWorld world) {
        return world.getEntitiesByClass(ItemEntity.class, itemReach, entity -> true);
    }
}
