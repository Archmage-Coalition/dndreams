package net.eman3600.dndreams.initializers.entity;

import net.eman3600.dndreams.infusions.InfernalInfusion;
import net.eman3600.dndreams.infusions.NatureInfusion;
import net.eman3600.dndreams.infusions.OtherwhereInfusion;
import net.eman3600.dndreams.infusions.SpiritInfusion;
import net.eman3600.dndreams.infusions.setup.Infusion;
import net.eman3600.dndreams.infusions.setup.InfusionRegistry;
import net.eman3600.dndreams.rituals.AmethystSourceRitual;
import net.eman3600.dndreams.rituals.AmethystSproutRitual;
import net.eman3600.dndreams.rituals.SummonStormRitual;
import net.eman3600.dndreams.rituals.WaystoneRitual;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;

import java.awt.*;

public class ModInfusions {

    public static final Infusion NONE = register("none", new Infusion(false, null, Color.BLACK));
    public static final Infusion NATURE = register("nature", new NatureInfusion());
    public static final Infusion INFERNAL = register("infernal", new InfernalInfusion());
    public static final Infusion OTHERWHERE = register("otherwhere", new OtherwhereInfusion());
    public static final Infusion SPIRIT = register("spirit", new SpiritInfusion());

    public static void registerRituals() {}

    private static Infusion register(String id, Infusion infusion) {
        return InfusionRegistry.register(id, infusion);
    }
}
