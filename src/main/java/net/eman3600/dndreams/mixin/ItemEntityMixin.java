package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.ItemEntityAccess;
import net.eman3600.dndreams.util.ItemInFlowingSpiritCallback;
import net.eman3600.dndreams.util.ModTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements ItemEntityAccess {

    @Unique private boolean unbrewable = false;
    @Unique private boolean transmutable = true;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void dndreams$itemEntityTick(CallbackInfo ci) {
        if (this.isSubmergedIn(ModTags.FLOWING_SPIRIT)) {
            ItemInFlowingSpiritCallback.EVENT.invoker().collision((ItemEntity) (Object) this);
        }
    }

    @Override
    public void markUnbrewable() {
        unbrewable = true;
    }

    @Override
    public boolean isUnbrewable() {
        return unbrewable;
    }

    @Override
    public boolean isTransmutable() {
        return transmutable;
    }

    @Override
    public void setTransmutable(boolean transmutable) {
        this.transmutable = transmutable;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void dndreams$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("dndreams.unbrewable", unbrewable);
        nbt.putBoolean("dndreams.transmutable", transmutable);
    }

    @Inject(method = "canMerge()Z", at = @At("HEAD"), cancellable = true)
    private void dndreams$canMerge(CallbackInfoReturnable<Boolean> cir) {
        if (!this.transmutable) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void dndreams$readNbt(NbtCompound nbt, CallbackInfo ci) {
        unbrewable = nbt.getBoolean("dndreams.unbrewable");
        transmutable = nbt.getBoolean("dndreams.transmutable");
    }
}
