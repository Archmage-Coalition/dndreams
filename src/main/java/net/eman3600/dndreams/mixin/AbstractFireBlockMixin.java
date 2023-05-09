package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public abstract class AbstractFireBlockMixin extends Block {
    public AbstractFireBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void dndreams$onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        for (ItemStack stack: entity.getArmorItems()) {
            if (stack.isOf(ModItems.CORRUPT_LEGGINGS)) {
                ci.cancel();
                break;
            }
        }
    }
}
