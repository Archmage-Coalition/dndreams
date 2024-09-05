package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.DrownedEntityAccess;
import net.eman3600.dndreams.mixin_interfaces.TridentEntityAccess;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity implements RangedAttackMob, DrownedEntityAccess {

    public DrownedEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;setVelocity(DDDFF)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void dndreams$attack$modifyTrident(LivingEntity target, float pullProgress, CallbackInfo ci, TridentEntity tridentEntity, double d, double e, double f, double g) {
        ItemStack stack = getMainHandStack();

        if (stack.isOf(Items.TRIDENT) && tridentEntity instanceof TridentEntityAccess access) {
            access.setTridentStack(stack);
            tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }
    }

    @Redirect(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/DrownedEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"))
    private void dndreams$initEquipment$modifyTrident(DrownedEntity instance, EquipmentSlot equipmentSlot, ItemStack stack) {
        if (stack.isOf(Items.TRIDENT)) {
            stack.setDamage(instance.world.random.nextBetween(25, 150));

            int loyaltyScore = instance.world.random.nextInt(5);

            switch (loyaltyScore) {
                case 2, 3 -> stack.addEnchantment(Enchantments.LOYALTY, 1);
                case 4 -> stack.addEnchantment(Enchantments.LOYALTY, 2);
            }

            if (instance.world.random.nextInt(10) <= loyaltyScore) {
                stack.addEnchantment(Enchantments.CHANNELING, 1);
            }
        }

        instance.equipStack(equipmentSlot, stack);
    }

    @ModifyConstant(method = "initEquipment", constant = @Constant(doubleValue = 0.9))
    private double dndreams$initEquipment$commonTridents(double constant) {
        return 0.66;
    }

    @ModifyConstant(method = "initEquipment", constant = @Constant(intValue = 10))
    private int dndreams$initEquipment$rareFishingRods(int constant) {
        return 14;
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void dndreams$attack$loseTrident(LivingEntity target, float pullProgress, CallbackInfo ci) {
        setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    public float[] getHandDropChances() {
        return handDropChances;
    }
}
