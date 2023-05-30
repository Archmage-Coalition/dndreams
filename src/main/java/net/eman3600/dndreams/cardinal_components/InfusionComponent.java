package net.eman3600.dndreams.cardinal_components;

import net.eman3600.dndreams.cardinal_components.interfaces.InfusionComponentI;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.infusions.setup.InfusionRegistry;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class InfusionComponent implements InfusionComponentI {
    public static final int LINK_LENGTH = 40;

    private final PlayerEntity player;

    /**
     * The player's current infusion.
     */
    private Infusion infusion = ModInfusions.NONE;
    /**
     * How long the player has left being linked to their bonfire.
     */
    private int linkTicks = 0;

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
    }

    @Override
    public void setLinkTicks(int amount) {
        linkTicks = amount;

        EntityComponents.INFUSION.sync(player);
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
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("infusion", InfusionRegistry.REGISTRY.getId(infusion).toString());
        tag.putInt("link_ticks", linkTicks);
    }

    @Override
    public void serverTick() {
        if (player.world instanceof ServerWorld serverWorld) {
            infusion.serverTick(serverWorld, this, player);
        }

        if (linkTicks > 0) {
            linkTicks--;
            EntityComponents.INFUSION.sync(player);
        }
    }
}
