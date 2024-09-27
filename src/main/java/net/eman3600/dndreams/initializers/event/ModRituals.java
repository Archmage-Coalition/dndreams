package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.rituals.*;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.eman3600.dndreams.rituals.setup.Ritual.Ring;
import net.eman3600.dndreams.rituals.setup.Ritual.CandleTuning;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;

public class ModRituals {



    public static void registerRituals() {}

    private static Ritual register(String id, Ritual ritual) {
        return RitualRegistry.register(id, ritual);
    }
}
