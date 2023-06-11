package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.InfusionComponentI;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.infusions.setup.InfusionRegistry;
import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModAttributes;
import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.eman3600.dndreams.networking.packet_c2s.DodgePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class InfusionComponent implements InfusionComponentI {
    public static final int LINK_LENGTH = 40;
    public static final int DODGE_COST = 5;
    public static final int DODGE_COOLDOWN = 24;

    private final PlayerEntity player;

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
    private int iTicks = 0;

    private boolean dirty = false;


    public InfusionComponent(PlayerEntity player) {
        this.player = player;
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
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("infusion", InfusionRegistry.REGISTRY.getId(infusion).toString());
        tag.putInt("link_ticks", linkTicks);
        tag.putBoolean("needs_kit", needsKit);
        tag.putBoolean("has_dodge", hasDodge);
        tag.putInt("dodge_cooldown", dodgeCooldown);
        tag.putInt("i_ticks", iTicks);
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
        }

        if (hasDodge && dodgeCooldown > 0) {
            dodgeCooldown--;
            markDirty();
        }

        if (iTicks > 0) {
            iTicks--;
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
        return hasDodge && dodgeCooldown <= 0 && player.isOnGround() && !player.hasStatusEffect(ModStatusEffects.STIFLED);
    }

    @Override
    public void setHasDodge(boolean allow) {

        hasDodge = allow;
        dodgeCooldown = 0;
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

    @Environment(EnvType.CLIENT)
    public void tryDodgeClient() {

        if (!canDodge()) return;

        ManaComponent mana = EntityComponents.MANA.get(player);

        if (mana.canAfford(DODGE_COST)) {

            Vec3d velocity = player.getVelocity();
            velocity = velocity.subtract(0, velocity.y, 0);

            if (velocity.lengthSquared() <= .0025) {
                velocity = Vec3d.fromPolar(0, player.getYaw());
            }

            velocity = velocity.normalize().multiply(player.getAttributeValue(ModAttributes.PLAYER_LUNGE)).add(0, 0.4, 0);

            player.setVelocityClient(velocity.x, velocity.y, velocity.z);

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

            player.setVelocity(velocity);

            player.world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_CREEPER_DEATH, SoundCategory.NEUTRAL, 5, 2);
        }
    }


}
