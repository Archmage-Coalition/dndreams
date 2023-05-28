package net.eman3600.dndreams.staves.setup;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import static net.eman3600.dndreams.Initializer.MODID;

public class StaffHandleRegistry {

    public static final Identifier ID = new Identifier(MODID, "staff_handle");
    public static final RegistryKey<Registry<StaffHandle>> KEY = RegistryKey.ofRegistry(ID);

    public static final Registry<StaffHandle> REGISTRY = new SimpleRegistry<>(KEY, Lifecycle.stable(), StaffHandle::reference);

    public static StaffHandle register(String id, StaffHandle core) {
        return register(new Identifier(MODID, id), core);
    }

    public static StaffHandle register(Identifier id, StaffHandle core) {
        Registry.register(REGISTRY, id, core);
        return core;
    }

}
