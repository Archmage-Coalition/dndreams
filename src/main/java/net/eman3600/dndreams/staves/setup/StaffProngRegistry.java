package net.eman3600.dndreams.staves.setup;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import static net.eman3600.dndreams.Initializer.MODID;

public class StaffProngRegistry {

    public static final Identifier ID = new Identifier(MODID, "staff_prong");
    public static final RegistryKey<Registry<StaffProng>> KEY = RegistryKey.ofRegistry(ID);

    public static final Registry<StaffProng> REGISTRY = new SimpleRegistry<>(KEY, Lifecycle.stable(), StaffProng::reference);

    public static StaffProng register(String id, StaffProng core) {
        return register(new Identifier(MODID, id), core);
    }

    public static StaffProng register(Identifier id, StaffProng core) {
        Registry.register(REGISTRY, id, core);
        return core;
    }

}
