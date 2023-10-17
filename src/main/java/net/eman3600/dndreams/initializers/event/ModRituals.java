package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.initializers.basics.ModItems;
import net.eman3600.dndreams.initializers.basics.ModStatusEffects;
import net.eman3600.dndreams.initializers.entity.ModInfusions;
import net.eman3600.dndreams.rituals.*;
import net.eman3600.dndreams.rituals.setup.Ritual;
import net.eman3600.dndreams.rituals.setup.Ritual.Ring;
import net.eman3600.dndreams.rituals.setup.Ritual.CandleTuning;
import net.eman3600.dndreams.rituals.setup.RitualRegistry;

public class ModRituals {

    public static final Ritual WAYSTONE = register("waystone", new WaystoneRitual(250, new Ring(Ring.INNER_RING, CandleTuning.END)));
    public static final Ritual WAYSTONE_SIMPLE = register("waystone_simple", new WaystoneRitual(25));

    public static final Ritual TAGLOCK = register("taglock", new TaglockRitual());
    public static final Ritual TAGLOCK_RELOCATED = register("taglock_relocated", new TaglockRelocatedRitual());

    public static final Ritual CHARGE_DEMONIC_CORE = register("charge_demonic_core", new DemonicCoreRitual());
    public static final Ritual REPAIR_EDGE = register("repair_edge", new CraftingRitual(ModItems.CROWNED_EDGE, 1000, new Ring(Ring.INNER_RING, CandleTuning.OVERWORLD), new Ring(Ring.MIDDLE_RING, CandleTuning.END)));

    public static final Ritual AMETHYST_SOURCE = register("amethyst_source", new AmethystSourceRitual());
    public static final Ritual AMETHYST_SPROUT = register("amethyst_sprout", new AmethystSproutRitual());
    public static final Ritual SUMMON_STORM = register("summon_storm", new SummonStormRitual());
    public static final Ritual NIGHT_TERROR = register("night_terror", new NightTerrorRitual());

    public static final Ritual GRACE_AURA = register("grace_aura", new EffectAuraRitual(ModStatusEffects.GRACE, 48, 1, 500, 5, new Ring(Ring.INNER_RING, CandleTuning.END)));

    public static final Ritual NATURE_INFUSION = register("nature_infusion", new InfusionRitual(3000, ModInfusions.NATURE, new Ring(Ring.INNER_RING, CandleTuning.OVERWORLD), new Ring(Ring.MIDDLE_RING, CandleTuning.OVERWORLD)));
    public static final Ritual INFERNAL_INFUSION = register("infernal_infusion", new InfusionRitual(3000, ModInfusions.INFERNAL, new Ring(Ring.INNER_RING, CandleTuning.NETHER), new Ring(Ring.MIDDLE_RING, CandleTuning.NETHER)));
    public static final Ritual OTHERWHERE_INFUSION = register("otherwhere_infusion", new InfusionRitual(3000, ModInfusions.OTHERWHERE, new Ring(Ring.INNER_RING, CandleTuning.END), new Ring(Ring.MIDDLE_RING, CandleTuning.END)));
    public static final Ritual SPIRIT_INFUSION = register("spirit_infusion", new InfusionRitual(7000, ModInfusions.SPIRIT, new Ring(Ring.INNER_RING), new Ring(Ring.MIDDLE_RING)));

    public static void registerRituals() {}

    private static Ritual register(String id, Ritual ritual) {
        return RitualRegistry.register(id, ritual);
    }
}
