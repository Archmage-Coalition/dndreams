package net.eman3600.dndreams.rituals.setup;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;

import static net.eman3600.dndreams.Initializer.MODID;

public class RitualRegistry {

    public static final Identifier ID = new Identifier(MODID, "ritual");
    public static final RegistryKey<Registry<Ritual>> KEY = RegistryKey.ofRegistry(ID);

    public static final Registry<Ritual> REGISTRY = new SimpleRegistry<>(KEY, Lifecycle.stable(), Ritual::reference);

    public static Ritual register(String id, Ritual ritual) {
        return register(new Identifier(MODID, id), ritual);
    }

    public static Ritual register(Identifier id, Ritual ritual) {
        Registry.register(REGISTRY, id, ritual);
        return ritual;
    }

}
