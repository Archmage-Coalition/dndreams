package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin extends Entity {
    protected LightningEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LightningEntity;emitGameEvent(Lnet/minecraft/world/event/GameEvent;)V"))
    private void dndreams$tick(CallbackInfo ci) {
        BlockPos pos = getBlockPos();

        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    BlockPos pos2 = pos.add(x, y, z);

                    if (world.getBlockState(pos2).isOf(ModBlocks.EMBER_MOSS)) {
                        world.setBlockState(pos2, ModBlocks.FRAZZLED_MOSS.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }
}
