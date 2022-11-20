package net.eman3600.dndreams.items.consumable.permanent;

import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PanaceaItem extends Item {
    public PanaceaItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {

            TormentComponent component = EntityComponents.TORMENT.get(player);

            if (!component.isShielded() && !world.isClient) {
                component.shield(true);

                player.sendMessage(Text.translatable("item.dndreams.panacea.use"), true);

                if (!player.isCreative()) {
                    player.setStackInHand(user.getActiveHand(), ItemUsage.exchangeStack(stack, player, Items.GLASS_BOTTLE.getDefaultStack()));
                }
            }
        }

        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!EntityComponents.TORMENT.get(user).isShielded()) return ItemUsage.consumeHeldItem(world, user, hand);

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        try {
            if (world instanceof ClientWorldAccess access) {
                TormentComponent component = EntityComponents.TORMENT.get(access.getClient().player);

                if (component.isShielded()) {
                    tooltip.add(Text.translatable("item.dndreams.panacea.tooltip_after"));
                } else if (access.getClient().player.getInventory().contains(stack)) {
                    tooltip.add(Text.translatable("item.dndreams.panacea.tooltip_before"));
                }
            }
        } catch (NullPointerException ignored) {}
    }
}
