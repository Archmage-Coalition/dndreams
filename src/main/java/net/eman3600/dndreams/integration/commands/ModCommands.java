package net.eman3600.dndreams.integration.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.eman3600.dndreams.cardinal_components.BloodMoonComponent;
import net.eman3600.dndreams.cardinal_components.BossStateComponent;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModCommands {

    public static class CommandSuggestions implements SuggestionProvider<ServerCommandSource> {

        public List<String> suggestions;

        public CommandSuggestions(List<String> suggestions) {
            this.suggestions = suggestions;
        }

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
            CommandSource.suggestMatching(this.suggestions, builder);
            return builder.buildFuture();
        }

    }

    public static CommandSuggestions bossSuggestions = new CommandSuggestions(List.of("wither", "ender_dragon", "elrunez"));


    public static void displayFeedback(CommandContext<ServerCommandSource> context, String string, boolean sendToOps, Object... args) {
        context.getSource().sendFeedback(Text.translatable("command.dndreams." + string, args), sendToOps);
    }
    public static void displayError(CommandContext<ServerCommandSource> context, String string, Object... args) {
        context.getSource().sendError(Text.translatable("command.dndreams." + string, args));
    }

    protected static String displayBloodMoonTime(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        BloodMoonComponent bloodComponent = WorldComponents.BLOOD_MOON.get(world);

        return "bloodmoon.get." + (bloodComponent.isBloodMoon() ? "now" : bloodComponent.damnedNight() ? "tonight" : "inactive");
    }

    protected static String displayBloodMoonTimeSet(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        BloodMoonComponent bloodComponent = WorldComponents.BLOOD_MOON.get(world);

        return "bloodmoon.set." + (bloodComponent.isBloodMoon() ? "now" : bloodComponent.damnedNight() ? "tonight" : "inactive");
    }

    protected static String displayBossSlain(CommandContext<ServerCommandSource> context, String slain) {
        ServerWorld world = context.getSource().getWorld();
        BossStateComponent boss = WorldComponents.BOSS_STATE.get(world.getScoreboard());

        return "slain." + slain + "." + switch (slain) {
            case "wither" -> boss.witherSlain();
            case "ender_dragon" -> boss.dragonSlain();
            case "elrunez" -> boss.elrunezSlain();
            default -> false;
        };

        /*return switch(slain) {
             case "wither" -> "Wither slain: " + boss.witherSlain();
             case "ender_dragon" -> "Ender Dragon slain: " + boss.dragonSlain();
             case "elrunez" -> "Elrunez slain: " + boss.elrunezSlain();
             default -> "Invalid boss!";
        };*/
    }

    protected static void setBossSlain(CommandContext<ServerCommandSource> context, String boss, boolean slain) {
        ServerWorld world = context.getSource().getWorld();
        BossStateComponent state = WorldComponents.BOSS_STATE.get(world.getScoreboard());

        boolean setSomething = switch (boss) {
            case "wither", "ender_dragon", "elrunez" -> true;
            default -> false;
        };

        switch (boss) {
            case "wither" -> state.flagWitherSlain(slain);
            case "ender_dragon" -> state.flagDragonSlain(slain);
            case "elrunez" -> state.flagElrunezSlain(slain);
        }

        displayFeedback(context, displayBossSlain(context, boss), setSomething);

    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("dndreams");

            LiteralArgumentBuilder<ServerCommandSource> bloodMoon = CommandManager.literal("bloodmoon").requires((context) -> context.hasPermissionLevel(2));

            bloodMoon.then(CommandManager.literal("get").executes((context -> {
                displayFeedback(context, displayBloodMoonTime(context), false);

                return 0;
            })));

            bloodMoon.then(CommandManager.literal("set").then(CommandManager.argument("tonight", BoolArgumentType.bool()).executes((context -> {
                boolean setBloodMoon = context.getArgument("tonight", Boolean.class);

                ServerWorld world = context.getSource().getWorld();

                if (world.getRegistryKey() == World.OVERWORLD) {
                    BloodMoonComponent bloodComponent = WorldComponents.BLOOD_MOON.get(world);

                    bloodComponent.setDamnedNight(setBloodMoon);
                    displayFeedback(context, displayBloodMoonTimeSet(context), true);
                } else {
                    displayError(context, "bloodmoon.set.wrong_dimension");
                }
                return 0;
            }))));

            LiteralArgumentBuilder<ServerCommandSource> slain = CommandManager.literal("slain").requires((context) -> context.hasPermissionLevel(2));

            slain.then(CommandManager.literal("get").then(CommandManager.argument("boss", StringArgumentType.word()).executes(((context) -> {
                String boss = context.getArgument("boss", String.class);
                displayFeedback(context, displayBossSlain(context, boss), false);

                return 0;
            })).suggests(bossSuggestions)));

            slain.then(CommandManager.literal("set").then(CommandManager.argument("boss", StringArgumentType.word()).suggests(bossSuggestions).then(CommandManager.argument("slain", BoolArgumentType.bool()).executes(((context) -> {
                String boss = context.getArgument("boss", String.class);
                boolean isBossSlain = context.getArgument("slain", Boolean.class);

                setBossSlain(context, boss, isBossSlain);

                return 0;
            })))));


            root.then(bloodMoon).then(slain);
            dispatcher.register(root);
        });
    }

}
