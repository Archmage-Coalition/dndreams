package net.eman3600.dndreams.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.WardenAngerManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.function.Predicate;

@Mixin(WardenAngerManager.class)
public abstract class WardenAngerManagerMixin {

    @Shadow @Final protected Object2IntMap<Entity> suspectsToAngerLevel;

    @Inject(method = "increaseAngerAt", at = @At("RETURN"))
    private void dndreams$increaseAngerAt$afflictSpotted(Entity entity, int amount, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof LivingEntity living && cir.getReturnValue() >= 80) {
            living.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SPOTTED, Math.min((cir.getReturnValue() - 79) * 20, 600)));
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectSet;iterator()Lit/unimi/dsi/fastutil/objects/ObjectIterator;", ordinal = 1))
    private void dndreams$tick(ServerWorld world, Predicate<Entity> suspectPredicate, CallbackInfo ci) {
        Iterator iterator = this.suspectsToAngerLevel.object2IntEntrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();

            int j = entry.getIntValue();
            Entity entity = (Entity)entry.getKey();

            if (entity instanceof LivingEntity living) {
                if (living.hasStatusEffect(ModStatusEffects.SPOTTED) && j < 85) {
                    entry.setValue(85);
                } else if (!living.hasStatusEffect(ModStatusEffects.SPOTTED) && j >= 80) {
                    entry.setValue(75);
                }
            }
        }
    }
}
