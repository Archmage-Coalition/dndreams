package net.eman3600.dndreams.mixin;

import com.mojang.authlib.GameProfile;
import net.eman3600.dndreams.initializers.*;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {


    @Shadow public abstract Arm getMainArm();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void injectTick(CallbackInfo info) {
        if (getEntityWorld().getDimensionKey() == DimensionTypes.THE_END) {
            EntityComponents.TORMENT.get(this).addPerMinute(-5f);
        }

        if (WorldComponents.BLOOD_MOON.get(getEntityWorld()).isBloodMoon()) {
            EntityComponents.TORMENT.get(this).addPerMinute(2f);
        }
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.setReturnValue((info.getReturnValue())
                .add(ModAttributes.PLAYER_MANA_REGEN, 8d)
                .add(ModAttributes.PLAYER_MAX_MANA, 25d));
    }

    @Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
    private void dndreams$isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (ModStatusEffects.shouldRestrict((PlayerEntity)(Object)this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    private void dndreams$attack(Entity target, CallbackInfo ci) {
        if (getMainHandStack().getItem() instanceof AirSwingItem item && ((Object)this) instanceof ServerPlayerEntity player) {
            item.swingItem(player, getActiveHand(), (ServerWorld) player.world, getMainHandStack(), true);
        }
    }
}
