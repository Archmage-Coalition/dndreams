package net.eman3600.dndreams.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.util.inventory.WeavingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WeavingRecipe implements Recipe<Inventory> {
    private final Identifier id;
    public final String group;
    public final ItemStack output;
    public final DefaultedList<Ingredient> input;
    public final Ingredient mold;
    public final float sanityCost;

    public WeavingRecipe(Identifier id, String group, ItemStack output, Ingredient mold, DefaultedList<Ingredient> input, float sanityCost) {
        this.id = id;
        this.group = group;
        this.output = output;
        this.input = input;
        this.mold = mold;
        this.sanityCost = sanityCost;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.WEAVING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.WEAVING;
    }

    @Override
    public boolean isEmpty() {
        return Recipe.super.isEmpty() && mold.isEmpty();
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getOutput() {
        return this.output;
    }



    public DefaultedList<Ingredient> getIngredients() {
        return this.input;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {

        return isEmpty() ? inventory.isEmpty() : matchesInput(inventory, world) || (!mold.isEmpty() && matchesMold(inventory, world));
    }

    public boolean matchesMold(Inventory inventory, World world) {

        return mold.test(inventory.getStack(0));
    }

    public boolean matchesInput(Inventory inventory, World world) {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for(int j = 0; j < 2; ++j) {
            ItemStack itemStack = inventory.getStack(j + 1);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.addInput(itemStack, 1);
            }
        }

        return i == this.input.size() && recipeMatcher.match(this, null);
    }

    public boolean matchesSanity(Inventory inventory, World world) {
        return inventory instanceof WeavingInventory inv && inv.canPlayerAffordSanity(sanityCost);
    }

    public boolean trulyMatches(Inventory inventory, World world) {
        return matchesMold(inventory, world) && matchesInput(inventory, world) && matchesSanity(inventory, world);
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        if (!mold.isEmpty()) {
            NbtCompound nbt = inventory.getStack(0).copy().getNbt();

            if (nbt != null) {
                nbt.remove("Damage");
                nbt.remove("RepairCost");

                ItemStack result = output.copy();
                result.setNbt(nbt);
                return result;
            }
        }
        return output.copy();
    }

    public boolean fits(int width, int height) {
        return width * height >= this.input.size();
    }

    public static class Serializer implements RecipeSerializer<WeavingRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "weaving";

        public Serializer() {
        }



        public WeavingRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));

            if (defaultedList.size() > 2) {
                throw new JsonParseException("Too many ingredients for refining recipe");
            } else {
                ItemStack itemStack = RefineryRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));

                float cost = MathHelper.clamp(JsonHelper.getFloat(jsonObject, "sanity_cost", 5), 0, 100);

                return new WeavingRecipe(identifier, string, itemStack, getMold(jsonObject), defaultedList, cost);
            }
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();

            for(int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (!ingredient.isEmpty()) {
                    defaultedList.add(ingredient);
                }
            }

            return defaultedList;
        }

        private static Ingredient getMold(JsonObject json) {

            if (json.has("mold")) return Ingredient.fromJson(json.get("mold"));
            else return Ingredient.EMPTY;
        }

        public WeavingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for(int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }

            Ingredient mold = Ingredient.fromPacket(packetByteBuf);

            ItemStack itemStack = packetByteBuf.readItemStack();
            float cost = packetByteBuf.readFloat();
            return new WeavingRecipe(identifier, string, itemStack, mold, defaultedList, cost);
        }

        public void write(PacketByteBuf packetByteBuf, WeavingRecipe recipe) {
            packetByteBuf.writeString(recipe.group);
            packetByteBuf.writeVarInt(recipe.input.size());

            for (Ingredient ingredient : recipe.input) {
                ingredient.write(packetByteBuf);
            }

            recipe.mold.write(packetByteBuf);

            packetByteBuf.writeItemStack(recipe.output);
            packetByteBuf.writeFloat(recipe.sanityCost);
        }
    }
}