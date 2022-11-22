package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.util.ModRegistries;
import net.minecraft.block.*;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(SculkVeinBlock.class)
public abstract class SculkVeinBlockMixin extends MultifaceGrowthBlock implements SculkSpreadable, Waterloggable {
    @Shadow @Final private LichenGrower allGrowTypeGrower;

    public SculkVeinBlockMixin(Settings settings) {
        super(settings);
    }


    @Inject(method = "convertToBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SculkVeinBlock;hasDirection(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$convertToBlock(SculkSpreadManager spreadManager, WorldAccess world, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir, BlockState blockState, TagKey tagKey, Iterator var7, Direction direction) {
        if (!SculkVeinBlock.hasDirection(blockState, direction)) return;

        BlockPos blockPos;
        BlockState blockState2 = world.getBlockState(blockPos = pos.offset(direction));

        BlockState state = world.getBlockState(blockPos);
        List<TagKey<Block>> tags = state.streamTags().toList();

        for (TagKey<Block> tag: tags) {
            if (ModRegistries.SCULK_TRANSFORM.containsKey(tag)) {
                BlockState blockState3 = ModRegistries.SCULK_TRANSFORM.getOrDefault(tag, Blocks.SCULK).getDefaultState();
                world.setBlockState(blockPos, blockState3, Block.NOTIFY_ALL);
                Block.pushEntitiesUpBeforeBlockChange(blockState2, blockState3, world, blockPos);
                world.playSound(null, blockPos, SoundEvents.BLOCK_SCULK_SPREAD, SoundCategory.BLOCKS, 1.0f, 1.0f);

                cir.setReturnValue(true);
                break;
            }
        }
    }

    @Mixin(targets = "net.minecraft.block.SculkVeinBlock$SculkVeinGrowChecker")
    private abstract static class SculkVeinGrowCheckerMixin {
        @Inject(method = "canGrow(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
        private void dndreams$canGrow(BlockView world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state, CallbackInfoReturnable<Boolean> cir) {
            List<TagKey<Block>> tags = state.streamTags().toList();

            for (TagKey<Block> tag: tags) {
                if (ModRegistries.SCULK_TRANSFORM.containsKey(tag)) {
                    cir.setReturnValue(false);
                    break;
                }
            }
        }
    }

}
