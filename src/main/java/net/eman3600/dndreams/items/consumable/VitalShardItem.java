package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.cardinal_components.RotComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class VitalShardItem extends TooltipItem {

    public final int cureAmount;
    public final int cooldown;

    public VitalShardItem(int cureAmount, int cooldown, Settings settings) {
        super(settings);
        this.cureAmount = cureAmount;
        this.cooldown = cooldown;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    private RotComponent getRot(LivingEntity entity) {
        return EntityComponents.ROT.get(entity);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        RotComponent rot = getRot(user);

        if (rot.getRot() > 0) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        RotComponent rot = getRot(user);
        if (!world.isClient && user instanceof PlayerEntity player && rot.getRot() > 0) {
            if (!player.isCreative()) {
                stack.decrement(1);
            }

            rot.healRot(cureAmount);
            player.getItemCooldownManager().set(this, cooldown);
        }

        return stack;
    }
}
