package net.eman3600.dndreams.recipe;

import com.google.gson.JsonObject;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.eman3600.dndreams.initializers.ModRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class TransmutationRecipe implements Recipe<Inventory> {

    private final Identifier id;
    final String group;
    final Ingredient input;
    final ItemStack output;
    private final boolean realOnly;
    private final boolean keepData;

    public TransmutationRecipe(Identifier id, String group, Ingredient input, ItemStack output, boolean realOnly, boolean keepData) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.output = output;
        this.realOnly = realOnly;
        this.keepData = keepData;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<TransmutationRecipe> getSerializer() {
        return ModRecipeTypes.TRANSMUTATION_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.TRANSMUTATION;
    }

    public String getGroup() {
        return this.group;
    }

    public boolean matches(Inventory inventory, World world) {
        int size = 0;
        boolean hasMatch = false;

        if (realOnly && world.getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            return false;
        }

        for(int j = 0; j < inventory.size(); j++) {
            ItemStack itemStack = inventory.getStack(j);
            if (!itemStack.isEmpty()) {
                size++;

                if (this.input.test(itemStack)) {
                    hasMatch = true;
                }
            }
        }

        return size == 1 && hasMatch;
    }

    public ItemStack craft(Inventory inventory) {
        if (!keepData) {
            return this.output.copy();
        } else {
            ItemStack newOutput = output.copy();
            newOutput.setNbt(inventory.getStack(0).getNbt());

            return newOutput;
        }
    }

    public boolean fits(int width, int height) {
        return width > 0 && height > 0;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.ofSize(1, this.input);
    }

    public static class Serializer implements RecipeSerializer<TransmutationRecipe> {

        public static final TransmutationRecipe.Serializer INSTANCE = new TransmutationRecipe.Serializer();
        public static final String ID = "transmutation";

        public Serializer() {
        }

        public TransmutationRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(jsonObject, "ingredient"));
            ItemStack itemStack = WeavingShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            boolean realOnly = JsonHelper.getBoolean(jsonObject, "real_only", false);
            boolean keepData = JsonHelper.getBoolean(jsonObject, "keep_data", false);

            return new TransmutationRecipe(identifier, string, ingredient, itemStack, realOnly, keepData);

        }

        public TransmutationRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String group = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            boolean realOnly = packetByteBuf.readBoolean();
            boolean keepData = packetByteBuf.readBoolean();

            return new TransmutationRecipe(identifier, group, ingredient, itemStack, realOnly, keepData);
        }

        public void write(PacketByteBuf packetByteBuf, TransmutationRecipe transmutationRecipe) {
            packetByteBuf.writeString(transmutationRecipe.group);
            transmutationRecipe.input.write(packetByteBuf);
            packetByteBuf.writeItemStack(transmutationRecipe.output);
            packetByteBuf.writeBoolean(transmutationRecipe.realOnly);
            packetByteBuf.writeBoolean(transmutationRecipe.keepData);
        }
    }

}
