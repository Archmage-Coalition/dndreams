package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.rituals.AmethystSourceRitual;
import net.eman3600.dndreams.rituals.WaystoneRitual;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;

public class ModRituals {

    public static final AbstractRitual WAYSTONE = register("waystone", new WaystoneRitual(250, new AbstractRitual.Ring(AbstractRitual.Ring.INNER_RING, AbstractRitual.CandleTuning.END)));
    public static final AbstractRitual WAYSTONE_SIMPLE = register("waystone_simple", new WaystoneRitual(25));

    public static final AbstractRitual AMETHYST_SOURCE = register("amethyst_source", new AmethystSourceRitual());

    public static void registerRituals() {}

    private static AbstractRitual register(String id, AbstractRitual ritual) {
        return RitualRegistry.register(id, ritual);
    }
}
