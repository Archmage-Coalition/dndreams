package net.eman3600.dndreams.items.misc_tool;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MindShearsItem extends Item {
    public MindShearsItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        TypedActionResult<ItemStack> result = EntityComponents.TORMENT.get(user).shearSanity(TormentComponent.THREAD_VALUE, true) ? TypedActionResult.consume(stack) : TypedActionResult.pass(stack);
        if (result.getResult() != ActionResult.PASS) {
            world.playSound(user, user.getBlockPos(), SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            user.getItemCooldownManager().set(this, 5);
            if (!user.isCreative()) stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
        }

        return result;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(ModItems.NIGHTMARE_FUEL);
    }
}
