package net.eman3600.dndreams.events.loot_conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.eman3600.dndreams.initializers.event.ModLootConditions;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

import java.util.Set;

public class BloodMoonLootCondition implements LootCondition {
    private final boolean nightOnly;
    private final boolean inverted;


    public BloodMoonLootCondition(boolean nightOnly, boolean inverted) {
        this.nightOnly = nightOnly;
        this.inverted = inverted;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of();
    }

    @Override
    public LootConditionType getType() {
        return ModLootConditions.BLOOD_MOON;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ServerWorld serverWorld = lootContext.getWorld();

        BloodMoonComponent component = WorldComponents.BLOOD_MOON.get(serverWorld);

        return inverted != (nightOnly ? component.isBloodMoon() : component.damnedNight());
    }


    public static class Serializer implements JsonSerializer<BloodMoonLootCondition> {
        @Override
        public void toJson(JsonObject jsonObject, BloodMoonLootCondition condition, JsonSerializationContext ctx) {
            jsonObject.addProperty("night_only", condition.nightOnly);
            jsonObject.addProperty("inverted", condition.inverted);
        }

        @Override
        public BloodMoonLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext ctx) {
            return new BloodMoonLootCondition(
                    JsonHelper.getBoolean(jsonObject, "nightOnly", true),
                    JsonHelper.getBoolean(jsonObject, "inverted", false));
        }
    }
}
