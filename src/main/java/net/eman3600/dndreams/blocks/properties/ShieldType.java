package net.eman3600.dndreams.blocks.properties;

import net.minecraft.util.StringIdentifiable;

public enum ShieldType implements StringIdentifiable {
    ALL("all"),
    HOSTILE("hostile"),
    PLAYERS("players"),
    MOBS("mobs");

    private String name;

    ShieldType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
