package net.eman3600.dndreams.items;

import net.eman3600.dndreams.items.interfaces.ManaCostItem;
import net.eman3600.dndreams.items.interfaces.PowerCostItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.Style;
import java.util.List;

public class SatiationStaffItem extends Item implements ManaCostItem, PowerCostItem {
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
        if (world instanceof ServerWorld && user instanceof PlayerEntity player && canAfford(stack, player)) {
            spendMana(player, stack);
            spendPower(player, stack);

            HungerManager manager = player.getHungerManager();

            manager.add(12, 1f);

            player.heal(3);

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
        return canAffordMana(player, stack) && canAffordPower(player, stack);
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
    public float getBasePowerCost() {
        return 10;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(getTooltipMana(stack));
        tooltip.add(getTooltipPower(world, stack));
    }
}
