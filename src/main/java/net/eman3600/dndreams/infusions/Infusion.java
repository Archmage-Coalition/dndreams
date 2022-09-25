package net.eman3600.dndreams.infusions;

import net.eman3600.dndreams.Initializer;
import net.eman3600.dndreams.initializers.ModDimensions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;

public enum Infusion {
    NONE(0, "none", false, null, Color.BLACK),
    OVERWORLD(1, "overworld", true, World.OVERWORLD, 0, 200, 0),
    NETHER(2, "nether", true, World.NETHER, 200, 0, 0),
    END(3, "end", true, World.END, 200, 0, 200),
    SPIRIT(4, "spirit", true, ModDimensions.DREAM_DIMENSION_KEY, 0, 200, 200);

    private final int num;
    private final Identifier id;
    private final boolean hasPower;
    private final RegistryKey<World> world;
    private final Color color;

    Infusion(int num, String name, boolean hasPower, @Nullable RegistryKey<World> world, int r, int g, int b) {
        this.num = num;
        this.id = new Identifier(Initializer.MODID, name);
        this.hasPower = hasPower;
        this.world = world;
        float[] color = Color.RGBtoHSB(r, g, b, null);

        this.color = Color.getHSBColor(color[0], color[1], color[2]);
    }

    Infusion(int num, String name, boolean hasPower, @Nullable RegistryKey<World> world, Color color) {
        this.num = num;
        this.id = new Identifier(Initializer.MODID, name);
        this.hasPower = hasPower;
        this.world = world;
        this.color = color;
    }

    public int getNum() {
        return num;
    }

    public Color getColor() {
        return color;
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
