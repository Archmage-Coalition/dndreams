package net.eman3600.dndreams.recipe;

import com.google.gson.JsonObject;
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

    public TransmutationRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.output = output;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<TransmutationRecipe> getSerializer() {
        return Serializer.INSTANCE;
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
        return this.output.copy();
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

            return new TransmutationRecipe(identifier, string, ingredient, itemStack);

        }

        public TransmutationRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String group = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();

            return new TransmutationRecipe(identifier, group, ingredient, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, TransmutationRecipe transmutationRecipe) {
            packetByteBuf.writeString(transmutationRecipe.group);
            transmutationRecipe.input.write(packetByteBuf);
            packetByteBuf.writeItemStack(transmutationRecipe.output);
        }
    }

}
