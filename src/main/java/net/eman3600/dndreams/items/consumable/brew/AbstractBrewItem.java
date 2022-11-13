package net.eman3600.dndreams.items.consumable.brew;

import net.eman3600.dndreams.blocks.entities.RefinedCauldronBlockEntity;
import net.eman3600.dndreams.blocks.properties.BrewStage;
import net.eman3600.dndreams.initializers.event.ModRecipeTypes;
import net.eman3600.dndreams.recipe.ApothecaryRecipe;
import net.eman3600.dndreams.util.data.EnhancementEntry;
import net.eman3600.dndreams.util.data.EnhancementType;
import net.eman3600.dndreams.util.inventory.ImplementedInventory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBrewItem extends Item {
    public AbstractBrewItem(Settings settings) {
        super(settings);
    }

    @Override
    public abstract TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand);

    @Override
    public Text getName(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();

        List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(nbt);

        if (effects.size() == 1) {
            return Text.translatable(getTranslationKey(stack), Text.translatable(effects.get(0).getTranslationKey()));
        } else if (effects.size() == 2) {
            return Text.translatable(getTranslationKey(stack) + ".double", Text.translatable(effects.get(1).getTranslationKey()), Text.translatable(effects.get(0).getTranslationKey()));
        } else if (effects.size() > 2) {
            return Text.translatable(getTranslationKey(stack), Text.translatable("item.dndreams.brew.overloaded"));
        }

        return super.getName(stack);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {}

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        PotionUtil.buildTooltip(stack, tooltip, 1.0f);
    }

    public ItemStack fromBrewing(DefaultedList<ItemStack> inventory, World world) {
        ItemStack potion = getDefaultStack();
        NbtCompound nbt = new NbtCompound();

        int length = 0;
        int amplifier = 0;
        List<StatusEffectInstance> effects = new ArrayList<>();

        for (int i = inventory.size() - 1; i > 0; i--) {
            ItemStack stack = inventory.get(i);
            BrewStage stage = BrewStage.fromStack(stack, world);

            if (stage == BrewStage.ENHANCEMENT) {
                EnhancementType type = RefinedCauldronBlockEntity.ENHANCEMENTS.get(stack.getItem()).type;

                if (type == EnhancementType.AMPLIFIER) amplifier++;
                else if (type == EnhancementType.LENGTH) length++;
            } else if (stage == BrewStage.EFFECT && !stack.isOf(Items.FERMENTED_SPIDER_EYE)) {
                Inventory inv = ImplementedInventory.of(DefaultedList.ofSize(2, ItemStack.EMPTY));
                inv.setStack(0, stack);

                try {
                    inv.setStack(1, inventory.get(i + 1));
                } catch (IndexOutOfBoundsException ignored) {}

                Optional<ApothecaryRecipe> optional = world.getRecipeManager().getFirstMatch(ModRecipeTypes.APOTHECARY, inv, world);
                if (optional.isPresent()) {
                    ApothecaryRecipe recipe = optional.get();

                    StatusEffect effect = recipe.effect;
                    int duration = recipe.duration;

                    if (effect.isInstant()) duration = 1;
                    else if (effect.isBeneficial()) {
                        duration *= (Math.pow(2.5, length));
                    } else {
                        duration *= (Math.pow(1.5, length));
                    }

                    effects.add(new StatusEffectInstance(effect, duration, amplifier));
                }
            }
        }

        nbt.putInt("amplifier", amplifier);
        nbt.putInt("length", length);

        potion.setNbt(nbt);

        PotionUtil.setCustomPotionEffects(potion, effects);
        return potion;
    }
}
