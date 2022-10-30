package net.eman3600.dndreams.rituals.setup;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;

import static net.eman3600.dndreams.Initializer.MODID;

public class RitualRegistry {

    public static final Identifier ID = new Identifier(MODID, "ritual");
    public static final RegistryKey<Registry<AbstractRitual>> KEY = RegistryKey.ofRegistry(ID);

    public static final Registry<AbstractRitual> REGISTRY = new SimpleRegistry<>(KEY, Lifecycle.stable(), AbstractRitual::reference);

    public static AbstractRitual register(String id, AbstractRitual ritual) {
        return register(new Identifier(MODID, id), ritual);
    }

    public static AbstractRitual register(Identifier id, AbstractRitual ritual) {
        Registry.register(REGISTRY, id, ritual);
        return ritual;
    }

}
