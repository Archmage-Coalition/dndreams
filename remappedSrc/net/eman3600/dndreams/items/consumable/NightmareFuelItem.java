package net.eman3600.dndreams.items.consumable;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.items.DreamyItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NightmareFuelItem extends Item {
    private static final int MAX_USE_TIME = 32;
    private static final float TORMENT_INCREMENT = 20.0f;

    public NightmareFuelItem(Settings settings) {
        super(settings);
    }

    public static int getMaxUseTime() {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity) {
            EntityComponents.TORMENT.get(user).addTorment(TORMENT_INCREMENT);
        }


        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        DreamyItem.dreamTooltip(world, tooltip);
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }
}
