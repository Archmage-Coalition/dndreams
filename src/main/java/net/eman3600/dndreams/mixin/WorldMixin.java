package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.ClientInitializer;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.cardinal_components.WorldStateComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.WorldAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin implements net.minecraft.world.WorldAccess, WorldAccess {

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

    @Shadow @Final public boolean isClient;

    @Shadow public abstract RegistryKey<World> getRegistryKey();

    @Shadow public abstract Scoreboard getScoreboard();

    @Shadow protected float rainGradient;

    @Shadow protected float thunderGradient;

    @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
    public void injectTimeOfDay(CallbackInfoReturnable<Long> info) {
        if (getRegistryKey() == ModDimensions.HAVEN_DIMENSION_KEY) {
            info.setReturnValue(6000L);
        } else if (getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY) {
            List<? extends PlayerEntity> players = getPlayers();
            float lowestSanity = lowestSanity(players);

            info.setReturnValue((long)(18000 - (lowestSanity * 120)));
        }
    }

    @Inject(method = "initWeatherGradients", at = @At("HEAD"), cancellable = true)
    private void injectInitWeatherGradients(CallbackInfo info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY || getDimensionKey() == ModDimensions.GATEWAY_TYPE_KEY) {
            info.cancel();
        } else if (usesCustomState()) {
            WorldStateComponent state = getWorldState();

            this.rainGradient = state.getRainGradient();
            this.thunderGradient = state.getThunderGradient();
        }
    }

    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    private void injectRainGradient(float delta, CallbackInfoReturnable<Float> info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY || getDimensionKey() == ModDimensions.GATEWAY_TYPE_KEY) {
            info.setReturnValue(0f);
        } else if (usesCustomState()) {

            WorldStateComponent state = getWorldState();

            info.setReturnValue(state.getRainGradient());
        }
    }

    @Inject(method = "getThunderGradient", at = @At("HEAD"), cancellable = true)
    private void injectThunderGradient(float delta, CallbackInfoReturnable<Float> info) {
        if (getDimensionKey() == ModDimensions.DREAM_TYPE_KEY || getDimensionKey() == ModDimensions.GATEWAY_TYPE_KEY) {
            info.setReturnValue(0f);
        } else if (usesCustomState()) {

            WorldStateComponent state = getWorldState();

            info.setReturnValue(state.getThunderGradient());
        }
    }

    @Inject(method = "calculateAmbientDarkness", at = @At("HEAD"), cancellable = true)
    private void injectAmbientDarkness(CallbackInfo info) {
        if (getRegistryKey() == ModDimensions.DREAM_DIMENSION_KEY || getRegistryKey() == ModDimensions.HAVEN_DIMENSION_KEY) {
            double d = 1.0D - (double)(this.getRainGradient(1.0F) * 5.0F) / 16.0D;
            double e = 1.0D - (double)(this.getThunderGradient(1.0F) * 5.0F) / 16.0D;
            double f = 0.5D + 2.0D * MathHelper.clamp(MathHelper.cos(this.getDimension().getSkyAngle(this.getTimeOfDay()) * 6.2831855F), -0.25D, 0.25D);
            this.ambientDarkness = (int)((1.0D - f * d * e) * 11.0D);
            info.cancel();
        }
    }

    @Override
    public boolean isTrulyDay() {

        long time = getTimeOfDay() % 24000;

        return !this.getDimension().hasFixedTime() && (time >= 23461 || time < 12543);
    }
    @Override
    public boolean isTrulyNight() {

        return !this.getDimension().hasFixedTime() && !isTrulyDay();
    }

    @Unique
    public float lowestSanity(List<? extends PlayerEntity> players) {
        float lowest = 100f;

        for (PlayerEntity player: players) {
            TormentComponent torment = EntityComponents.TORMENT.get(player);
            lowest = Math.min(lowest, torment.getAttunedSanity());
        }

        return lowest;
    }

    @Unique
    private boolean drawAether() {

        return (isClient && ClientInitializer.drawAether((World)(Object) this));

    }

    private WorldStateComponent getWorldState() {
        return WorldComponents.WORLD_STATE.get(this);
    }

    private boolean usesCustomState() {
        return WorldComponents.WORLD_STATE.isProvidedBy(this) && getWorldState().isCustom();
    }

}
