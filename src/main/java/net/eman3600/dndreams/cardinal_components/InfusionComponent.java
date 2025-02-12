package net.eman3600.dndreams.cardinal_components;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.blocks.VitalOreBlock;
import net.eman3600.dndreams.cardinal_components.interfaces.InfusionComponentI;
import net.eman3600.dndreams.entities.mobs.ParryableEntity;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.world.ModGameRules;
import net.eman3600.dndreams.items.misc_tool.AscendItem;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.misc_armor.EvergaleItem;
import net.eman3600.dndreams.items.trinket.AirJumpItem;
import net.eman3600.dndreams.mixin_interfaces.DamageSourceAccess;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityAccess;
import net.eman3600.dndreams.networking.packet_c2s.AirJumpPacket;
import net.eman3600.dndreams.networking.packet_c2s.AscendPacket;
import net.eman3600.dndreams.networking.packet_c2s.DodgePacket;
import net.eman3600.dndreams.networking.packet_c2s.GaleBoostPacket;
import net.eman3600.dndreams.networking.packet_s2c.MotionUpdatePacket;
import net.eman3600.dndreams.networking.packet_s2c.ParryFlashPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfusionComponent implements InfusionComponentI {
    public static final int DODGE_COST = 4;
    public static final int DODGE_COOLDOWN = 24;
    public static final int PARRY_TIME = 6;
    public static final int ROSE_COOLDOWN = 30;
    public static final int ROSE_RANGE = 20;
    public static final float PUNCH_DAMAGE = 2.5f;
    public static final double PUNCH_KNOCKBACK = 0.5;
    public static final double PARRY_KNOCKBACK = 1.25;
    public static final double PARRY_PROJ_KNOCKBACK = 2.5;

    private final PlayerEntity player;
    private final LivingEntityAccess access;


    /**
     * How long the player has left being linked to their bonfire.
     */
    private boolean needsKit = true;
    private boolean hasAerialDodge = false;
    private int dodgeCooldown = 0;
    private boolean dodgeLanded = true;
    private int iTicks = 0;
    private int parryTicks = 0;
    private boolean canParryPunch = false;
    private int airJumps = 0;
    private int jumpCooldown = 0;
    private boolean roseGlasses = false;
    private int roseCooldown = 0;
    private int ascendState = 0;
    private int galeCharge = 0;
    private final List<BlockPos> revealedQuartz = new ArrayList<>();

    private boolean dirty = false;


    public InfusionComponent(PlayerEntity player) {
        this.player = player;
        this.access = (LivingEntityAccess) player;
    }

    private TormentComponent getTorment() {
        return EntityComponents.TORMENT.get(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        needsKit = tag.getBoolean("needs_kit");
        hasAerialDodge = tag.getBoolean("has_aerial_dodge");
        dodgeCooldown = tag.getInt("dodge_cooldown");
        iTicks = tag.getInt("i_ticks");
        parryTicks = tag.getInt("p_ticks");
        canParryPunch = tag.getBoolean("p_punch");
        airJumps = tag.getInt("air_jumps");
        dodgeLanded = tag.getBoolean("dodge_landed");
        ascendState = tag.getInt("ascend_state");
        galeCharge = tag.getInt("gale_charge");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("needs_kit", needsKit);
        tag.putBoolean("has_aerial_dodge", hasAerialDodge);
        tag.putInt("dodge_cooldown", dodgeCooldown);
        tag.putInt("i_ticks", iTicks);
        tag.putInt("p_ticks", parryTicks);
        tag.putBoolean("p_punch", canParryPunch);
        tag.putInt("air_jumps", airJumps);
        tag.putBoolean("dodge_landed", dodgeLanded);
        tag.putInt("ascend_state", ascendState);
        tag.putInt("gale_charge", galeCharge);
    }

    @Override
    public void serverTick() {
        if (needsKit) {
            needsKit = false;
            markDirty();

            player.giveItemStack(new ItemStack(ModItems.BOOK_OF_DREAMS));
            GameRules rules = player.world.getGameRules();

            if (rules.getBoolean(ModGameRules.DO_SANITY_TAX) && !rules.getBoolean(GameRules.KEEP_INVENTORY)) {
                rules.get(GameRules.KEEP_INVENTORY).set(true, ((ServerWorld)player.world).getServer());
            }
        }

        if (dodgeCooldown > 0) {
            dodgeCooldown--;
            markDirty();
        }

        if (iTicks > 0) {
            iTicks--;
            markDirty();
        }

        if (parryTicks > 0) {
            parryTicks--;

            if (canParryPunch) {
                Vec3d pos = player.getEyePos().add(AirSwingItem.rayZVector(player.getHeadYaw(), player.getPitch()).multiply(.75f));
                Box box = Box.of(pos, 1.5d, 1.5d, 1.5d);

                for (LivingEntity target : player.world.getEntitiesByClass(LivingEntity.class, box, (entity) -> entity != player)) {
                    if (target instanceof ParryableEntity parryable && parryable.canParry()) {
                        float damage = parryable.parryInterruptDamage();
                        parryable.onParry(player);
                        hitParry(damage, target.isAlive() ? target : null);
                        break;
                    } else {
                        hitParryPunch(target);
                    }
                    canParryPunch = false;
                }
            }
        }

        if (!dodgeLanded && (player.isOnGround() || player.isTouchingWater())) {
            dodgeLanded = true;
            markDirty();
        }

        if (airJumps > 0 && (player.isOnGround() || player.isTouchingWater())) {
            airJumps = 0;
            markDirty();
        }

        if (ascendState == 1 && (AscendItem.isInBlock(player) || player.getY() > player.world.getTopY())) {
            ascendState = 2;
            markDirty();
        } else if (ascendState == 2 && (!AscendItem.isInBlock(player) || player.isOnGround() || player.getY() > player.world.getTopY())) {
            ascendState = 0;
            Vec3d velocity = new Vec3d(0, .6d, 0);
            player.setVelocity(velocity);
            player.velocityModified = true;
            player.velocityDirty = true;
            MotionUpdatePacket.send((ServerPlayerEntity) player);
            markDirty();
        }

        if (galeCharge > 0) {

            galeCharge--;
            markDirty();
        }



        if (dirty) {
            dirty = false;
            EntityComponents.INFUSION.sync(player);
        }
    }

    @Override
    public boolean hasAerialDodge() {
        return hasAerialDodge;
    }

    @Override
    public boolean canDodge() {
        return (hasAerialDodge || player.isOnGround()) && dodgeCooldown <= 0 && dodgeLanded && !player.hasStatusEffect(ModStatusEffects.STIFLED);
    }

    @Override
    public void setHasAerialDodge(boolean allow) {

        hasAerialDodge = allow;
        dodgeCooldown = 0;
        dodgeLanded = true;
        markDirty();
    }

    @Override
    public void startParry() {
        this.parryTicks = PARRY_TIME;
        this.canParryPunch = true;
        markDirty();
    }

    @Override
    public boolean isParrying() {
        return this.parryTicks > 0;
    }

    @Override
    public void hitParryPunch(LivingEntity target) {
        target.timeUntilRegen = 0;
        if (target.damage(DamageSourceAccess.chargeback(player), PUNCH_DAMAGE)) {
            Vec3d kb = AirSwingItem.rayZVector(player.getHeadYaw(), player.getPitch()).multiply(PUNCH_KNOCKBACK);

            target.addVelocity(kb.x, kb.y, kb.z);
            target.velocityDirty = true;
            target.velocityModified = true;

            target.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1, 1);
        } else {
            target.getWorld().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, SoundCategory.PLAYERS, 1, 1);
        }
    }

    @Override
    public void hitParry(float damageAbsorbed, @Nullable Entity source) {
        Vec3d pos = player.getEyePos();
        Vec3d flashPos = pos.add(AirSwingItem.rayZVector(player.getHeadYaw(), player.getPitch()).multiply(.3f));

        ParryFlashPacket.send((ServerWorld) player.getWorld(), flashPos);

        player.getItemCooldownManager().set(ModItems.CHARGEBACK, 10);

        HungerManager manager = player.getHungerManager();
        if (manager.getSaturationLevel() < manager.getFoodLevel()) {
            manager.setSaturationLevel(manager.getFoodLevel());
        }

        if (source instanceof LivingEntity entity && this.canParryPunch) {
            hitParryPunch(entity);
        }

        Box box = Box.of(pos, 4d, 5d, 4d);

        for (Entity entity : player.world.getOtherEntities(player, box, (e) -> true)) {
            if (this.canParryPunch)
                entity.timeUntilRegen = 0;
            entity.damage(DamageSourceAccess.parry(player), 4 + damageAbsorbed);

            if (entity instanceof LivingEntity livingEntity) {
                Vec3d angle = pos.subtract(entity.getPos());
                angle = angle.normalize().multiply(PARRY_KNOCKBACK);
                livingEntity.takeKnockback(angle.length(), angle.x, angle.z);
            } else if (entity instanceof PersistentProjectileEntity projectile) {
                Vec3d vel = AirSwingItem.rayZVector(player.getHeadYaw(), player.getPitch()).multiply(PARRY_PROJ_KNOCKBACK * -10);
                if (projectile instanceof TridentEntity) {
                    vel = vel.multiply(5, .5f, 5);
                } else {
                    PersistentProjectileEntity.PickupPermission pickupType = projectile.pickupType;
                    projectile.setOwner(player);
                    projectile.pickupType = pickupType;
                }
                projectile.setVelocity(vel);
                projectile.setPitch(player.getPitch());
                projectile.setYaw(player.getHeadYaw());
                projectile.setDamage(projectile.getDamage() + 1);
            }

            entity.velocityDirty = true;
            entity.velocityModified = true;
        }

        this.canParryPunch = false;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void giveImmunity() {

        iTicks = Math.min(DODGE_COOLDOWN, (int) (player.getAttributeValue(ModAttributes.PLAYER_EVASION)));
        markDirty();
    }

    @Override
    public boolean hasImmunity() {
        return iTicks > 0;
    }

    public boolean shouldSeeRose() {
        Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
        return optional.isPresent() && optional.get().isEquipped(ModItems.ROSE_GLASSES);
    }

    @Environment(EnvType.CLIENT)
    public void tryDodgeClient(Vec3d addedInput) {

        if (!canDodge()) return;

        ManaComponent mana = EntityComponents.MANA.get(player);

        if (mana.canAfford(DODGE_COST)) {

            Vec3d playerVelocity = player.getVelocity();
            if (addedInput.lengthSquared() <= .0025) {
                addedInput = addedInput.add(0, 0, 1);
            }
            double velY = player.isOnGround() ? 0 : playerVelocity.y;

            Vec3d newVelocity = AirSwingItem.rotateVector(addedInput, player.getHeadYaw(), player.getPitch());

            newVelocity = newVelocity.normalize().multiply(player.getAttributeValue(ModAttributes.PLAYER_LUNGE));

            player.setVelocityClient(newVelocity.x, velY, newVelocity.z);

            DodgePacket.send(newVelocity);
        }
    }

    @Override
    public void tryDodgeServer(Vec3d velocity) {

        ManaComponent mana = EntityComponents.MANA.get(player);

        if (mana.canAfford(DODGE_COST) && canDodge()) {

            mana.useMana(DODGE_COST);
            giveImmunity();
            dodgeCooldown = DODGE_COOLDOWN;
            dodgeLanded = false;

            player.setVelocity(velocity);

            player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_CREEPER_DEATH, SoundCategory.NEUTRAL, 5, 2);
        }
    }

    public int getMaxJumps() {

        try {

            int j = 0;
            TrinketComponent trinkets = TrinketsApi.getTrinketComponent(player).get();

            for (var pair: trinkets.getAllEquipped()) {
                if (pair.getRight().getItem() instanceof AirJumpItem item) {
                    j += item.jumps;
                }
            }

            return j;
        } catch (NullPointerException e) {
            return 0;
        }
    }


    @Override
    public void clientTick() {

        if (access.dndreams$isJumping() && !player.isFallFlying() && jumpCooldown <= 0 && airJumps < getMaxJumps()) {

            jumpCooldown = 9;

            Vec3d velocity = player.getVelocity();
            velocity = new Vec3d(velocity.x, ((LivingEntityAccess)player).getJumpVelocity() * 1.2f + player.getJumpBoostVelocityModifier(), velocity.z);
            if (player.isSprinting()) {
                float f = player.getYaw() * 0.017453292f;
                velocity = velocity.add(-MathHelper.sin(f) * 0.2f, 0.0, MathHelper.cos(f) * 0.2f);
            }

            player.setVelocity(velocity);
            player.velocityModified = true;
            player.velocityDirty = true;
            AirJumpPacket.send(velocity);
        } else if (access.dndreams$isJumping() && EvergaleItem.isUsing(player)) {

            Vec3d velocity = player.getVelocity().add(AirSwingItem.rayZVector(player.getYaw(), player.getPitch()).multiply(EvergaleItem.ACCELERATION));

            if (velocity.lengthSquared() > 16) {

                velocity = velocity.normalize().multiply(4);
            }

            player.setVelocity(velocity);
            player.velocityModified = true;
            player.velocityDirty = true;
            GaleBoostPacket.send(velocity);
        }

        if (player.isOnGround()) {
            jumpCooldown = 8;
        } else if (player.isTouchingWater()) {
            jumpCooldown = 2;
        } else if (jumpCooldown > 0) jumpCooldown--;

        if (ascendState > 0) {
            Vec3d velocity = new Vec3d(0, .8f, 0);

            player.setVelocity(velocity);
            player.velocityDirty = true;
            player.velocityModified = true;
            AscendPacket.send(velocity);
        }

        if (roseGlasses != shouldSeeRose()) {

            roseGlasses = !roseGlasses;
            updateRose();
        }

        if (roseGlasses && roseCooldown-- <= 0) {

            roseCooldown = ROSE_COOLDOWN;
            updateRose();
        }
    }

    private void updateRose() {

        for (BlockPos pos: revealedQuartz) {

            if (player.world.canSetBlock(pos) && player.world.getBlockState(pos).getBlock() instanceof VitalOreBlock) player.world.setBlockState(pos, player.world.getBlockState(pos).with(VitalOreBlock.GLASSES, false));
        }

        revealedQuartz.clear();

        if (roseGlasses) {

            BlockPos playerPos = player.getBlockPos();

            for (int i = -ROSE_RANGE; i <= ROSE_RANGE; i++) for (int j = -ROSE_RANGE; j <= ROSE_RANGE; j++) for (int k = -ROSE_RANGE; k <= ROSE_RANGE; k++) {

                BlockPos pos = playerPos.add(i, j, k);
                if (player.world.getBlockState(pos).getBlock() instanceof VitalOreBlock && !player.world.getBlockState(pos).get(VitalOreBlock.REVEALED)) {

                    player.world.setBlockState(pos, player.world.getBlockState(pos).with(VitalOreBlock.GLASSES, true));
                    revealedQuartz.add(pos);
                }
            }
        }
    }

    public boolean airJump() {
        if (airJumps < getMaxJumps()) {
            airJumps++;
            player.fallDistance = 0;
            access.dndreams$setJumpingCooldown(10);

            markDirty();
            return true;
        }
        return false;
    }

    public boolean galeBoost() {

        if (EvergaleItem.isUsing(player)) {
            galeCharge = 5;
            markDirty();
            return true;
        }

        return false;
    }

    public int getAscendState() {
        return ascendState;
    }

    public void setAscending() {
        ascendState = 1;
        markDirty();
    }

    public boolean isGaleBoosted() {
        return galeCharge > 0;
    }
}
