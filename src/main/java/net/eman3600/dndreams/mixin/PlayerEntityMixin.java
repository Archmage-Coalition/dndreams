package net.eman3600.dndreams.mixin;

import net.eman3600.dndreams.blocks.energy.BonfireBlock;
import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.cardinal_components.ShockComponent;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.entities.projectiles.TeslaSlashEntity;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.items.InstrumentOfTruthItem;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.interfaces.VariableMineSpeedItem;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.mixin_interfaces.PlayerEntityAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;
import java.util.Optional;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    @Shadow public abstract Arm getMainArm();

    @Shadow public abstract boolean isInvulnerableTo(DamageSource damageSource);

    @Shadow public abstract float getAttackCooldownProgress(float baseTime);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract PlayerInventory getInventory();

    @Shadow public abstract float getMovementSpeed();

    @Shadow public abstract void increaseStat(Stat<?> stat, int amount);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.setReturnValue((info.getReturnValue())
                .add(ModAttributes.PLAYER_MANA_REGEN, 8d)
                .add(ModAttributes.PLAYER_MAX_MANA, 0d)
                .add(ModAttributes.PLAYER_REVIVAL, 1d)
                .add(ModAttributes.PLAYER_RECLAMATION, 1d)
                .add(ModAttributes.PLAYER_EVASION, 7d)
                .add(ModAttributes.PLAYER_LUNGE, 1d));
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

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5f))
    private float dndreams$attack$critDamage(float constant) {

        if (getMainHandStack().getItem() instanceof InstrumentOfTruthItem item && item.getForm(getMainHandStack()) == InstrumentOfTruthItem.InstrumentForm.KATANA) {

            return 3f;
        }

        return constant;
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void dndreams$getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        float f = cir.getReturnValueF();

        ItemStack stack = getMainHandStack();
        if (stack.getItem() instanceof VariableMineSpeedItem item) {
            f *= item.additionalMiningModifiers(stack, (PlayerEntity) (Object) this, block, world);
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

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setAbsorptionAmount(F)V", shift = At.Shift.AFTER))
    private void dndreams$applyDamage$afterAbsorptionDamage(DamageSource source, float amount, CallbackInfo ci) {
        if (getInventory().getArmorStack(2).isOf(ModItems.CORRUPT_CHESTPLATE)) {
            addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLOODLUST, (int) Math.ceil(amount * 40)));

            if (source.getAttacker() instanceof LivingEntity attacker) {
                attacker.setOnFireFor((int) Math.ceil(amount * 2));
            }
        }

        if (hasStatusEffect(ModStatusEffects.HEARTBLEED) && source.getAttacker() instanceof LivingEntity attacker) {
            float multiplier = 0.2f * (getStatusEffect(ModStatusEffects.HEARTBLEED).getAmplifier() + 1);

            attacker.heal(amount * multiplier);
        }

        if (DamageSourceAccess.isAffliction(source)) {

            EntityComponents.ROT.maybeGet(this).ifPresent(rot -> rot.inflictRot(MathHelper.ceil(amount)));
        }

        TormentComponent torment = EntityComponents.TORMENT.get(this);

        if (source == DamageSource.DROWN && torment.isFearDrowning()) {

            torment.lowerSanity(4f);
        }
    }

    @Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
    private void dndreams$canFoodHeal(CallbackInfoReturnable<Boolean> cir) {

        if (hasStatusEffect(ModStatusEffects.MORTAL)) cir.setReturnValue(false);
    }

    @ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.02f))
    private float dndreams$tickMovement$maintainSpeed(float constant) {

        return (PlayerEntityAccess.hasAerialMovement(this)) ? (float) (getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) / getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)) * constant : constant;
    }

    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    private void dndreams$isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {

            InfusionComponent infusion = EntityComponents.INFUSION.get(this);

            if (infusion.hasImmunity() && damageSource.getSource() != null && !damageSource.isOutOfWorld()) {
                cir.setReturnValue(true);
            }
        }
    }
}
