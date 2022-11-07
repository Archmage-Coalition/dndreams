package net.eman3600.dndreams.blocks.properties;

import net.minecraft.util.StringIdentifiable;

public enum CauldronState implements StringIdentifiable {
    IDLE("idle"),
    CRAFTING("crafting"),
    BREWING("brewing"),
    FINISHED("finished"),
    RUINED("ruined");

    private final String name;

    CauldronState(String name) {
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

    public static CauldronState fromString(String name) {
        for (CauldronState state: CauldronState.values()) {
            if (name.equals(state.name)) return state;
        }
        return IDLE;
    }
}
