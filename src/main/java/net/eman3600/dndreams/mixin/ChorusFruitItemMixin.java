package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChorusFruitItem.class)
public abstract class ChorusFruitItemMixin extends Item {

    public ChorusFruitItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getX()D", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack) {

        BlockPos pos = user.getBlockPos();

        BlockPos focusPos = null;

        for (int i = -16; i <= 16; i++) for (int j = -8; j <= 8; j++) for (int k = -16; k <= 16; k++) {

            BlockPos blockPos = pos.add(i, j, k);
            BlockState focusState = world.getBlockState(blockPos);

            if (focusState.isOf(ModBlocks.CHORUS_FOCUS) && world.isAir(blockPos.up()) && world.isAir((blockPos.up(2)))) {

                if (focusPos == null || focusPos.getSquaredDistance(pos) > blockPos.getSquaredDistance(pos)) focusPos = blockPos;
            }
        }

        if (focusPos != null && world.getWorldBorder().contains(focusPos)) {

            Vec3d vec = Vec3d.ofCenter(focusPos.up());

            Vec3d prevPos = user.getPos();
            user.teleport(vec.x, vec.y, vec.z, true);

            world.emitGameEvent(GameEvent.TELEPORT, prevPos, GameEvent.Emitter.of(user));
            SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
            world.playSound(null, vec.x, vec.y, vec.z, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
            user.playSound(soundEvent, 1.0f, 1.0f);

            if (user instanceof PlayerEntity) {
                ((PlayerEntity)user).getItemCooldownManager().set(this, 20);
            }

            cir.setReturnValue(itemStack);
        }
    }
}
