package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.ModStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoglinEntity.class)
public abstract class HoglinEntityMixin extends AnimalEntity implements Monster, Hoglin {
    @Shadow protected abstract boolean isImmuneToZombification();

    protected HoglinEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canConvert", at = @At("HEAD"), cancellable = true)
    private void dndreams$shouldZombify(CallbackInfoReturnable<Boolean> cir) {
        if (!this.isImmuneToZombification() && hasStatusEffect(ModStatusEffects.AFFLICTION)) {
            cir.setReturnValue(true);
        }
    }
}
