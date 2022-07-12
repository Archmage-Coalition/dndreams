package net.eman3600.dndreams.mixin;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.EntityComponents;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess {

    @Shadow
    private int ambientDarkness;

    @Shadow
    public abstract RegistryKey<DimensionType> getDimensionKey();

    @Shadow
    public abstract long getTimeOfDay();

    @Shadow
    public abstract float getRainGradient(float delta);

    @Shadow
    public abstract float getThunderGradient(float delta);

    @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
    public void injectTimeOfDay(CallbackInfoReturnable info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            List<? extends PlayerEntity> players = getPlayers();
            float highestTorment = highestTorment(players);

            info.setReturnValue((long)(6000 + (highestTorment * 120)));
        }
    }

    @Inject(method = "initWeatherGradients", at = @At("HEAD"), cancellable = true)
    private void injectInitWeatherGardients(CallbackInfo info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            info.cancel();
        }
    }

    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    private void injectRainGradient(float delta, CallbackInfoReturnable info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            info.setReturnValue(0f);
        }
    }

    @Inject(method = "getThunderGradient", at = @At("HEAD"), cancellable = true)
    private void injectThunderGradient(float delta, CallbackInfoReturnable info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            info.setReturnValue(0f);
        }
    }

    @Inject(method = "calculateAmbientDarkness", at = @At("HEAD"), cancellable = true)
    private void injectAmbientDarkness(CallbackInfo info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {
            double d = 1.0D - (double)(this.getRainGradient(1.0F) * 5.0F) / 16.0D;
            double e = 1.0D - (double)(this.getThunderGradient(1.0F) * 5.0F) / 16.0D;
            double f = 0.5D + 2.0D * MathHelper.clamp((double)MathHelper.cos(this.getDimension().getSkyAngle(this.getTimeOfDay()) * 6.2831855F), -0.25D, 0.25D);
            this.ambientDarkness = (int)((1.0D - f * d * e) * 11.0D);
            info.cancel();
        }
    }

    private float highestTorment(List<? extends PlayerEntity> players) {
        float highest = 0f;

        for (PlayerEntity player: players) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);
            highest = Math.max(highest, torment.getTorment());
        }

        return highest;
    }
}
