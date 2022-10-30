package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.rituals.WaystoneRitual;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;

public class ModRituals {

    public static final AbstractRitual WAYSTONE = register("waystone", new WaystoneRitual());

    public static void registerRituals() {}

    private static AbstractRitual register(String id, AbstractRitual ritual) {
        return RitualRegistry.register(id, ritual);
    }
}
