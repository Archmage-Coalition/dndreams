package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SanityThreadItem extends Item {
    public SanityThreadItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TormentComponent component = EntityComponents.TORMENT.get(user);
        if (component.getMaxSanity() < TormentComponent.MAX_SANITY) {
            component.lowerMaxSanity(-TormentComponent.THREAD_VALUE);
            ItemStack stack = user.getStackInHand(hand);

            world.playSound(user, user.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!user.isCreative()) stack.decrement(1);

            user.getItemCooldownManager().set(this, 5);
            return TypedActionResult.consume(stack);
        }

        return super.use(world, user, hand);
    }
}
