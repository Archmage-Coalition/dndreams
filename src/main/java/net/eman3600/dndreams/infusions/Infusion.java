package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public enum Infusion {
    NONE(0, "none", false, null),
    OVERWORLD(1, "overworld", true, World.OVERWORLD),
    NETHER(2, "nether", true, World.NETHER),
    END(3, "end", true, World.END),
    SPIRIT(4, "spirit", true, ModDimensions.DREAM_DIMENSION_KEY);

    private final int num;
    private final Identifier id;
    private final boolean hasPower;
    private final RegistryKey<World> world;

    Infusion(int num, String name, boolean hasPower, @Nullable RegistryKey<World> world) {
        this.num = num;
        this.id = new Identifier(Initializer.MODID, name);
        this.hasPower = hasPower;
        this.world = world;
    }

    public int getNum() {
        return num;
    }

    public Identifier getId() {
        return id;
    }

    public boolean isInfused() {
        return hasPower;
    }

    public RegistryKey<World> getWorld() {
        return world;
    }

    public static Infusion getFromNum(int find) {
        if (find == OVERWORLD.num) {
            return OVERWORLD;
        } else if (find == NETHER.num) {
            return NETHER;
        } else if (find == END.num) {
            return END;
        } else if (find == SPIRIT.num) {
            return SPIRIT;
        }
        return NONE;
    }
}
