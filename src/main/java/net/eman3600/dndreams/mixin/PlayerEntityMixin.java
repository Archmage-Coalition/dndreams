package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.blocks.energy.BonfireBlock;
import net.eman3600.dndreams.cardinal_components.ShockComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.projectiles.TeslaSlashEntity;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.VariedMineSpeedItem;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    @Shadow public abstract Arm getMainArm();

    @Shadow public abstract boolean isInvulnerableTo(DamageSource damageSource);

    @Shadow public abstract float getAttackCooldownProgress(float baseTime);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.setReturnValue((info.getReturnValue())
                .add(ModAttributes.PLAYER_MANA_REGEN, 8d)
                .add(ModAttributes.PLAYER_MAX_MANA, 0d)
                .add(ModAttributes.PLAYER_REVIVAL, 1d)
                .add(ModAttributes.PLAYER_RECLAMATION, 5d));
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
            item.swingItem(player, getActiveHand(), (ServerWorld) player.world, getMainHandStack(), target);
        }

        if (EntityComponents.SHOCK.isProvidedBy(this)) {
            ShockComponent shock = EntityComponents.SHOCK.get(this);
            if (shock.hasShock() && getAttackCooldownProgress(0.5f) > 0.9f) {
                world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, this.getSoundCategory(), 1.0f, 2.5f);

                TeslaSlashEntity slash = new TeslaSlashEntity(this, world, shock.dischargeShock((float) getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
                world.spawnEntity(slash);
            }
        }
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void dndreams$getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        float f = cir.getReturnValueF();

        ItemStack stack = getMainHandStack();
        if (stack.getItem() instanceof VariedMineSpeedItem item) {
            f *= item.additionalModifiers(stack, (PlayerEntity) (Object) this, block, world);
        }

        cir.setReturnValue(f);
    }

    @Inject(method = "findRespawnPosition", at = @At("HEAD"), cancellable = true)
    private static void dndreams$findRespawnPosition(ServerWorld world, BlockPos pos, float angle, boolean forced, boolean alive, CallbackInfoReturnable<Optional<Vec3d>> cir) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BonfireBlock && state.get(Properties.LIT)) {
            Optional<Vec3d> optional = RespawnAnchorBlock.findRespawnPosition(EntityType.PLAYER, world, pos);

            cir.setReturnValue(optional);
        }
    }


    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    private void dndreams$applyDamage$absorbShock(DamageSource source, float amount, CallbackInfo ci) {
        try {
            ShockComponent shock = EntityComponents.SHOCK.get(this);

            if (isInvulnerableTo(source)) return;

            if (DamageSourceAccess.isElectric(source) && shock.canStoreShock()) {
                amount = this.applyArmorToDamage(source, amount);
                amount = this.modifyAppliedDamage(source, amount);

                if (amount > 0f) shock.chargeShock(amount);

                ci.cancel();
            }

            TormentComponent torment = EntityComponents.TORMENT.get(this);

            if (source == DamageSource.FREEZE) {
                torment.lowerSanity(-amount/2);
            }
        } catch (NullPointerException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}
