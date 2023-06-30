package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPiglinEntity.class)
public abstract class AbstractPiglinEntityMixin extends HostileEntity {
    @Shadow protected abstract boolean isImmuneToZombification();

    protected AbstractPiglinEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "shouldZombify", at = @At("HEAD"), cancellable = true)
    private void dndreams$shouldZombify(CallbackInfoReturnable<Boolean> cir) {
        if (!this.isImmuneToZombification() && EntityComponents.ROT.get(this).getRot() > 0) {
            cir.setReturnValue(true);
        }
    }
}
