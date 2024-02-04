package net.eman3600.dndreams.initializers.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGameRules {

    public static final GameRules.Key<GameRules.BooleanRule> DO_SANITY_TAX = registerRule("doSanityTax", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true, (server, rule) -> {
        if (rule.get()) server.getGameRules().get(GameRules.KEEP_INVENTORY).set(true, server);
    }));

    public static void registerRules() {

    }

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> registerRule(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, category, type);
    }
}