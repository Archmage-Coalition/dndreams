package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.events.advancements.AwakenedCriterion;
import net.eman3600.dndreams.events.advancements.InsanityCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;

public class ModCriterion {
    public static AwakenedCriterion AWAKENED = new AwakenedCriterion();
    public static InsanityCriterion INSANITY = new InsanityCriterion();

    public static void registerCriterion() {
        Criteria.register(AWAKENED);
        Criteria.register(INSANITY);
    }
}
