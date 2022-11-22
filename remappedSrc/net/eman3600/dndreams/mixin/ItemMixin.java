package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow @Final private boolean fireproof;

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void blockBottler(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();

        PlayerEntity player = context.getPlayer();
        if (player != null) {
            if (world.getBlockState(pos).getBlock() == Blocks.END_PORTAL && stack.getItem() == Items.GLASS_BOTTLE) {
                if (world.getRegistryKey() == World.END) {
                    context.getPlayer().setStackInHand(context.getHand(), ItemUsage.exchangeStack(stack, context.getPlayer(), new ItemStack(ModItems.LIQUID_VOID)));
                } else {
                    player.sendMessage(Text.translatable("item.dndreams.liquid_void.wrong_dimension"), true);
                }

                info.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
