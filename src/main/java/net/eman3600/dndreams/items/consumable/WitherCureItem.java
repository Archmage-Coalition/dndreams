package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class WitherCureItem extends TooltipItem {

    public final int cooldown;

    public WitherCureItem(int cooldown, Settings settings) {
        super(settings);
        this.cooldown = cooldown;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 10;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.hasStatusEffect(StatusEffects.WITHER)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player && player.hasStatusEffect(StatusEffects.WITHER)) {
            if (!player.isCreative()) {
                stack.decrement(1);
            }

            player.removeStatusEffect(StatusEffects.WITHER);
            player.getItemCooldownManager().set(this, cooldown);
        }

        return stack;
    }
}
