package net.eman3600.dndreams.infusions.setup;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import static net.eman3600.dndreams.Initializer.MODID;

public class InfusionRegistry {

    public static final Identifier ID = new Identifier(MODID, "infusion");
    public static final RegistryKey<Registry<Infusion>> KEY = RegistryKey.ofRegistry(ID);

    public static final Registry<Infusion> REGISTRY = new SimpleRegistry<>(KEY, Lifecycle.stable(), Infusion::reference);

    public static Infusion register(String id, Infusion ritual) {
        return register(new Identifier(MODID, id), ritual);
    }

    public static Infusion register(Identifier id, Infusion ritual) {
        Registry.register(REGISTRY, id, ritual);
        return ritual;
    }

}
