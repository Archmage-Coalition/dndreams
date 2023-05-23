package net.eman3600.dndreams.events.loot_conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.event.ModLootConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

import java.util.Set;

public class SanityLootCondition implements LootCondition {
    private final LootContext.EntityTarget target;
    private final float sanity;
    private final boolean mad;


    public SanityLootCondition(LootContext.EntityTarget target, float sanity, boolean mad) {
        this.target = target;
        this.sanity = sanity;
        this.mad = mad;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.ORIGIN, this.target.getParameter());
    }

    @Override
    public LootConditionType getType() {
        return ModLootConditions.SANITY;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.get(target.getParameter());


        if (entity instanceof PlayerEntity player) {
            TormentComponent component = EntityComponents.TORMENT.get(player);

            return mad ? component.getAttunedSanity() <= sanity : component.getAttunedSanity() >= sanity;
        }

        return false;
    }


    public static class Serializer implements JsonSerializer<SanityLootCondition> {
        @Override
        public void toJson(JsonObject jsonObject, SanityLootCondition condition, JsonSerializationContext ctx) {
            jsonObject.add("entity", ctx.serialize(condition.target));
            jsonObject.addProperty("sanity", condition.sanity);
            jsonObject.addProperty("mad", condition.mad);
        }

        @Override
        public SanityLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext ctx) {
            return new SanityLootCondition(
                    JsonHelper.deserialize(jsonObject, "entity", ctx, LootContext.EntityTarget.class),
                    JsonHelper.getFloat(jsonObject, "sanity", 0f),
                    JsonHelper.getBoolean(jsonObject, "mad", false));
        }
    }
}
