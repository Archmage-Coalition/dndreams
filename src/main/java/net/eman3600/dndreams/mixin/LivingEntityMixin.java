package net.eman3600.dndreams.mixin;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.blocks.entities.BonfireBlockEntity;
import net.eman3600.dndreams.cardinal_components.*;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.entity.ModEntities;
import net.eman3600.dndreams.initializers.world.ModDimensions;
import net.eman3600.dndreams.items.AscendItem;
import net.eman3600.dndreams.items.celestium.CelestiumArmorItem;
import net.eman3600.dndreams.items.tormite.TormiteArmorItem;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityAccess;
import net.eman3600.dndreams.util.ModFoodComponents;
import net.eman3600.dndreams.util.ModTags;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.NoSuchElementException;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityAccess {
    @Shadow
    @Final
    private AttributeContainer attributes;


    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow public abstract EntityDimensions getDimensions(EntityPose pose);

    @Shadow public abstract boolean isInsideWall();

    @Shadow public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow public abstract boolean canMoveVoluntarily();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source);

    @Shadow public abstract boolean isUndead();

    @Shadow public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow protected abstract int computeFallDamage(float fallDistance, float damageMultiplier);

    @Shadow private boolean effectsChanged;

    @Shadow public abstract boolean canTakeDamage();

    @Shadow public abstract Vec3d applyMovementInput(Vec3d movementInput, float slipperiness);

    @Shadow protected abstract void applyDamage(DamageSource source, float amount);

    @Shadow protected abstract float applyArmorToDamage(DamageSource source, float amount);

    @Shadow protected abstract float modifyAppliedDamage(DamageSource source, float amount);

    @Shadow public abstract float getAbsorptionAmount();

    @Shadow public abstract void setAbsorptionAmount(float amount);

    @Shadow public abstract float getHealth();

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract DamageTracker getDamageTracker();

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract boolean hasStackEquipped(EquipmentSlot slot);

    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow protected boolean jumping;

    @Shadow private int jumpingCooldown;

    @Shadow public abstract int getArmor();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void injectDeath(DamageSource damageSource, CallbackInfo info) {
        if ((Entity)this instanceof WitherEntity) {
            WorldComponents.BOSS_STATE.get(world.getScoreboard()).flagWitherSlain(true);
        } else if ((Entity)this instanceof MerchantEntity) {
            EntityComponents.TORMENT.maybeGet(damageSource.getAttacker()).ifPresent(torment -> torment.lowerSanity(15));
        } else if ((Entity)this instanceof AnimalEntity e && e.isBaby() && !world.isClient) {

            for (BlockPos pos : BlockPos.iterateOutwards(getBlockPos(), 7, 7, 7)) {

                if (world.getBlockEntity(pos) instanceof BonfireBlockEntity bonfire) {

                    bonfire.onSacrifice((ServerWorld) world);
                }
            }
        }
    }




    @Inject(method = "fall", at = @At("HEAD"), cancellable = true)
    private void dndreams$fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
        Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity)(Object)this);
        if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.LAVA_STRIDERS)) {
            this.checkBlockCollision();
            if (isAtLavaSurface()) {
                this.onLanding();
                ci.cancel();
            }
        }
        if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.WATER_STRIDERS)) {
            this.checkBlockCollision();
            if (isAtHydroSurface()) {
                this.onLanding();
                ci.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void dndreams$tick$tail(CallbackInfo ci) {
        Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity)(Object)this);
        if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.LAVA_STRIDERS)) {
            updateLavaFloating();
            this.checkBlockCollision();
        }
        if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.WATER_STRIDERS)) {
            updateHydroFloating();
            this.checkBlockCollision();
        }
    }

    @Inject(method = "canWalkOnFluid", at = @At("RETURN"), cancellable = true)
    private void dndreams$canWalkOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity)(Object)this);
        if (trinketOptional.isPresent() &&
                (trinketOptional.get().isEquipped(ModItems.LAVA_STRIDERS) && state.isIn(FluidTags.LAVA) && hasNotBrokenLava() ||
                        trinketOptional.get().isEquipped(ModItems.WATER_STRIDERS) && state.isIn(ModTags.HYDRO) && hasNotBrokenHydro())) {
            cir.setReturnValue(true);
        }
    }

    private boolean isAtLavaSurface() {
        return hasNotBrokenLava() && isInLava();
    }

    private boolean isAtHydroSurface() {
        return hasNotBrokenHydro() && isInHydro();
    }

    private boolean isInHydro() {
        return !this.firstUpdate && (this.fluidHeight.getDouble(FluidTags.WATER) > 0.0 || this.fluidHeight.getDouble(ModTags.FLOWING_SPIRIT) > 0.0 || this.fluidHeight.getDouble(ModTags.SORROW) > 0.0);
    }

    @Override
    public boolean hasNotBrokenLava() {
        if (this.isSneaking() && this.isInLava()) return false;
        return this.fluidHeight.getDouble(FluidTags.LAVA) <= 0.7 && !isOnFire();
    }

    @Override
    public boolean hasNotBrokenHydro() {
        if (this.isSneaking() && isInHydro()) return false;
        return this.fluidHeight.getDouble(ModTags.HYDRO) <= 0.7;
    }

    private void updateLavaFloating() {
        if (isAtLavaSurface()) {
            ShapeContext shapeContext = ShapeContext.of(this);
            if (!shapeContext.isAbove(FluidBlock.COLLISION_SHAPE, this.getBlockPos(), true) || this.world.getFluidState(this.getBlockPos().up()).isIn(FluidTags.LAVA)) {
                this.setVelocity(this.getVelocity().multiply(0.5).add(0.0, 0.05, 0.0));
            } else {
                this.onGround = true;
            }
        }
    }

    private void updateHydroFloating() {
        if (isAtHydroSurface()) {
            ShapeContext shapeContext = ShapeContext.of(this);
            if (!shapeContext.isAbove(FluidBlock.COLLISION_SHAPE, this.getBlockPos(), true) || this.world.getFluidState(this.getBlockPos().up()).isIn(ModTags.HYDRO)) {
                this.setVelocity(this.getVelocity().multiply(0.5).add(0.0, 0.05, 0.0));
            } else {
                this.onGround = true;
            }
        }
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At("HEAD"), cancellable = true)
    private static void dndreams$getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {

        if (stack.isOf(ModItems.CLOUD_WINGS) || stack.isOf(ModItems.EVERGALE)) cir.setReturnValue(EquipmentSlot.CHEST);
    }






    @Inject(method = "baseTick", at = @At("HEAD"))
    public void dndreams$baseTick(CallbackInfo ci) {
        Optional<TrinketComponent> trinketOptional = TrinketsApi.getTrinketComponent((LivingEntity)(Object)this);
        if (trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.FLAME_CAPE) && getFireTicks() > 60) setFireTicks(60);

        if (isSubmergedIn(ModTags.FLOWING_SPIRIT) && !hasStatusEffect(ModStatusEffects.INSUBSTANTIAL) && !(trinketOptional.isPresent() && trinketOptional.get().isEquipped(ModItems.SUBSTANCE_CLOAK))) {
            addStatusEffect(new StatusEffectInstance(ModStatusEffects.INSUBSTANTIAL, Integer.MAX_VALUE, 0, true, true));
        } else if (hasStatusEffect(ModStatusEffects.INSUBSTANTIAL) && !isSubmergedIn(ModTags.FLOWING_SPIRIT) && (!AscendItem.isInBlock(this) || isOnGround())) {
            removeStatusEffect(ModStatusEffects.INSUBSTANTIAL);
        }
        EntityComponents.SHOCK.maybeGet(this).ifPresent(component -> {
            if (component.isCushioned() && this.isOnGround()) component.setCushioned(false);
        });

        if (fluidHeight.getOrDefault(ModTags.SORROW, 0) > 0.1f) {

            if ((Object)this instanceof PlayerEntity player) {

                TormentComponent component = EntityComponents.TORMENT.get(player);

                component.lowerPerMinute(100f);
            }
        }

    }

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void dndreams$tryUseTotem$mortality(DamageSource source, CallbackInfoReturnable<Boolean> cir) {

        if (hasStatusEffect(ModStatusEffects.MORTAL) && world.getRegistryKey() != ModDimensions.DREAM_DIMENSION_KEY) cir.setReturnValue(false);
    }

    @Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true)
    private void dndreams$tryUseTotem$stopDeath(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            if ((Entity) this instanceof PlayerEntity player) {
                DreamingComponent dream = EntityComponents.DREAMING.get(player);
                ReviveComponent revive = EntityComponents.REVIVE.get(player);
                if (dream.isDreaming()) {

                    player.setHealth(1.0f);

                    RegistryKey<World> registryKey = World.OVERWORLD;
                    ServerWorld serverWorld = ((ServerWorld) world).getServer().getWorld(registryKey);
                    FabricDimensions.teleport(player, serverWorld, new TeleportTarget(dream.returnPos(), Vec3d.ZERO, player.getYaw(), player.getPitch()));

                    cir.setReturnValue(true);
                } else if (revive.canRevive()) {

                    setHealth(MathHelper.ceil(1f));

                    if (EntityComponents.TORMENT.isProvidedBy(this)) {
                        EntityComponents.TORMENT.get(this).lowerSanity(25f);
                    }
                    EntityComponents.ROT.maybeGet(this).ifPresent(rot -> {
                        float total = getMaxHealth() + rot.getRot();

                        if (total > 10 && rot.getRot() > 0 && getMaxHealth() < 10) {
                            rot.healRot((int)(10 - getMaxHealth()));
                        }
                    });

                    addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 4, false, false, true));

                    revive.revive();

                    this.world.sendEntityStatus(this, EntityStatuses.USE_TOTEM_OF_UNDYING);
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/tag/TagKey;)Z"), require = 0)
    private boolean dndreams$baseTick$isSubmergedIn(LivingEntity instance, TagKey<Fluid> tagKey) {

        if (tagKey == FluidTags.WATER) {

            if (instance instanceof PlayerEntity player && EntityComponents.TORMENT.get(player).isFearDrowning()) {

                return true;
            }

            return instance.isSubmergedIn(tagKey) | instance.isSubmergedIn(ModTags.SORROW);
        } else {
            return instance.isSubmergedIn(tagKey);
        }
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectUtil;hasWaterBreathing(Lnet/minecraft/entity/LivingEntity;)Z"))
    private boolean dndreams$baseTick$waterBreathing(LivingEntity entity) {

        if (entity instanceof PlayerEntity player && EntityComponents.TORMENT.get(player).isFearDrowning()) {

            return false;
        }

        return StatusEffectUtil.hasWaterBreathing(entity) && !entity.isSubmergedIn(ModTags.SORROW);
    }

    @Inject(method = "getNextAirUnderwater", at = @At("HEAD"), cancellable = true)
    private void dndreams$getNextAirUnderwater(int air, CallbackInfoReturnable<Integer> cir) {
        try {
            LivingEntity entity = (LivingEntity) (Object) this;

            if (entity.isSubmergedIn(ModTags.SORROW)) {
                cir.setReturnValue(Math.max(air > 0 ? air - 5 : air - 2, -20));
            }
        } catch (ClassCastException ignored) {}
    }



    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void dndreams$damage$tormiteNullify(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Cancels damage if the expression evaluates to true
        // Expression evaluates to true if all of the following are met:
        // 1. The damage is small enough to be chip damage
        // 2. Something gives this entity chip damage immunity
        // 3. The entity has neither heartbleed nor mortality
        // 4. The damage source is not out of world, affliction, or electric
        if ((amount <= 1.25f || (amount <= (1.25f + 0.175f * this.getArmor()) && !source.bypassesArmor())) && (TormiteArmorItem.wornPieces(this) >= 4 || this.getType().isIn(ModTags.CHIP_IMMUNE_ENEMIES)) && !this.hasStatusEffect(ModStatusEffects.HEARTBLEED) && !this.hasStatusEffect(ModStatusEffects.MORTAL) && !source.isOutOfWorld() && source instanceof DamageSourceAccess access && !access.isAffliction() && !access.isElectric()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float dndreams$damage$heartbleed(float amount) {
        if (this.hasStatusEffect(ModStatusEffects.HEARTBLEED)) {
            return (float) (amount * Math.pow(1.2f, this.getStatusEffect(ModStatusEffects.HEARTBLEED).getAmplifier() + 1));
        } else return amount;
    }

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void dndreams$modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (shouldResist(amount, source)) {
            float hp = getHealth() + getAbsorptionAmount();
            float wouldRemain = hp - amount;
            float cap = getMaxHealth();

            if (wouldRemain < cap) {
                float extraDmg = 0;
                if (hp > cap) {
                    extraDmg = hp - cap;
                    amount -= extraDmg;
                    hp = cap;
                }

                amount = Math.min((float) (hp - (hp * Math.pow(1 / Math.pow(Math.E, 1 / cap), amount))), hp - .2f) + extraDmg;

                cir.setReturnValue(amount);
            }
        }
    }

    @Override
    public boolean shouldResist(float damage, DamageSource source) {

        return false;
    }

    @Redirect(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I", opcode = Opcodes.PUTFIELD))
    public void dndreams$damage$shortenIFrames(LivingEntity instance, int value) {
        if (this.hasStatusEffect(ModStatusEffects.MORTAL)) {
            instance.timeUntilRegen = 16;
        } else {
            instance.timeUntilRegen = value;
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void dndreams$addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        StatusEffect status = effect.getEffectType();

        if ((status == StatusEffects.WITHER && (getType() == EntityType.WARDEN || getType() == ModEntities.TORMENTOR
                || (getType() == EntityType.PLAYER && TrinketsApi.getTrinketComponent((PlayerEntity)(Object)this).isPresent()
                && TrinketsApi.getTrinketComponent((PlayerEntity)(Object)this).get().isEquipped(ModItems.LIFE_GIVING_AMULET))))
                || (status == ModStatusEffects.HAUNTED && CelestiumArmorItem.wornPieces(this) >= 4) || ((status == ModStatusEffects.HEARTBLEED) && TormiteArmorItem.wornPieces(this) >= 4)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    private void dndreams$applyDamage$absorbShock(DamageSource source, float amount, CallbackInfo ci) {
        try {
            ShockComponent shock = EntityComponents.SHOCK.get(this);

            if (isInvulnerableTo(source)) return;

            if (DamageSourceAccess.isElectric(source) && shock.canStoreShock()) {
                if (amount > 0f) shock.chargeShock(amount);

                ci.cancel();
            }
        } catch (NullPointerException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setAbsorptionAmount(F)V", shift = At.Shift.AFTER))
    private void dndreams$applyDamage$afterAbsorptionDamage(DamageSource source, float amount, CallbackInfo ci) {
        /*if (hasStatusEffect(ModStatusEffects.HEARTBLEED) && source.getAttacker() instanceof LivingEntity attacker) {
            float multiplier = 0.2f * (getStatusEffect(ModStatusEffects.HEARTBLEED).getAmplifier() + 1);

            attacker.heal(amount * multiplier);
        }*/

        if (DamageSourceAccess.isAffliction(source)) {

            EntityComponents.ROT.maybeGet(this).ifPresent(rot -> rot.inflictRot(MathHelper.ceil(amount)));
        }
    }

    @Inject(method = "applyFoodEffects", at = @At("HEAD"))
    private void dndreams$applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
        if (targetEntity instanceof PlayerEntity player && stack.isFood()) {
            FoodComponent food = stack.getItem().getFoodComponent();

            if (ModFoodComponents.FOODS_TO_SANITY.containsKey(food)) {

                EntityComponents.TORMENT.get(player).lowerSanity(-ModFoodComponents.FOODS_TO_SANITY.get(food));
            }
        }
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
    private static void dndreams$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(ModAttributes.PLAYER_RECLAMATION, 1d)
        );
    }

    @ModifyArg(method = "heal", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"))
    private float dndreams$heal$reclamation(float health) {
        float amountHealed = health - getHealth();
        float bonus = (amountHealed * (float)getAttributeValue(ModAttributes.PLAYER_RECLAMATION)) - amountHealed;

        return health + bonus;
    }

    @ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08))
    private double dndreams$travel$grace(double constant) {

        if (hasStatusEffect(ModStatusEffects.GRACE)) {

            onLanding();
            return isSneaking() ? constant : 0.02 / (1 + getStatusEffect(ModStatusEffects.GRACE).getAmplifier());
        }

        return constant;
    }

    @Inject(method = "canHit", at = @At("RETURN"), cancellable = true)
    private void dndreams$canHit$dodge(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && EntityComponents.INFUSION.isProvidedBy(this)) {

            InfusionComponent infusion = EntityComponents.INFUSION.get(this);

            if (infusion.hasImmunity()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Override
    public boolean isJumping() {
        return jumping;
    }

    @Override
    public int getJumpingCooldown() {
        return jumpingCooldown;
    }

    @Override
    public void setJumpingCooldown(int jumpingCooldown) {
        this.jumpingCooldown = jumpingCooldown;
    }
}
