package net.eman3600.dndreams.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntList;
import net.eman3600.dndreams.initializers.ModRecipeTypes;
import net.eman3600.dndreams.util.inventory.WeavingInventory;
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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WeavingShapelessRecipe implements WeavingRecipe {
    private final Identifier id;
    final String group;
    final ItemStack output;
    final DefaultedList<Ingredient> input;
    final Ingredient weave_input;

    public WeavingShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, Ingredient weave_input) {
        this.id = id;
        this.group = group;
        this.output = output;
        this.input = input;
        this.weave_input = weave_input;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.WEAVING_SHAPELESS_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.WEAVING;
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
    public Ingredient getWeavingIngredient() {
        return this.weave_input;
    }

    public boolean matches(WeavingInventory craftingInventory, World world) {
        if (!weave_input.test(craftingInventory.getStack(9))) {
            return false;
        }

        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for(int j = 0; j < craftingInventory.size() - 1; ++j) {
            ItemStack itemStack = craftingInventory.getStack(j);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.addInput(itemStack, 1);
            }
        }

        return i == this.input.size() && recipeMatcher.match(this, (IntList)null);
    }

    public ItemStack craft(WeavingInventory craftingInventory) {
        return this.output.copy();
    }

    public boolean fits(int width, int height) {
        return width * height >= this.input.size();
    }

    public static class Serializer implements RecipeSerializer<WeavingShapelessRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "weaving_shapeless";

        public Serializer() {
        }



        public WeavingShapelessRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));

            JsonObject extra_ingredient = JsonHelper.getObject(jsonObject, "weave_item");

            Ingredient extra_input = Ingredient.fromJson(extra_ingredient);

            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (defaultedList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                ItemStack itemStack = WeavingShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
                return new WeavingShapelessRecipe(identifier, string, itemStack, defaultedList, extra_input);
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

        public WeavingShapelessRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for(int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }

            Ingredient extra_input = Ingredient.fromPacket(packetByteBuf);

            ItemStack itemStack = packetByteBuf.readItemStack();
            return new WeavingShapelessRecipe(identifier, string, itemStack, defaultedList, extra_input);
        }

        public void write(PacketByteBuf packetByteBuf, WeavingShapelessRecipe shapelessRecipe) {
            packetByteBuf.writeString(shapelessRecipe.group);
            packetByteBuf.writeVarInt(shapelessRecipe.input.size());

            for (Ingredient ingredient : shapelessRecipe.input) {
                ingredient.write(packetByteBuf);
            }

            shapelessRecipe.weave_input.write(packetByteBuf);
            packetByteBuf.writeItemStack(shapelessRecipe.output);
        }
    }
}