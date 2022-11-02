package net.eman3600.dndreams.blocks.entities;

import net.eman3600.dndreams.initializers.ModBlockEntities;
import net.eman3600.dndreams.initializers.ModRecipeTypes;
import net.eman3600.dndreams.recipe.SmokestackRecipe;
import net.eman3600.dndreams.screen.SmokestackScreenHandler;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SmokestackBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
        private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(2, ItemStack.EMPTY);

    public SmokestackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMOKESTACK_ENTITY, pos, state);
    }

    public void receiveBurn(DefaultedList<ItemStack> stacks) {
        Random random = world.random;
        if (random.nextInt(10) > 3) return;

        Inventory inv = new SimpleInventory(1);
        inv.setStack(0, stacks.get(0));

        Optional<SmokestackRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(ModRecipeTypes.SMOKESTACK, inv, world);

        if (optional.isPresent() && inventory.get(0).getCount() > 0) {
            craftRecipe(optional.get(), inv);
        }
    }

    private void craftRecipe(SmokestackRecipe recipe, Inventory inv) {
        if (recipe == null || !canAcceptRecipeOutput(recipe, recipe.getOutput().getMaxCount())) {
            return;
        }

        ItemStack jars = inventory.get(0);
        ItemStack output = recipe.craft(inv);
        ItemStack results = inventory.get(1);

        if (results.isEmpty()) {
            inventory.set(1, output.copy());
        } else if (results.isOf(output.getItem())) {
            results.increment(1);
        }

        jars.decrement(1);
    }

    private boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, int count) {
        if (inventory.get(0).isEmpty() || recipe == null) {
            return false;
        }
        ItemStack itemStack = recipe.getOutput();
        if (itemStack.isEmpty()) {
            return false;
        }
        ItemStack itemStack2 = inventory.get(1);
        if (itemStack2.isEmpty()) {
            return true;
        }
        if (!itemStack2.isItemEqualIgnoreDamage(itemStack)) {
            return false;
        }
        if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
            return true;
        }
        return itemStack2.getCount() < itemStack.getMaxCount();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.dndreams.smokestack");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SmokestackScreenHandler(syncId, inv, ImplementedInventory.of(inventory));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
    }
}
