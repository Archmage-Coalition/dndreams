package net.eman3600.dndreams.events.loot_conditions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.eman3600.dndreams.cardinal_components.TormentComponent;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
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

public class AwakenedLootCondition implements LootCondition {


    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of();
    }

    @Override
    public LootConditionType getType() {
        return ModLootConditions.AWAKENED;
    }

    @Override
    public boolean test(LootContext lootContext) {
        try {
            return WorldComponents.BOSS_STATE.get(lootContext.getWorld().getScoreboard()).dragonSlain();
        } catch (Exception e) {
            return false;
        }
    }


    public static class Serializer implements JsonSerializer<AwakenedLootCondition> {
        @Override
        public void toJson(JsonObject jsonObject, AwakenedLootCondition condition, JsonSerializationContext ctx) {}

        @Override
        public AwakenedLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext ctx) {
            return new AwakenedLootCondition();
        }
    }
}
