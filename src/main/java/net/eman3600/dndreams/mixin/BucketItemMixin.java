package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.blocks.spirtloggable.Spiritloggable;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item implements FluidModificationItem {

    @Shadow @Final private Fluid fluid;

    @Shadow protected abstract void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos);

    public BucketItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void dndreams$use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (user.hasStatusEffect(ModStatusEffects.RESTRICTED)) {
            cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BucketItem;placeFluid(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/hit/BlockHitResult;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$use$spiritlog(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack itemStack, BlockHitResult blockHitResult, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState, BlockPos blockPos3) {
        if (Spiritloggable.FLUID_PROPERTIES.containsKey(this.fluid) && blockState.getBlock() instanceof Spiritloggable) {
            blockPos3 = blockHitResult.getBlockPos();

            if (this.placeFluid(user, world, blockPos3, blockHitResult)) {
                this.onEmptied(user, world, itemStack, blockPos3);
                if (user instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)user, blockPos3, itemStack);
                }
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                cir.setReturnValue(TypedActionResult.success(BucketItem.getEmptiedStack(itemStack, user), world.isClient()));
            }

        }
    }

    @Inject(method = "placeFluid", at = @At("HEAD"), cancellable = true)
    private void dndreams$placeFluid(PlayerEntity player, World world, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = world.getBlockState(pos);
        Block block;

        if ((block = state.getBlock()) instanceof Spiritloggable && fluid != Fluids.WATER && Spiritloggable.FLUID_PROPERTIES.containsKey(fluid)) {
            ((FluidFillable) block).tryFillWithFluid(world, pos, state, ((FlowableFluid)this.fluid).getStill(false));
            this.playEmptyingSound(player, world, pos);
            cir.setReturnValue(true);
        }
    }
}
