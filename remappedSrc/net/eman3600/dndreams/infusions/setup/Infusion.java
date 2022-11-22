package net.eman3600.dndreams.infusions.setup;

import net.eman3600.dndreams.cardinal_components.InfusionComponent;
import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;

public class Infusion {

    private final RegistryEntry.Reference<Infusion> entry;
    public final boolean hasPower;
    public final RegistryKey<World> world;
    public final Color color;

    public static final HashMap<RegistryKey<World>, Infusion> WORLDS_TO_INFUSIONS = new HashMap<>();


    public Infusion(boolean hasPower, @Nullable RegistryKey<World> world, Color color) {
        this.entry = InfusionRegistry.REGISTRY.createEntry(this);
        this.hasPower = hasPower;
        this.world = world;
        this.color = color;

        if (world != null && !WORLDS_TO_INFUSIONS.containsKey(world)) {
            WORLDS_TO_INFUSIONS.put(world, this);
        }
    }

    public Infusion(boolean hasPower, @Nullable RegistryKey<World> world, int r, int g, int b) {
        this(hasPower, world, fromRGB(r, g, b));
    }


    /**
     * Meant to be overridden for passive effects.
     * @param world The world which is ticked.
     * @param component The infusion component of the player being ticked.
     * @param player The player in which the infusion belongs to.
     */
    public void serverTick(ServerWorld world, InfusionComponent component, PlayerEntity player) {}

    /**
     * Meant to be overridden for damage resistances. A damage resistance will turn a once-lethal damage into a nonlethal form.
     * @param damage The amount of damage, in case this is important.
     * @param source The source of the damage, which is the most important bit.
     * @param player The player being damaged.
     * @return Whether the player is resistant to the damage.
     */
    public boolean resistantTo(float damage, DamageSource source, PlayerEntity player) {
        return false;
    }





    private static Color fromRGB(int r, int g, int b) {
        float[] color = Color.RGBtoHSB(r, g, b, null);

        return Color.getHSBColor(color[0], color[1], color[2]);
    }

    public RegistryEntry.Reference<Infusion> reference() {
        return entry;
    }

    public static Infusion ofID(Identifier id) {
        Infusion infusion = InfusionRegistry.REGISTRY.get(id);

        if (infusion == null) {
            return ModInfusions.NONE;
        }

        return InfusionRegistry.REGISTRY.get(id);
    }

    public String getTranslationKey() {
        return Util.createTranslationKey("infusion", InfusionRegistry.REGISTRY.getId(this));
    }


}
