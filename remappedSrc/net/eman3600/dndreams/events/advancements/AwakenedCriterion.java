package net.eman3600.dndreams.events.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.ConstructBeaconCriterion;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class AwakenedCriterion extends AbstractCriterion<AwakenedCriterion.Conditions> {
    private static final Identifier ID = new Identifier(MODID, "awakened");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Identifier getId() {
        return ID;
    }


    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(EntityPredicate.Extended player) {
            super(ID, player);
        }

        public static ConstructBeaconCriterion.Conditions create() {
            return new ConstructBeaconCriterion.Conditions(EntityPredicate.Extended.EMPTY, NumberRange.IntRange.ANY);
        }

        public static ConstructBeaconCriterion.Conditions level(NumberRange.IntRange level) {
            return new ConstructBeaconCriterion.Conditions(EntityPredicate.Extended.EMPTY, level);
        }
    }
}
