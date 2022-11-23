package net.eman3600.dndreams.items.consumable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class DrinkableItem extends Item {
    private final boolean hasGlint;
    private final StatusEffectInstance[] effects;


    public DrinkableItem(Settings settings, boolean hasGlint, StatusEffectInstance... effects) {
        super(settings);
        this.hasGlint = hasGlint;
        this.effects = effects;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (user instanceof PlayerEntity player) {
                user.setStackInHand(user.getActiveHand(), ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE), true));
            }

            for (StatusEffectInstance effect: effects) {
                user.addStatusEffect(new StatusEffectInstance(effect));
            }
        }

        return stack;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return hasGlint;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
