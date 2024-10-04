package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.rituals.CraftingRitual;
import net.eman3600.dndreams.rituals.DreadMoonRitual;
import net.eman3600.dndreams.rituals.Ritual;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;

public class ModRituals {

    public static final Ritual CROWNED_EDGE = register("crowned_edge", new CraftingRitual());
    public static final Ritual TRUE_CROWNED_EDGE = register("true_crowned_edge", new CraftingRitual());
    public static final Ritual DREAD_MOON = register("dread_moon", new DreadMoonRitual());

    public static void registerRituals() {}

    private static Ritual register(String id, Ritual ritual) {
        return RitualRegistry.register(id, ritual);
    }
}
