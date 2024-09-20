package net.eman3600.dndreams.mixin.client;

import net.eman3600.dndreams.cardinal_components.DarkStormComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.cardinal_components.WorldStateComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.event.ModParticles;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.mixin_interfaces.ClientWorldAccess;
import net.eman3600.dndreams.mixin_interfaces.WorldAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World implements ClientWorldAccess {
    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract void setTimeOfDay(long timeOfDay);

    @Shadow public abstract Scoreboard getScoreboard();

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Redirect(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean dndreams$tickTime(GameRules instance, GameRules.Key<GameRules.BooleanRule> rule) {
        if (this.getDimensionKey() == ModDimensions.DREAM_TYPE_KEY) {

            List<? extends PlayerEntity> players = getPlayers();
            float lowestSanity = lowestSanity(players);

            this.setTimeOfDay((long)(18000 - (lowestSanity * 120)));

            return false;
        } else {
            return instance.getBoolean(rule);
        }
    }

    @Inject(method = "randomBlockDisplayTick", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dndreams$randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, Random random, Block block, BlockPos.Mutable pos, CallbackInfo ci, int i, int j, int k, BlockState blockState) {

        WorldStateComponent worldState = WorldComponents.WORLD_STATE.get(this);
        DarkStormComponent storm = WorldComponents.DARK_STORM.get(getScoreboard());

        float strength = storm.windStrength();

        if (!blockState.isFullCube(this, pos) && worldState.isCustom() && (random.nextFloat() * 5f) < (strength * strength)) {
            this.addParticle(ModParticles.BLACK_WIND, (double)i + 0.5, (double)j + 0.5, (double)k + 0.5, strength * storm.getWindX() + (random.nextFloat() * .1f - .05f), -0.1 - (.2 * strength) + (random.nextFloat() * .1f - .05f), strength * storm.getWindY() + (random.nextFloat() * .1f - .05f));
        }
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


    @Override
    public MinecraftClient getClient() {
        return this.client;
    }

    @Override
    public PlayerEntity getPlayer() {
        return this.client.player;
    }
}
