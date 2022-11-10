package net.eman3600.dndreams.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SmokestackRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final DefaultedList<Ingredient> ingredients;
    private final ItemStack output;

    public SmokestackRecipe(Identifier id, Ingredient input, ItemStack output) {
        this.id = id;
        this.ingredients = DefaultedList.ofSize(1, Ingredient.EMPTY);
        ingredients.set(0, input);
        this.output = output;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<SmokestackRecipe> getSerializer() {
        return ModRecipeTypes.SMOKESTACK_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.SMOKESTACK;
    }

    public String getGroup() {
        return "none";
    }

    public boolean matches(Inventory inventory, World world) {
        try {
            return ingredients.get(0).test(inventory.getStack(0));
        } catch (Exception e) {
            return false;
        }
    }

    public ItemStack craft(Inventory inventory) {
        return this.output.copy();
    }

    public boolean fits(int width, int height) {
        return width > 0 && height > 0;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return ingredients;
    }

    public static class Serializer implements RecipeSerializer<SmokestackRecipe> {

        public static final SmokestackRecipe.Serializer INSTANCE = new SmokestackRecipe.Serializer();
        public static final String ID = "smokestack";

        public Serializer() {
        }

        public SmokestackRecipe read(Identifier identifier, JsonObject jsonObject) {
            JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(jsonElement);
            String string2 = JsonHelper.getString(jsonObject, "result");
            Identifier identifier2 = new Identifier(string2);
            ItemStack itemStack = new ItemStack(Registry.ITEM.getOrEmpty(identifier2).orElseThrow(() -> new IllegalStateException("Item: " + string2 + " does not exist")));

            return new SmokestackRecipe(identifier, ingredient, itemStack);
        }

        public SmokestackRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();

            return new SmokestackRecipe(identifier, ingredient, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, SmokestackRecipe recipe) {
            recipe.ingredients.get(0).write(packetByteBuf);
            packetByteBuf.writeItemStack(recipe.output);
        }
    }

}
