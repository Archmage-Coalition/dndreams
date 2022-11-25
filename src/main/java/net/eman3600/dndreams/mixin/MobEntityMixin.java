package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void dndreams$interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);
        if (((LivingEntity)this) instanceof WardenEntity warden && stack.isOf(Items.GLASS_BOTTLE) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            player.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0f, 1.0f);
            ItemStack itemStack2 = ItemUsage.exchangeStack(stack, player, ModItems.LIQUID_SOUL.getDefaultStack());
            player.setStackInHand(hand, itemStack2);

            player.getItemCooldownManager().set(stack.getItem(), 20);

            warden.increaseAngerAt(player, 80, true);

            cir.setReturnValue(ActionResult.success(this.world.isClient));
        }
    }
}
