package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.DrownedEntityAccess;
import net.eman3600.dndreams.mixin_interfaces.TridentEntityAccess;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity implements TridentEntityAccess {

    @Shadow private ItemStack tridentStack;

    @Shadow @Final private static TrackedData<Byte> LOYALTY;

    @Shadow @Final private static TrackedData<Boolean> ENCHANTED;

    @Shadow protected abstract boolean isOwnerAlive();

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setTridentStack(ItemStack stack) {
        this.tridentStack = stack.copy();
        this.dataTracker.set(LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    @Override
    public void collideWithDrowned(DrownedEntity drowned) {
        if (dataTracker.get(LOYALTY) > 0 ? this.isNoClip() && this.isOwner(drowned) : this.pickupType == PickupPermission.ALLOWED) {
            if (this.world.isClient || !this.inGround && !this.isNoClip() || this.shake > 0) {
                return;
            }

            float[] linkedChances = ((DrownedEntityAccess)drowned).getHandDropChances();
            float[] clonedChances = new float[] {linkedChances[0], linkedChances[1]};

            if (drowned.tryEquip(this.tridentStack)) {

                if (dataTracker.get(LOYALTY) > 0) {
                    linkedChances[0] = clonedChances[0];
                    linkedChances[1] = clonedChances[1];
                }

                drowned.sendPickup(this, 1);
                this.discard();
            }
        }

    }

    @Inject(method = "onPlayerCollision", at = @At("TAIL"))
    private void dndreams$onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (getOwner() instanceof DrownedEntity && dataTracker.get(LOYALTY) == 0) {
            super.onPlayerCollision(player);
        }
    }
}
