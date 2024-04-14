package net.eman3600.dndreams.cardinal_components;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.eman3600.dndreams.blocks.VitalOreBlock;
import net.eman3600.dndreams.cardinal_components.interfaces.InfusionComponentI;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.infusions.setup.InfusionRegistry;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.eman3600.dndreams.initializers.world.ModGameRules;
import net.eman3600.dndreams.items.AscendItem;
import net.eman3600.dndreams.items.interfaces.AirSwingItem;
import net.eman3600.dndreams.items.misc_armor.EvergaleItem;
import net.eman3600.dndreams.items.trinket.AirJumpItem;
import net.eman3600.dndreams.mixin_interfaces.LivingEntityAccess;
import net.eman3600.dndreams.networking.packet_c2s.AirJumpPacket;
import net.eman3600.dndreams.networking.packet_c2s.AscendPacket;
import net.eman3600.dndreams.networking.packet_c2s.DodgePacket;
import net.eman3600.dndreams.networking.packet_c2s.GaleBoostPacket;
import net.eman3600.dndreams.networking.packet_s2c.MotionUpdatePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InfusionComponent implements InfusionComponentI {
    public static final int LINK_LENGTH = 40;
    public static final int DODGE_COST = 4;
    public static final int DODGE_COOLDOWN = 24;
    public static final int ROSE_COOLDOWN = 30;
    public static final int ROSE_RANGE = 20;

    private final PlayerEntity player;
    private final LivingEntityAccess access;

    /**
     * The player's current infusion.
     */
    private Infusion infusion = ModInfusions.NONE;
    /**
     * How long the player has left being linked to their bonfire.
     */
    private int linkTicks = 0;
    private boolean needsKit = true;
    private boolean hasDodge = false;
    private int dodgeCooldown = 0;
    private boolean dodgeLanded = true;
    private int iTicks = 0;
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

    @Override
    public Infusion getInfusion() {
        return infusion;
    }

    @Override
    public boolean infused() {
        return infusion.hasPower;
    }

    @Override
    public void setInfusion(Infusion change) {
        infusion = change;
        markDirty();
    }

    @Override
    public void setLinkTicks(int amount) {
        linkTicks = amount;

        markDirty();
    }

    @Override
    public boolean linkedToBonfire() {
        return linkTicks > 0;
    }

    @Override
    public boolean tryResist(DamageSource source, float amount) {
        float cost = amount * .2f;
        TormentComponent torment = getTorment();

        if (torment.getSanity() >= cost && infusion.resistantTo(amount, source, player)) {
            torment.lowerSanity(cost);
            return true;
        }

        return false;
    }

    private TormentComponent getTorment() {
        return EntityComponents.TORMENT.get(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        infusion = Infusion.ofID(Identifier.tryParse(tag.getString("infusion")));
        linkTicks = tag.getInt("link_ticks");
        needsKit = tag.getBoolean("needs_kit");
        hasDodge = tag.getBoolean("has_dodge");
        dodgeCooldown = tag.getInt("dodge_cooldown");
        iTicks = tag.getInt("i_ticks");
        airJumps = tag.getInt("air_jumps");
        dodgeLanded = tag.getBoolean("dodge_landed");
        ascendState = tag.getInt("ascend_state");
        galeCharge = tag.getInt("gale_charge");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("infusion", InfusionRegistry.REGISTRY.getId(infusion).toString());
        tag.putInt("link_ticks", linkTicks);
        tag.putBoolean("needs_kit", needsKit);
        tag.putBoolean("has_dodge", hasDodge);
        tag.putInt("dodge_cooldown", dodgeCooldown);
        tag.putInt("i_ticks", iTicks);
        tag.putInt("air_jumps", airJumps);
        tag.putBoolean("dodge_landed", dodgeLanded);
        tag.putInt("ascend_state", ascendState);
        tag.putInt("gale_charge", galeCharge);
    }

    @Override
    public void serverTick() {
        if (player.world instanceof ServerWorld serverWorld) {
            infusion.serverTick(serverWorld, this, player);
        }

        if (linkTicks > 0) {
            linkTicks--;
            markDirty();
        }

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

        if (!dodgeLanded && player.isOnGround()) {
            dodgeLanded = true;
            markDirty();
        }

        if (airJumps > 0 && player.isOnGround()) {
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
    public boolean hasDodge() {
        return hasDodge;
    }

    @Override
    public boolean canDodge() {
        return hasDodge && dodgeCooldown <= 0 && dodgeLanded && !player.hasStatusEffect(ModStatusEffects.STIFLED);
    }

    @Override
    public void setHasDodge(boolean allow) {

        hasDodge = allow;
        dodgeCooldown = 0;
        dodgeLanded = true;
        markDirty();
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
    public void tryDodgeClient() {

        if (!canDodge()) return;

        ManaComponent mana = EntityComponents.MANA.get(player);

        if (mana.canAfford(DODGE_COST)) {

            Vec3d velocity = player.getVelocity();
            double velY = player.isOnGround() ? 0.4 : velocity.y;
            velocity = velocity.subtract(0, velocity.y, 0);

            if (velocity.lengthSquared() <= .0025) {
                velocity = Vec3d.fromPolar(0, player.getYaw());
            }

            velocity = velocity.normalize().multiply(player.getAttributeValue(ModAttributes.PLAYER_LUNGE));

            player.setVelocityClient(velocity.x, velY, velocity.z);

            DodgePacket.send(velocity);
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

        if (access.isJumping() && !player.isFallFlying() && jumpCooldown <= 0 && airJumps < getMaxJumps()) {

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
        } else if (access.isJumping() && EvergaleItem.isUsing(player)) {

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

            if (player.world.canSetBlock(pos)) player.world.setBlockState(pos, player.world.getBlockState(pos).getBlock().getDefaultState());
        }

        revealedQuartz.clear();

        if (roseGlasses) {

            BlockPos playerPos = player.getBlockPos();

            for (int i = -ROSE_RANGE; i <= ROSE_RANGE; i++) for (int j = -ROSE_RANGE; j <= ROSE_RANGE; j++) for (int k = -ROSE_RANGE; k <= ROSE_RANGE; k++) {

                BlockPos pos = playerPos.add(i, j, k);
                if (player.world.getBlockState(pos).getBlock() instanceof VitalOreBlock && !player.world.getBlockState(pos).get(VitalOreBlock.REVEALED)) {

                    player.world.setBlockState(pos, player.world.getBlockState(pos).with(VitalOreBlock.REVEALED, true));
                    revealedQuartz.add(pos);
                }
            }
        }
    }

    public boolean airJump() {
        if (airJumps < getMaxJumps()) {
            airJumps++;
            player.fallDistance = 0;
            access.setJumpingCooldown(10);

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
