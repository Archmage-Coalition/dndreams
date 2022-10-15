package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void blockBottler(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();

        PlayerEntity player = context.getPlayer();
        if (player != null) {
            if (world.getBlockState(pos).getBlock() == Blocks.END_PORTAL && stack.getItem() == Items.GLASS_BOTTLE) {
                if (world.getDimensionKey() == DimensionTypes.THE_END) {
                    context.getPlayer().setStackInHand(context.getHand(), ItemUsage.exchangeStack(stack, context.getPlayer(), new ItemStack(ModItems.LIQUID_VOID)));
                } else {
                    player.sendMessage(Text.translatable("item.dndreams.liquid_void.wrong_dimension"), true);
                }

                info.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }


    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    public void entityBottler(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if (entity instanceof WardenEntity warden && user instanceof ServerPlayerEntity && stack.getItem() == Items.GLASS_BOTTLE && !user.getItemCooldownManager().isCoolingDown(Items.GLASS_BOTTLE)) {
            user.getItemCooldownManager().set(Items.GLASS_BOTTLE, 20);

            ItemStack soulStack = ItemUsage.exchangeStack(stack, user, ModItems.LIQUID_SOUL.getDefaultStack());
            user.setStackInHand(hand, soulStack);
            System.out.println(soulStack);

            warden.increaseAngerAt(user, 80, true);

            info.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
