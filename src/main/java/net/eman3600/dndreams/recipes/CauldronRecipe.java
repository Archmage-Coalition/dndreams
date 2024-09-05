package net.eman3600.dndreams.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.eman3600.dndreams.blocks.entities.RefinedCauldronBlockEntity;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CauldronRecipe implements Recipe<Inventory> {
    private final Identifier id;
    public final String group;
    public final ItemStack output;
    public final DefaultedList<Ingredient> input;
    public final boolean dreamOnly;
    public final boolean realOnly;
    public final ExtractionMethod method;
    public final int color;

    public CauldronRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input, ExtractionMethod method, boolean dreamOnly, boolean realOnly, int color) {
        this.id = id;
        this.group = group;
        this.output = output;
        this.input = input;
        this.method = method;
        this.dreamOnly = dreamOnly;
        this.realOnly = realOnly;
        this.color = color;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.CAULDRON_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CAULDRON;
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

    private static int itemsInside(Inventory inventory) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) count++;
        }
        return count;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if ((dreamOnly && world.getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY) || (realOnly && world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY)) return false;

        for (int i = 0; i < itemsInside(inventory); i++) {
            try {
                boolean bl = true;

                for (Ingredient ingredient : input) {
                    if (ingredient.test(inventory.getStack(i))) {
                        bl = false;
                        break;
                    }
                }

                if (bl) {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }

        return true;
    }

    public boolean fullyMatches(Inventory inventory, World world) {
        return matches(inventory, world) && itemsInside(inventory) == input.size();
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return output.copy();
    }

    public boolean fits(int width, int height) {
        return width * height >= this.input.size();
    }

    public static class Serializer implements RecipeSerializer<CauldronRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "cauldron";

        public Serializer() {
        }



        public CauldronRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));

            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for cauldron recipe");
            } else if (defaultedList.size() > 9) {
                throw new JsonParseException("Too many ingredients for cauldron recipe");
            } else {
                ItemStack itemStack = RefineryRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));


                boolean dream = JsonHelper.getBoolean(jsonObject, "dream_only", false);
                boolean real = JsonHelper.getBoolean(jsonObject, "real_only", false);
                if (dream && real) {
                    throw new JsonParseException("Cannot be both dream only AND real only");
                }
                ExtractionMethod method = ExtractionMethod.fromString(JsonHelper.getString(jsonObject, "method", "none"));
                int color = JsonHelper.getInt(jsonObject, "color", RefinedCauldronBlockEntity.WATER_COLOR);
                return new CauldronRecipe(identifier, string, itemStack, defaultedList, method, dream, real, color);
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

        public CauldronRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for(int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            ExtractionMethod method = ExtractionMethod.fromString(packetByteBuf.readString());
            boolean dream = packetByteBuf.readBoolean();
            boolean real = packetByteBuf.readBoolean();
            int color = packetByteBuf.readInt();
            return new CauldronRecipe(identifier, string, itemStack, defaultedList, method, dream, real, color);
        }

        public void write(PacketByteBuf packetByteBuf, CauldronRecipe recipe) {
            packetByteBuf.writeString(recipe.group);
            packetByteBuf.writeVarInt(recipe.input.size());

            for (Ingredient ingredient : recipe.input) {
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(recipe.output);
            packetByteBuf.writeString(recipe.method.asString());
            packetByteBuf.writeBoolean(recipe.dreamOnly);
            packetByteBuf.writeBoolean(recipe.realOnly);
            packetByteBuf.writeInt(recipe.color);
        }
    }
}