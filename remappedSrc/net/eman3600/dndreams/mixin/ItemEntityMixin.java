package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.mixin_interfaces.ItemEntityInterface;
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
public abstract class ItemEntityMixin extends Entity implements ItemEntityInterface {

    @Unique private boolean unbrewable = false;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void dndreams$itemEntityTick(CallbackInfo ci) {
        if (this.isSubmergedIn(ModTags.FLOWING_SPIRIT)) {
            ItemInFlowingSpiritCallback.EVENT.invoker().collision((ItemEntity) (Object) this);
        }
    }

    int windupTicks = 0;

    @Override
    public int getWindupTicks() {
        return windupTicks;
    }

    @Override
    public void setWindupTicks(int ticks) {
        windupTicks = ticks;
    }

    @Override
    public void markUnbrewable() {
        unbrewable = true;
    }

    @Override
    public boolean isUnbrewable() {
        return unbrewable;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void dndreams$writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("dndreams.unbrewable", unbrewable);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void dndreams$readNbt(NbtCompound nbt, CallbackInfo ci) {
        unbrewable = nbt.getBoolean("dndreams.unbrewable");
    }
}
