package net.eman3600.dndreams.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ApothecaryRecipe implements Recipe<Inventory> {

    private final Identifier id;
    public final String group;
    public final ItemStack input;
    public final boolean corrupted;
    public final int capacity;
    public final int power;
    public final StatusEffect effect;
    public final int duration;
    public final int maxAmplifier;

    public ApothecaryRecipe(Identifier id, String group, ItemStack input, boolean corrupted, int capacity, int power, StatusEffect effect, int duration, int maxAmplifier) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.corrupted = corrupted;
        this.capacity = capacity;
        this.power = power;
        this.effect = effect;
        this.duration = duration;
        this.maxAmplifier = maxAmplifier;
    }

    public Identifier getId() {
        return this.id;
    }

    public RecipeSerializer<ApothecaryRecipe> getSerializer() {
        return ModRecipeTypes.APOTHECARY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.APOTHECARY;
    }

    public String getGroup() {
        return this.group;
    }

    public boolean matches(Inventory inventory, World world) {
        try {
            boolean match = inventory.getStack(0).isItemEqualIgnoreDamage(input);

            return match && (corrupted == inventory.getStack(1).isIn(ModTags.CORRUPTORS));
        } catch (IndexOutOfBoundsException e) {
            return !corrupted && inventory.getStack(0).isItemEqualIgnoreDamage(input);
        }
    }

    public ItemStack craft(Inventory inventory) {
        return this.input.copy();
    }

    public boolean fits(int width, int height) {
        return width > 0 && height > 0;
    }

    public ItemStack getOutput() {
        return this.input;
    }

    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.ofSize(1, Ingredient.ofStacks(input));
    }

    public static class Serializer implements RecipeSerializer<ApothecaryRecipe> {

        public static final ApothecaryRecipe.Serializer INSTANCE = new ApothecaryRecipe.Serializer();
        public static final String ID = "apothecary";

        public Serializer() {
        }

        public ApothecaryRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            ItemStack stack = RefineryRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "ingredient"));
            if (stack.isIn(ModTags.CORRUPTORS)) throw new JsonParseException("Corruptors cannot be ingredients.");
            boolean corrupted = JsonHelper.getBoolean(jsonObject, "corrupted", false);
            int capacity = JsonHelper.getInt(jsonObject, "capacity", 2);
            int power = JsonHelper.getInt(jsonObject, "cost", 250);

            String potion = JsonHelper.getString(jsonObject, "effect", "minecraft:strength");
            Identifier id = Identifier.tryParse(potion);

            StatusEffect effect = Registry.STATUS_EFFECT.get(id);
            if (effect == null) {
                effect = StatusEffects.STRENGTH;
            }

            int duration = JsonHelper.getInt(jsonObject, "duration", 3600);
            int maxAmplifier = JsonHelper.getInt(jsonObject, "max_amplifier", 4);

            return new ApothecaryRecipe(identifier, string, stack, corrupted, capacity, power, effect, duration, maxAmplifier);

        }

        public ApothecaryRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String group = packetByteBuf.readString();
            ItemStack stack = packetByteBuf.readItemStack();
            boolean corrupted = packetByteBuf.readBoolean();
            int capacity = packetByteBuf.readInt();
            int power = packetByteBuf.readInt();

            StatusEffect effect = Registry.STATUS_EFFECT.get(Identifier.tryParse(packetByteBuf.readString()));
            if (effect == null) {
                effect = StatusEffects.STRENGTH;
            }

            int duration = packetByteBuf.readInt();
            int maxAmplifier = packetByteBuf.readInt();

            return new ApothecaryRecipe(identifier, group, stack, corrupted, capacity, power, effect, duration, maxAmplifier);
        }

        public void write(PacketByteBuf packetByteBuf, ApothecaryRecipe recipe) {
            packetByteBuf.writeString(recipe.group);
            packetByteBuf.writeItemStack(recipe.input);
            packetByteBuf.writeBoolean(recipe.corrupted);
            packetByteBuf.writeInt(recipe.capacity);
            packetByteBuf.writeInt(recipe.power);
            packetByteBuf.writeString(Registry.STATUS_EFFECT.getId(recipe.effect).toString());
            packetByteBuf.writeInt(recipe.duration);
            packetByteBuf.writeInt(recipe.maxAmplifier);
        }
    }

}
