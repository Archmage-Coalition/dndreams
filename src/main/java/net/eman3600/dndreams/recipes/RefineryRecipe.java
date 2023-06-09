package net.eman3600.dndreams.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RefineryRecipe implements Recipe<Inventory> {
    private final Identifier id;
    public final String group;
    public final ItemStack output;
    public final ItemStack byproduct;
    public final DefaultedList<Ingredient> input;
    public final int refineTime;
    public final int powerCost;
    public final boolean dreamOnly;

    public RefineryRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, ItemStack byproduct, int refineTime, int powerCost, boolean dreamOnly) {
        this.id = id;
        this.group = group;
        this.output = output;
        this.input = input;
        this.byproduct = byproduct;
        this.refineTime = refineTime;
        this.powerCost = powerCost;
        this.dreamOnly = dreamOnly;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.REFINERY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.REFINERY;
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

    public int jarsRequired(Inventory inventory) {
        int jars = 0;

        if (output.getItem().getRecipeRemainder() == ModItems.AMETHYST_JAR) jars += output.getCount();
        if (!byproduct.isEmpty() && byproduct.getItem().getRecipeRemainder() == ModItems.AMETHYST_JAR) jars += byproduct.getCount();

        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).getItem().getRecipeRemainder() == ModItems.AMETHYST_JAR) jars--;
        }

        return jars;
    }

    public int jarsRequired() {
        int jars = 0;

        if (output.getItem().getRecipeRemainder() == ModItems.AMETHYST_JAR) jars += output.getCount();
        if (!byproduct.isEmpty() && byproduct.getItem().getRecipeRemainder() == ModItems.AMETHYST_JAR) jars += byproduct.getCount();

        main: for (Ingredient ingredient : input) {
            ItemStack[] stacks = ingredient.getMatchingStacks();

            for (ItemStack stack : stacks) {
                if (stack.getItem().getRecipeRemainder() != ModItems.AMETHYST_JAR) {
                    continue main;
                }
            }

            jars--;
        }

        return jars;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if (dreamOnly && world.getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY) return false;

        int jars = jarsRequired(inventory);
        int jarsPresent = inventory.getStack(6).isOf(ModItems.AMETHYST_JAR) ? inventory.getStack(6).getCount() : 0;

        if (jarsPresent < jars || jarsPresent > 64 + jars) return false;

        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for(int j = 0; j < 4; ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.addInput(itemStack, 1);
            }
        }

        return i == this.input.size() && recipeMatcher.match(this, null);
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return output.copy();
    }

    public ItemStack byproduct() {
        return byproduct.copy();
    }

    public boolean fits(int width, int height) {
        return width * height >= this.input.size();
    }

    public static class Serializer implements RecipeSerializer<RefineryRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "refinery";

        public Serializer() {
        }



        public RefineryRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));

            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for refining recipe");
            } else if (defaultedList.size() > 4) {
                throw new JsonParseException("Too many ingredients for refining recipe");
            } else {
                ItemStack itemStack = outputFromJson(JsonHelper.getObject(jsonObject, "result"));
                ItemStack byproduct = outputFromJson(JsonHelper.getObject(jsonObject, "byproduct", null));

                int time = JsonHelper.getInt(jsonObject, "refine_time", 200);
                int cost = Math.max(JsonHelper.getInt(jsonObject, "cost", 100), 25);
                boolean dream = JsonHelper.getBoolean(jsonObject, "dream_only", false);

                return new RefineryRecipe(identifier, string, itemStack, defaultedList, byproduct, time, cost, dream);
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

        public RefineryRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for(int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            ItemStack byproduct = packetByteBuf.readItemStack();
            int time = packetByteBuf.readInt();
            int cost = packetByteBuf.readInt();
            boolean dream = packetByteBuf.readBoolean();
            return new RefineryRecipe(identifier, string, itemStack, defaultedList, byproduct, time, cost, dream);
        }

        public void write(PacketByteBuf packetByteBuf, RefineryRecipe recipe) {
            packetByteBuf.writeString(recipe.group);
            packetByteBuf.writeVarInt(recipe.input.size());

            for (Ingredient ingredient : recipe.input) {
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(recipe.output);
            packetByteBuf.writeItemStack(recipe.byproduct);
            packetByteBuf.writeInt(recipe.refineTime);
            packetByteBuf.writeInt(recipe.powerCost);
            packetByteBuf.writeBoolean(recipe.dreamOnly);
        }
    }

    public static ItemStack outputFromJson(JsonObject json) {
        if (json == null) return ItemStack.EMPTY;

        Item item = getItem(json);
        if (json.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JsonHelper.getInt(json, "count", 1);
            if (i < 1) {
                throw new JsonSyntaxException("Invalid icon count: " + i);
            } else {
                return new ItemStack(item, i);
            }
        }
    }

    public static Item getItem(JsonObject json) {
        String string = JsonHelper.getString(json, "item");
        Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + string);
        } else {
            return item;
        }
    }
}