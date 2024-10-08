package net.eman3600.dndreams.screens;

import com.google.common.collect.Lists;
import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.initializers.event.ModScreenHandlerTypes;
import net.eman3600.dndreams.recipes.WeavingRecipe;
import net.eman3600.dndreams.screens.slot.WeavingResultSlot;
import net.eman3600.dndreams.util.inventory.WeavingInventory;
import net.eman3600.dndreams.util.inventory.WeavingResultInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class WeavingScreenHandler extends ScreenHandler {

    private final WeavingInventory input;
    private final WeavingResultInventory result;
    private final ScreenHandlerContext context;
    private final Property selectedRecipe = Property.create();
    private final PlayerEntity player;
    private List<WeavingRecipe> availableRecipes = Lists.newArrayList();
    private float sanityCost = 0;
    public boolean clientDirty = false;
    private Runnable contentsChangedListener = () -> {};

    public WeavingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public WeavingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlerTypes.WEAVING, syncId);
        this.input = new WeavingInventory(this);
        this.result = new WeavingResultInventory();
        this.context = context;
        this.player = playerInventory.player;

        this.addSlot(new WeavingResultSlot(playerInventory.player, this.input, this.result, this, 0, 143, 49));

        int i;
        int j;
        this.addSlot(new Slot(this.input, 0, 20, 13));
        this.addSlot(new Slot(this.input, 1, 20, 33));
        this.addSlot(new Slot(this.input, 2, 20, 51));

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addProperty(this.selectedRecipe);
        this.selectedRecipe.set(-1);
        onContentChanged(input);
    }


    private void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedRecipe.get())) {
            WeavingRecipe recipe = this.availableRecipes.get(this.selectedRecipe.get());
            result.setLastRecipe(recipe);
            result.setStack(0, recipe.craft(this.input));
            sanityCost = recipe.sanityCost;
        } else {
            result.setStack(0, ItemStack.EMPTY);
            sanityCost = 0;
        }
        this.sendContentUpdates();
        clientDirty = true;
    }

    public boolean canCraft() {
        return !this.availableRecipes.isEmpty();
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
            this.populateResult();
        }
        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {

        WeavingRecipe current = selectedRecipe.get() >= 0 ? availableRecipes.get(selectedRecipe.get()) : null;

        this.availableRecipes.clear();
        this.selectedRecipe.set(-1);
        this.result.setStack(0, ItemStack.EMPTY);
        this.availableRecipes = player.world.getRecipeManager().getAllMatches(ModRecipeTypes.WEAVING, input, player.world);

        if (current != null && availableRecipes.contains(current)) {
            selectedRecipe.set(availableRecipes.indexOf(current));

            populateResult();
        } else {
            sanityCost = 0;
        }
    }

    public boolean matches(Recipe<? super WeavingInventory> recipe) {
        return recipe.matches(this.input, this.player.world);
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.input);
        });
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ModScreenHandlerTypes.WEAVING;
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.DREAM_TABLE);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            Item item = itemStack2.getItem();
            itemStack = itemStack2.copy();
            if (index == 0) {
                boolean failure = true;

                while (trulyMatches()) {

                    itemStack2 = itemStack.copy();

                    if (this.insertItem(itemStack2, 4, 40, true)) {
                        failure = false;
                        slot.onTakeItem(player, itemStack);
                        slot.onQuickTransfer(itemStack2, itemStack);
                        item.onCraft(itemStack, player.world, player);
                    } else {
                        break;
                    }
                }

                if (failure) return ItemStack.EMPTY;
                else {
                    slot.markDirty();
                    this.sendContentUpdates();
                    updateAfterCraft();
                    return itemStack;
                }

            } else if (index < 4 ? !this.insertItem(itemStack2, 4, 40, false) : !this.insertItem(itemStack2, 1, 4, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            slot.markDirty();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
            this.sendContentUpdates();
        }
        return itemStack;
    }

    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    public Runnable getContentsChangedListener() {
        return contentsChangedListener;
    }

    public int getSelectedRecipe() {
        return selectedRecipe.get();
    }

    public List<WeavingRecipe> getAvailableRecipes() {
        return availableRecipes;
    }

    public boolean canPlayerAffordSanity(float cost) {
        return EntityComponents.TORMENT.get(player).canFullyAfford(cost);
    }

    public boolean trulyMatches() {
        int recipe = selectedRecipe.get();
        return isInBounds(recipe) && availableRecipes.get(recipe).trulyMatches(input, player.world);
    }

    public void updateAfterCraft() {
        onContentChanged(input);
    }

    public float getSanityCost() {
        return sanityCost;
    }

    public boolean showsOutput() {
        return !result.isEmpty();
    }

    public List<Ingredient> getMissingIngredients() {

        List<Ingredient> list = new ArrayList<>(2);

        if (getSelectedRecipe() >= 0) {
            WeavingRecipe recipe = availableRecipes.get(getSelectedRecipe());

            for (Ingredient ingredient : recipe.input) {
                if (!isInputPresent(ingredient)) {
                    list.add(ingredient);
                }
            }
        }

        return list;
    }

    public WeavingRecipe getCurrentRecipe() {
        if (getSelectedRecipe() >= 0) {
            return availableRecipes.get(getSelectedRecipe());
        } else {
            return null;
        }
    }

    /**
     * Determines the state of the slots in regard to the current recipe:
     * Ingredient 2 Missing, Ingredient 1 Missing, Slot 2 Match, Slot 1 Match, Slot 2 Present, Slot 1 Present
     * @return A bitmask representing the recipe state of the input.
     */
    public int getInputSlotStates(WeavingRecipe recipe) {
        int mask = 0;

        if (!input.getStack(1).isEmpty()) mask |= 0b000001;
        if (!input.getStack(2).isEmpty()) mask |= 0b000010;

        Ingredient ingredient;

        if (recipe.input.size() > 0) {
            ingredient = recipe.input.get(0);

            if (ingredient.test(input.getStack(1))) {
                mask |= 0b000100;
            } else if (ingredient.test(input.getStack(2))) {
                mask |= 0b001000;
            } else {
                mask |= 0b010000;
            }
        }

        if (recipe.input.size() > 1) {
            ingredient = recipe.input.get(1);

            if (ingredient.test(input.getStack(1))) {
                mask |= 0b000100;
            } else if (ingredient.test(input.getStack(2))) {
                mask |= 0b001000;
            } else {
                mask |= 0b100000;
            }
        }

        return mask;
    }

    private boolean isInputPresent(Ingredient ingredient) {
        if (ingredient.isEmpty()) return true;

        return ingredient.test(input.getStack(1)) || ingredient.test(input.getStack(2));
    }
}
