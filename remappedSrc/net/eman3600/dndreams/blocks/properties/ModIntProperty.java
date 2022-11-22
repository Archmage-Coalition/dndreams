package net.eman3600.dndreams.blocks.properties;

import net.minecraft.state.property.IntProperty;

public class ModIntProperty extends IntProperty {
    public ModIntProperty(String name, int min, int max) {
        super(name, min, max);
    }
}
