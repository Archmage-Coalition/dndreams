package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

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

    @Inject(method = "appendTooltip", at = @At("TAIL"))
    private void dndreams$appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {

        if (stack.isIn(ModTags.MANA_BUFFERING_TOOLS)) {
            tooltip.add(Text.translatable("tooltip.dndreams.mana_buffer"));
        }
        if (stack.isIn(ModTags.SUNLIGHT_REPAIRING_TOOLS)) {
            tooltip.add(Text.translatable("tooltip.dndreams.sunlight_repair"));
        }
        if (stack.isIn(ModTags.INSANITY_REPAIRING_TOOLS)) {
            tooltip.add(Text.translatable("tooltip.dndreams.insanity_repair"));
        }
        if (stack.isIn(ModTags.GROUND_REPAIRING_TOOLS) || stack.isIn(ModTags.FAST_GROUND_REPAIRING_TOOLS)) {
            tooltip.add(Text.translatable("tooltip.dndreams.ground_repair"));
        }
    }
}
