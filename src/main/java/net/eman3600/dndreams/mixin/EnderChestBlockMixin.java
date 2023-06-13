package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin extends AbstractChestBlock<EnderChestBlockEntity> {

    protected EnderChestBlockMixin(Settings settings, Supplier<BlockEntityType<? extends EnderChestBlockEntity>> blockEntityTypeSupplier) {
        super(settings, blockEntityTypeSupplier);
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void dndreams$onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world != null && world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY) {
            cir.setReturnValue(world.isClient ? ActionResult.SUCCESS : ActionResult.CONSUME);
            if (!world.isClient) {
                player.sendMessage(Text.translatable("message.dndreams.ender_chest_restricted"), true);
            }
        }
    }
}
