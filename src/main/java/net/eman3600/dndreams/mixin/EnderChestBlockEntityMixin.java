package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin extends BlockEntity {
    public EnderChestBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "onOpen", at = @At("HEAD"), cancellable = true)
    private void dndreams$onOpen$noDream(PlayerEntity player, CallbackInfo ci) {
        if (world != null && world.getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY) {
            ci.cancel();
            if (!world.isClient) {
                player.sendMessage(Text.translatable("message.dndreams.ender_chest_restricted"), true);
            }
        }
    }
}
