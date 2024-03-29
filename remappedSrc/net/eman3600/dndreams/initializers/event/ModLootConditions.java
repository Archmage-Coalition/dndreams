package net.eman3600.dndreams.initializers.event;

import net.eman3600.dndreams.events.loot_conditions.TormentLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.eman3600.dndreams.Initializer.MODID;

public class ModLootConditions {

    public static final LootConditionType TORMENT = registerCondition("torment", new LootConditionType(new TormentLootCondition.Serializer()));




    private static LootConditionType registerCondition(String name, LootConditionType condition) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(MODID, name), condition);
    }

    public static void registerConditions() {}
}
