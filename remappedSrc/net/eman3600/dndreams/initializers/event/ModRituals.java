package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.eman3600.dndreams.rituals.*;
import net.eman3600.dndreams.rituals.setup.AbstractRitual;
import net.eman3600.dndreams.rituals.setup.AbstractRitual.Ring;
import net.eman3600.dndreams.rituals.setup.AbstractRitual.CandleTuning;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;

public class ModRituals {

    public static final AbstractRitual WAYSTONE = register("waystone", new WaystoneRitual(250, new Ring(Ring.INNER_RING, CandleTuning.END)));
    public static final AbstractRitual WAYSTONE_SIMPLE = register("waystone_simple", new WaystoneRitual(25));

    public static final AbstractRitual AMETHYST_SOURCE = register("amethyst_source", new AmethystSourceRitual());
    public static final AbstractRitual AMETHYST_SPROUT = register("amethyst_sprout", new AmethystSproutRitual());

    public static final AbstractRitual SUMMON_STORM = register("summon_storm", new SummonStormRitual());

    public static final AbstractRitual NATURE_INFUSION = register("nature_infusion", new InfusionRitual(3000, ModInfusions.NATURE, new Ring(Ring.INNER_RING, CandleTuning.OVERWORLD), new Ring(Ring.MIDDLE_RING, CandleTuning.OVERWORLD)));
    public static final AbstractRitual INFERNAL_INFUSION = register("infernal_infusion", new InfusionRitual(3000, ModInfusions.INFERNAL, new Ring(Ring.INNER_RING, CandleTuning.NETHER), new Ring(Ring.MIDDLE_RING, CandleTuning.NETHER)));
    public static final AbstractRitual OTHERWHERE_INFUSION = register("otherwhere_infusion", new InfusionRitual(3000, ModInfusions.OTHERWHERE, new Ring(Ring.INNER_RING, CandleTuning.END), new Ring(Ring.MIDDLE_RING, CandleTuning.END)));
    public static final AbstractRitual SPIRIT_INFUSION = register("spirit_infusion", new InfusionRitual(7000, ModInfusions.SPIRIT, new Ring(Ring.INNER_RING), new Ring(Ring.MIDDLE_RING)));

    public static void registerRituals() {}

    private static AbstractRitual register(String id, AbstractRitual ritual) {
        return RitualRegistry.register(id, ritual);
    }
}
