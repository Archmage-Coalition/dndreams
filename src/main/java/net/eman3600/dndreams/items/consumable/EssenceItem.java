package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.TooltipItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class EssenceItem extends TooltipItem {

    private final int threads;

    public EssenceItem(int threads, Settings settings) {
        super(settings);
        this.threads = threads;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 30;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        TormentComponent torment = EntityComponents.TORMENT.get(user);

        if (torment.getMaxSanity() < TormentComponent.MAX_SANITY) {

            user.setCurrentHand(hand);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }

        return super.use(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (!world.isClient && user instanceof PlayerEntity player) {

            TormentComponent torment = EntityComponents.TORMENT.get(user);

            torment.lowerMaxSanity(-threads * TormentComponent.THREAD_VALUE);

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1f, 1);
            if (!player.isCreative()) stack.decrement(1);
        }

        return super.finishUsing(stack, world, user);
    }
}
