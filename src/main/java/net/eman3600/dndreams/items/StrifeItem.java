package net.eman3600.dndreams.items;

import net.eman3600.dndreams.entities.projectiles.StrifeEntity;
import net.eman3600.dndreams.items.interfaces.BloodlustItem;
import net.eman3600.dndreams.items.magic_bow.MagicCrossbowItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StrifeItem extends TooltipItem {

    private static final int MAX_CHARGES = 3;

    public StrifeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);
        if (MagicCrossbowItem.isCharged(stack)) {

            int charges = MagicCrossbowItem.getCharges(stack);

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.NEUTRAL, 0.7f, 1.3f);

            if (!world.isClient) {

                StrifeEntity flame = new StrifeEntity(user, world);
                flame.setVelocity(user, Math.max(user.getPitch() - 7.5f, -90f), user.getYaw(), 0.0f, 1.4f, 0.3f);
                world.spawnEntity(flame);

                user.getItemCooldownManager().set(this, 5);
                if (!user.isCreative() && charges <= 1) stack.damage(1, user, e -> e.sendToolBreakStatus(hand));
            }

            MagicCrossbowItem.setCharges(stack, charges - 1);
            return TypedActionResult.success(stack);
        } else {

            if (!world.isClient) {

                user.getItemCooldownManager().set(this, 10);
                user.damage(BloodlustItem.CRIMSON_SACRIFICE, 4);
            }

            MagicCrossbowItem.setCharges(stack, MAX_CHARGES);
            return TypedActionResult.consume(stack);
        }
    }

    @Override
    public int getItemBarStep(ItemStack stack) {

        if (MagicCrossbowItem.isCharged(stack)) {
            int charges = MagicCrossbowItem.getCharges(stack);

            return charges * 13 / MAX_CHARGES;
        }

        return super.getItemBarStep(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {

        if (MagicCrossbowItem.isCharged(stack)) {
            return 0xd30037;
        }

        return super.getItemBarColor(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return super.isItemBarVisible(stack) || MagicCrossbowItem.isCharged(stack);
    }
}
