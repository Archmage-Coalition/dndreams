package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.initializers.ModFluids;
import net.eman3600.dndreams.util.ItemEntityInterface;
import net.eman3600.dndreams.util.ItemInFlowingSpiritCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements ItemEntityInterface {

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void dndreams$itemEntityTick(CallbackInfo ci) {
        if (this.isSubmergedIn(ModFluids.FLOWING_SPIRIT_TAG)) {
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
}
