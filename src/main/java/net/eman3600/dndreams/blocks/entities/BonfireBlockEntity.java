package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.blocks.energy.BonfireBlock;
import net.eman3600.dndreams.initializers.basics.ModBlockEntities;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.recipes.RefineryRecipe;
import net.eman3600.dndreams.recipes.RitualRecipe;
import net.eman3600.dndreams.rituals.Ritual;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.eman3600.dndreams.blocks.energy.BonfireBlock.STRONG;

public class BonfireBlockEntity extends BlockEntity
        implements Clearable, ImplementedInventory {

    private int delayTicks = 0;
    private final DefaultedList<ItemStack> ritualItems = DefaultedList.ofSize(5, ItemStack.EMPTY);

    private RitualRecipe recipe = null;

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

                if (recipe == null) {
                    Optional<RitualRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.RITUAL, this, world);

                    optional.ifPresent(ritualRecipe -> recipe = ritualRecipe);
                }

                if (recipe != null) {

                    Ritual ritual = recipe.getRitual();

                    if (ritual.onCast(world, pos, this, recipe)) {

                        clearBase();
                    } else {

                        Box box = new Box(pos).expand(12f);
                        Text errorText = Text.translatable(ritual.getErrorTranslation(world, pos, this, recipe));

                        for (PlayerEntity player : world.getNonSpectatingEntities(PlayerEntity.class, box)) {
                            player.sendMessage(errorText, true);
                        }

                        removeItems();
                    }

                    recipe = null;
                }
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
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.delayTicks = nbt.getInt("DelayTicks");
        this.ritualItems.clear();
        Inventories.readNbt(nbt, this.ritualItems);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {

        return this.ritualItems;
    }

    @Override
    public void clear() {
        this.ritualItems.clear();

        this.recipe = null;

        if (getCachedState().get(STRONG)) {
            this.delayTicks = 0;
            world.setBlockState(pos, getCachedState().with(STRONG, false));
        }

        updateListeners();
    }

    public void clearBase() {
        for (int i = 0; i < 4; i++) {
            this.ritualItems.set(i, ItemStack.EMPTY);
        }
        updateListeners();
    }

    public DefaultedList<ItemStack> getRitualItems() {
        return ritualItems;
    }

    public int nextAvailableSlot() {
        for (int i = 0; i < 4; i++) {
            if (ritualItems.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public void removeItems() {

        if (world == null) return;

        ItemScatterer.spawn(world, pos, getRitualItems());

        clear();
    }

    public ItemStack getRitualFocus() {
        return ritualItems.get(4);
    }

    public void setRitualFocus(ItemStack stack) {
        setRitualItem(stack, 4);
        testForRitual();
    }

    public void setRitualItem(ItemStack stack, int slot) {
        this.ritualItems.set(slot, stack);
        testForRitual();
        updateListeners();
    }

    public void testForRitual() {

        if (world == null || world.isClient) return;

        Optional<RitualRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.RITUAL, this, world);

        if (optional.isPresent()) {

            this.recipe = optional.get();
            this.delayTicks = 100;

            this.world.setBlockState(pos, getCachedState().with(STRONG, true));
        }
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
        return nbtCompound;
    }
}
