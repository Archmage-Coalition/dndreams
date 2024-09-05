package net.eman3600.dndreams.mixin;

import com.mojang.authlib.GameProfile;
import net.eman3600.dndreams.initializers.world.ModGameRules;
import net.eman3600.dndreams.mixin_interfaces.PlayerEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerEntityAccess {
    @Shadow @Final public ServerPlayerInteractionManager interactionManager;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Override
    public GameMode getGameMode() {
        return interactionManager.getGameMode();
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void dndreams$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {

        if (this.world.getGameRules().getBoolean(ModGameRules.DO_SANITY_TAX)) {

            this.experienceLevel /= 4;
            this.experienceProgress = 0;
            this.totalExperience = PlayerEntityAccess.getExperienceOf(this.experienceLevel);
        }
    }
}
