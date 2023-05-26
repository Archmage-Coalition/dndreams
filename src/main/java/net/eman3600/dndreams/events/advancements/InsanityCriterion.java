package net.eman3600.dndreams.events.advancements;

import com.google.gson.JsonObject;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static net.eman3600.dndreams.Initializer.MODID;

public class InsanityCriterion extends AbstractCriterion<InsanityCriterion.Conditions> {
    private static final Identifier ID = new Identifier(MODID, "insanity");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        NumberRange.FloatRange floatRange = NumberRange.FloatRange.fromJson(obj.get("sanity"));
        return new Conditions(playerPredicate, floatRange);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> {
            TormentComponent component = EntityComponents.TORMENT.get(player);

            return conditions.sanity.test(component.getAttunedSanity());
        });
    }

    @Override
    public Identifier getId() {
        return ID;
    }


    public static class Conditions extends AbstractCriterionConditions {
        public final NumberRange.FloatRange sanity;

        public Conditions(EntityPredicate.Extended player, NumberRange.FloatRange sanity) {
            super(ID, player);
            this.sanity = sanity;
        }

        public static Conditions create() {
            return new Conditions(EntityPredicate.Extended.EMPTY, NumberRange.FloatRange.between(0, 100));
        }

        public static Conditions sanity(NumberRange.FloatRange sanity) {
            return new Conditions(EntityPredicate.Extended.EMPTY, sanity);
        }
    }
}
