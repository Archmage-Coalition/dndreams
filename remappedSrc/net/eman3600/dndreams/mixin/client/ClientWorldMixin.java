package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.ClientInitializer;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.mixin_interfaces.WorldAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World implements ClientWorldAccess {
    @Shadow @Final private MinecraftClient client;

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Redirect(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean dndreams$tickTime(GameRules instance, GameRules.Key<GameRules.BooleanRule> rule) {
        if (drawAether() || (this.getDimensionKey() == ModDimensions.DREAM_TYPE_KEY && ((WorldAccess)this).highestTorment(getPlayers()) >= 100f)) {
            return false;
        } else {
            return instance.getBoolean(rule);
        }
    }

    @Redirect(method = "setTimeOfDay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld$Properties;setTimeOfDay(J)V"))
    private void dndreams$setTimeOfDay(ClientWorld.Properties instance, long timeOfDay) {
        if (drawAether()) {
            instance.setTimeOfDay(18000L);
        } else {
            instance.setTimeOfDay(timeOfDay);
        }
    }


    @Unique
    private boolean drawAether() {
        return ClientInitializer.drawAether(this);
    }

    @Override
    public MinecraftClient getClient() {
        return this.client;
    }

    @Override
    public PlayerEntity getPlayer() {
        return this.client.player;
    }
}
