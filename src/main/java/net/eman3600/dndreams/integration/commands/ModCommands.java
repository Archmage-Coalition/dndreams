package net.eman3600.dndreams.integration.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.eman3600.dndreams.cardinal_components.*;
import net.eman3600.dndreams.entities.mobs.FacelessEntity;
import net.eman3600.dndreams.initializers.cca.EntityComponents;
import net.eman3600.dndreams.initializers.cca.WorldComponents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
    public static CommandSuggestions stormSuggestions = new CommandSuggestions(List.of("reset", "clear", "onset", "storming", "recovery"));


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
    }

    protected static String displayStormState(CommandContext<ServerCommandSource> context, String state) {
        ServerWorld world = context.getSource().getWorld();

        return "storm." + state;
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

    protected static void setStormState(CommandContext<ServerCommandSource> context, String state) {
        ServerWorld world = context.getSource().getWorld();
        DarkStormComponent storm = WorldComponents.DARK_STORM.get(world.getScoreboard());

        switch (state) {
            case "reset" -> storm.startState(-1);
            case "clear" -> storm.startState(0);
            case "onset" -> storm.startState(1);
            case "storming" -> storm.startState(2);
            case "recovery" -> storm.startState(3);
            default -> {
                displayError(context, "storm.invalid");
                return;
            }
        }

        displayFeedback(context, displayStormState(context, state), true);
    }
    protected static void setStormState(CommandContext<ServerCommandSource> context, String state, int ticks) {
        ServerWorld world = context.getSource().getWorld();
        DarkStormComponent storm = WorldComponents.DARK_STORM.get(world.getScoreboard());

        switch (state) {
            case "reset" -> storm.startState(-1);
            case "clear" -> storm.startState(0);
            case "onset" -> storm.startState(1);
            case "storming" -> storm.startState(2);
            case "recovery" -> storm.startState(3);
            default -> {
                displayError(context, "storm.invalid");
                return;
            }
        }
        storm.setStateTime(ticks);

        displayFeedback(context, displayStormState(context, state), true);
    }

    protected static void setPlayerMana(CommandContext<ServerCommandSource> context, PlayerEntity player, int amount) {
        ManaComponent mana = EntityComponents.MANA.get(player);

        mana.setMana(amount);

        displayFeedback(context, "mana.set", true, player.getDisplayName(), mana.getMana());
    }

    protected static void addPlayerMana(CommandContext<ServerCommandSource> context, PlayerEntity player, int amount) {
        ManaComponent mana = EntityComponents.MANA.get(player);

        mana.setMana(mana.getMana() + amount);

        displayFeedback(context, "mana.set", true, player.getDisplayName(), mana.getMana());
    }

    protected static int getPlayerMana(CommandContext<ServerCommandSource> context, PlayerEntity player) {
        ManaComponent mana = EntityComponents.MANA.get(player);

        displayFeedback(context, "mana.get", true, player.getDisplayName(), mana.getMana());

        return mana.getMana();
    }

    protected static float getPlayerSanity(CommandContext<ServerCommandSource> context, PlayerEntity player) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        displayFeedback(context, "sanity.get", true, player.getDisplayName(), torment.getSanity());

        return torment.getSanity();
    }

    protected static float getPlayerMaxSanity(CommandContext<ServerCommandSource> context, PlayerEntity player) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        displayFeedback(context, "sanity.get_max", true, player.getDisplayName(), torment.getMaxSanity());

        return torment.getMaxSanity();
    }

    protected static void setPlayerSanity(CommandContext<ServerCommandSource> context, PlayerEntity player, float amount) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        torment.setSanity(amount);

        displayFeedback(context, "sanity.set", true, player.getDisplayName(), torment.getSanity());
    }

    protected static void addPlayerSanity(CommandContext<ServerCommandSource> context, PlayerEntity player, float amount) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        torment.lowerSanity(-amount);

        displayFeedback(context, "sanity.set", true, player.getDisplayName(), torment.getSanity());
    }

    protected static void setPlayerMaxSanity(CommandContext<ServerCommandSource> context, PlayerEntity player, float amount) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        torment.setMaxSanity(amount);

        displayFeedback(context, "sanity.set_max", true, player.getDisplayName(), torment.getMaxSanity());
    }

    protected static void addPlayerMaxSanity(CommandContext<ServerCommandSource> context, PlayerEntity player, float amount) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        torment.lowerMaxSanity(-amount);

        displayFeedback(context, "sanity.set_max", true, player.getDisplayName(), torment.getMaxSanity());
    }

    protected static void resetAcharosCooldown(CommandContext<ServerCommandSource> context, PlayerEntity player) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        if (torment.getFacelessCooldown() > 0) {
            torment.setFacelessCooldown(0);
            displayFeedback(context, "acharos.reset_cooldown", true, player.getDisplayName());
        } else {
            displayFeedback(context, "acharos.reset_cooldown.no_effect", true, player.getDisplayName());
        }
    }

    protected static int acharosEvent(CommandContext<ServerCommandSource> context, PlayerEntity player) {
        TormentComponent torment = EntityComponents.TORMENT.get(player);

        if (FacelessEntity.daylightAt(player.world, player.getBlockPos())) {
            displayError(context, "acharos.event.sunlight", player.getDisplayName());
            return 0;
        }
        Entity priorEntity = torment.getFacelessEntity();

        int spawns = torment.triggerFacelessEvent();

        if (spawns > 0) {
            displayFeedback(context, "acharos.event", true);
            if (priorEntity != null) {
                priorEntity.discard();
            }
        } else {
            displayError(context, "acharos.event.failure");
        }

        return spawns;
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("dndreams");



            LiteralArgumentBuilder<ServerCommandSource> bloodMoon = CommandManager.literal("dreadmoon").requires((context) -> context.hasPermissionLevel(2));

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




            LiteralArgumentBuilder<ServerCommandSource> darkStorm = CommandManager.literal("storm").requires((context) -> context.hasPermissionLevel(2));

            darkStorm.then(CommandManager.argument("state", StringArgumentType.word()).suggests(stormSuggestions).executes(context -> {
                String state = context.getArgument("state", String.class);

                setStormState(context, state);

                return 0;
            }).then(CommandManager.argument("ticks", IntegerArgumentType.integer(0, 72000)).executes(context -> {
                String state = context.getArgument("state", String.class);
                int ticks = context.getArgument("ticks", Integer.class);

                setStormState(context, state, ticks);

                return 0;
            })));




            LiteralArgumentBuilder<ServerCommandSource> mana = CommandManager.literal("mana").requires(context -> context.hasPermissionLevel(2));

            mana.then(CommandManager.literal("set").then(CommandManager.argument("target", EntityArgumentType.players()).then(CommandManager.argument("amount", IntegerArgumentType.integer(0)).executes(context -> {
                EntitySelector selector = context.getArgument("target", EntitySelector.class);
                int amount = context.getArgument("amount", Integer.class);

                for (PlayerEntity player: selector.getPlayers(context.getSource())) {

                    setPlayerMana(context, player, amount);
                }

                return 0;
            }))));

            mana.then(CommandManager.literal("add").then(CommandManager.argument("target", EntityArgumentType.players()).then(CommandManager.argument("amount", IntegerArgumentType.integer()).executes(context -> {
                EntitySelector selector = context.getArgument("target", EntitySelector.class);
                int amount = context.getArgument("amount", Integer.class);

                for (PlayerEntity player: selector.getPlayers(context.getSource())) {

                    addPlayerMana(context, player, amount);
                }

                return 0;
            }))));

            mana.then(CommandManager.literal("get").then(CommandManager.argument("target", EntityArgumentType.player()).executes(context -> {
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                PlayerEntity player = selector.getPlayer(context.getSource());

                return getPlayerMana(context, player);
            })).executes(context -> getPlayerMana(context, context.getSource().getPlayerOrThrow())));




            LiteralArgumentBuilder<ServerCommandSource> sanity = CommandManager.literal("sanity").requires(context -> context.hasPermissionLevel(2));

            sanity.then(CommandManager.literal("set").then(CommandManager.argument("target", EntityArgumentType.players()).then(CommandManager.argument("amount", FloatArgumentType.floatArg(0, 100)).executes(context -> {
                float amount = context.getArgument("amount", Float.class);
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                for (PlayerEntity player: selector.getPlayers(context.getSource())) {

                    setPlayerSanity(context, player, amount);
                }

                return 0;
            }))));

            sanity.then(CommandManager.literal("add").then(CommandManager.argument("target", EntityArgumentType.players()).then(CommandManager.argument("amount", FloatArgumentType.floatArg()).executes(context -> {
                float amount = context.getArgument("amount", Float.class);
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                for (PlayerEntity player: selector.getPlayers(context.getSource())) {

                    addPlayerSanity(context, player, amount);
                }

                return 0;
            }))));

            sanity.then(CommandManager.literal("set_max").then(CommandManager.argument("target", EntityArgumentType.players()).then(CommandManager.argument("amount", FloatArgumentType.floatArg(30, 100)).executes(context -> {
                float amount = context.getArgument("amount", Float.class);
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                for (PlayerEntity player: selector.getPlayers(context.getSource())) {

                    setPlayerMaxSanity(context, player, amount);
                }

                return 0;
            }))));

            sanity.then(CommandManager.literal("add_max").then(CommandManager.argument("target", EntityArgumentType.players()).then(CommandManager.argument("amount", FloatArgumentType.floatArg()).executes(context -> {
                float amount = context.getArgument("amount", Float.class);
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                for (PlayerEntity player: selector.getPlayers(context.getSource())) {

                    addPlayerMaxSanity(context, player, amount);
                }

                return 0;
            }))));

            sanity.then(CommandManager.literal("get").then(CommandManager.argument("target", EntityArgumentType.player()).executes(context -> {
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                return (int) getPlayerSanity(context, selector.getPlayer(context.getSource()));
            })).executes(context -> (int) getPlayerSanity(context, context.getSource().getPlayerOrThrow())));

            sanity.then(CommandManager.literal("get_max").then(CommandManager.argument("target", EntityArgumentType.player()).executes(context -> {
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                return (int) getPlayerMaxSanity(context, selector.getPlayer(context.getSource()));
            })).executes(context -> (int) getPlayerMaxSanity(context, context.getSource().getPlayerOrThrow())));




            LiteralArgumentBuilder<ServerCommandSource> acharos = CommandManager.literal("acharos").requires(context -> context.hasPermissionLevel(2));

            acharos.then(CommandManager.literal("reset_cooldown").then(CommandManager.argument("target", EntityArgumentType.player()).executes(context -> {
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                resetAcharosCooldown(context, selector.getPlayer(context.getSource()));

                return 0;
            })).executes(context -> {
                resetAcharosCooldown(context, context.getSource().getPlayerOrThrow());

                return 0;
            }));

            acharos.then(CommandManager.literal("event").then(CommandManager.argument("target", EntityArgumentType.player()).executes(context -> {
                EntitySelector selector = context.getArgument("target", EntitySelector.class);

                return acharosEvent(context, selector.getPlayer(context.getSource()));
            })).executes(context -> {

                return acharosEvent(context, context.getSource().getPlayerOrThrow());
            }));



            root.then(bloodMoon).then(slain).then(darkStorm).then(mana).then(sanity).then(acharos);
            dispatcher.register(root);
        });
    }

}
