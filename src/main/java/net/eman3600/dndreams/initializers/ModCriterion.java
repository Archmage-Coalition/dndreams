package net.eman3600.dndreams.initializers;

import net.eman3600.dndreams.events.advancements.AwakenedCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;

public class ModCriterion {
    public static Criterion AWAKENED = new AwakenedCriterion();

    public static void registerCriterion() {
        Criteria.register(AWAKENED);
    }
}
