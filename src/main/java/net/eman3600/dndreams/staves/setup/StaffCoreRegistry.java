package net.eman3600.dndreams.staves.setup;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import static net.eman3600.dndreams.Initializer.MODID;

public class StaffCoreRegistry {

    public static final Identifier ID = new Identifier(MODID, "staff_core");
    public static final RegistryKey<Registry<StaffCore>> KEY = RegistryKey.ofRegistry(ID);

    public static final Registry<StaffCore> REGISTRY = new SimpleRegistry<>(KEY, Lifecycle.stable(), StaffCore::reference);

    public static StaffCore register(String id, StaffCore core) {
        return register(new Identifier(MODID, id), core);
    }

    public static StaffCore register(Identifier id, StaffCore core) {
        Registry.register(REGISTRY, id, core);
        return core;
    }

}
