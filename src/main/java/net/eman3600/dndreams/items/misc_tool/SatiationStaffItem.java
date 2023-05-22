package net.eman3600.dndreams.items.misc_tool;

import net.eman3600.dndreams.items.TooltipItem;
import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.items.interfaces.SanityCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SatiationStaffItem extends TooltipItem implements ManaCostItem, SanityCostItem {
    private static final int USE_TIME = 30;

    public SatiationStaffItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return USE_TIME;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player && canAfford(stack, player)) {
            spendMana(player, stack);
            spendSanity(player, stack);

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.7f, 1.3f);

            player.getItemCooldownManager().set(this, 7);
            if (!player.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(player.getActiveHand()));

            if (world instanceof ServerWorld) {
                HungerManager manager = player.getHungerManager();

                manager.add(12, 1f);

                player.heal(3);
            }

        }

        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (canAfford(stack, user)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
    }

    private boolean canAfford(ItemStack stack, PlayerEntity player) {
        return canAffordMana(player, stack) && canAffordSanity(player, stack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getBaseManaCost() {
        return 25;
    }

    @Override
    public float getBaseSanityCost() {
        return 20;
    }

    @Override
    public boolean isPermanent(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isOptional(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(getTooltipMana(stack));
        tooltip.add(getTooltipSanity(stack));
    }
}
