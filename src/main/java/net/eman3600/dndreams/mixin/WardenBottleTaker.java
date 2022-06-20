package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(GlassBottleItem.class)
public abstract class WardenBottleTaker {
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof WardenEntity) {
            WardenEntity warden = (WardenEntity)entity;
            ItemUsage.exchangeStack(stack, user, new ItemStack(ModItems.LIQUID_SOUL));
            warden.increaseAngerAt(user, 80, true);

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
