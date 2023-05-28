package net.eman3600.dndreams.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class RitualRecipe implements Recipe<Inventory> {

    private final Identifier id;
    final DefaultedList<Ingredient> input;
    final ItemStack icon;
    public final Identifier ritualID;

    public RitualRecipe(Identifier id, DefaultedList<Ingredient> input, ItemStack output, Identifier ritualID) {
        this.id = id;
        this.input = input;
        this.icon = output;
        this.ritualID = ritualID;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<RitualRecipe> getSerializer() {
        return ModRecipeTypes.RITUAL_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.RITUAL;
    }

    public String getGroup() {
        return "none";
    }

    public boolean matches(Inventory inventory, World world) {
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for(int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.addInput(itemStack, 1);
            }
        }

        return i == this.input.size() && recipeMatcher.match(this, null);
    }

    public ItemStack craft(Inventory inventory) {
        return this.icon.copy();
    }

    public boolean fits(int width, int height) {
        return width > 0 && height > 0;
    }

    public ItemStack getOutput() {
        return this.icon;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return input;
    }

    public AbstractRitual getRitual() {
        return RitualRegistry.REGISTRY.get(ritualID);
    }

    public static class Serializer implements RecipeSerializer<RitualRecipe> {

        public static final RitualRecipe.Serializer INSTANCE = new RitualRecipe.Serializer();
        public static final String ID = "ritual";

        public Serializer() {
        }

        public RitualRecipe read(Identifier identifier, JsonObject jsonObject) {
            DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            ItemStack itemStack = WeavingShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));

            Identifier ritual = Identifier.tryParse(jsonObject.get("ritual").getAsString());

            return new RitualRecipe(identifier, ingredients, itemStack, ritual);

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

        public RitualRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(i, Ingredient.EMPTY);

            for(int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            Identifier ritual = Identifier.tryParse(packetByteBuf.readString());

            return new RitualRecipe(identifier, ingredients, itemStack, ritual);
        }

        public void write(PacketByteBuf packetByteBuf, RitualRecipe recipe) {
            packetByteBuf.writeVarInt(recipe.input.size());

            for (Ingredient ingredient : recipe.input) {
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(recipe.icon);
            packetByteBuf.writeString(recipe.ritualID.toString());
        }
    }

}
