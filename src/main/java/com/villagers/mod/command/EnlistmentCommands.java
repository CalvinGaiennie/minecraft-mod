package com.villagers.mod.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.armies.BanditGenerationScheduler;
import com.villagers.mod.armies.BanditSiteGenerator;
import com.villagers.mod.armies.BanditWorldSavedData;
import com.villagers.mod.player.AllySavedData;
import com.villagers.mod.player.EnlistmentService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class EnlistmentCommands {
    private EnlistmentCommands() {
    }

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("villagers")
                        .then(Commands.literal("enlist").executes(ctx -> {
                            EnlistmentService.enlist(ctx.getSource().getPlayerOrException());
                            ctx.getSource().sendSuccess(() -> Component.translatable("message.villagers.enlisted"), true);
                            return 1;
                        }))
                        .then(Commands.literal("optout").executes(ctx -> {
                            EnlistmentService.optOut(ctx.getSource().getPlayerOrException());
                            ctx.getSource().sendSuccess(() -> Component.translatable("message.villagers.opted_out"), true);
                            return 1;
                        }))
                        .then(Commands.literal("ally")
                                .then(Commands.literal("add")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    ServerPlayer self = ctx.getSource().getPlayerOrException();
                                                    ServerPlayer other = EntityArgument.getPlayer(ctx, "player");
                                                    if (AllySavedData.get(self.serverLevel()).addAlly(self.getUUID(), other.getUUID())) {
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.translatable("message.villagers.ally_added", other.getDisplayName()),
                                                                true);
                                                        return 1;
                                                    }
                                                    ctx.getSource().sendFailure(Component.translatable("message.villagers.ally_already"));
                                                    return 0;
                                                })))
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    ServerPlayer self = ctx.getSource().getPlayerOrException();
                                                    ServerPlayer other = EntityArgument.getPlayer(ctx, "player");
                                                    if (AllySavedData.get(self.serverLevel()).removeAlly(self.getUUID(), other.getUUID())) {
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.translatable("message.villagers.ally_removed", other.getDisplayName()),
                                                                true);
                                                        return 1;
                                                    }
                                                    ctx.getSource().sendFailure(Component.translatable("message.villagers.ally_not_listed"));
                                                    return 0;
                                                })))
                                .then(Commands.literal("list").executes(ctx -> {
                                    ServerPlayer self = ctx.getSource().getPlayerOrException();
                                    var allies = AllySavedData.get(self.serverLevel()).getAllies(self.getUUID());
                                    if (allies.isEmpty()) {
                                        ctx.getSource().sendSuccess(() -> Component.translatable("message.villagers.ally_list_empty"), false);
                                        return 0;
                                    }
                                    ctx.getSource().sendSuccess(
                                            () -> Component.translatable("message.villagers.ally_list_header", allies.size()),
                                            false);
                                    for (UUID id : allies) {
                                        var listed = ctx.getSource().getServer().getPlayerList().getPlayer(id);
                                        Component name = listed != null
                                                ? listed.getDisplayName()
                                                : Component.literal(id.toString());
                                        ctx.getSource().sendSuccess(() -> name, false);
                                    }
                                    return allies.size();
                                })))
                        .then(Commands.literal("locate")
                                .then(buildLocateKind("camp", k -> k == BanditWorldSavedData.SiteKind.CAMP))
                                .then(buildLocateKind("hideout", k -> k == BanditWorldSavedData.SiteKind.HIDEOUT))
                                .then(buildLocateKind("corvin", k -> k == BanditWorldSavedData.SiteKind.CORVIN))
                                .then(buildLocateKind("garland", k -> k == BanditWorldSavedData.SiteKind.GARLAND))
                                .then(buildLocateKind("bandit", EnlistmentCommands::isBanditSite))
                                .then(Commands.literal("list")
                                        .executes(ctx -> listBanditSites(ctx.getSource(), EnlistmentCommands::isBanditSite))
                                        .then(Commands.literal("camp").executes(ctx -> listBanditSites(ctx.getSource(), k -> k == BanditWorldSavedData.SiteKind.CAMP)))
                                        .then(Commands.literal("hideout").executes(ctx -> listBanditSites(ctx.getSource(), k -> k == BanditWorldSavedData.SiteKind.HIDEOUT)))
                                        .then(Commands.literal("corvin").executes(ctx -> listBanditSites(ctx.getSource(), k -> k == BanditWorldSavedData.SiteKind.CORVIN)))
                                        .then(Commands.literal("garland").executes(ctx -> listBanditSites(ctx.getSource(), k -> k == BanditWorldSavedData.SiteKind.GARLAND)))
                                        .then(Commands.literal("bandit").executes(ctx -> listBanditSites(ctx.getSource(), EnlistmentCommands::isBanditSite))))
                                .executes(ctx -> {
                                    ctx.getSource().sendFailure(Component.translatable("commands.villagers.locate.usage"));
                                    return 0;
                                })));
    }

    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> buildLocateKind(
            String name, Predicate<BanditWorldSavedData.SiteKind> kindFilter) {
        SuggestionProvider<CommandSourceStack> indexSuggestions = (ctx, builder) -> suggestSiteIndices(ctx, builder, kindFilter);
        return Commands.literal(name)
                .executes(ctx -> locateBanditSite(ctx.getSource(), kindFilter))
                .then(Commands.literal("list").executes(ctx -> listBanditSites(ctx.getSource(), kindFilter)))
                .then(Commands.literal("tp")
                        .then(Commands.argument("index", IntegerArgumentType.integer(1, 128))
                                .suggests(indexSuggestions)
                                .executes(ctx -> tpToListedSite(
                                        ctx.getSource(),
                                        kindFilter,
                                        IntegerArgumentType.getInteger(ctx, "index")))));
    }

    private static boolean isBanditSite(BanditWorldSavedData.SiteKind kind) {
        return kind == BanditWorldSavedData.SiteKind.CAMP
                || kind == BanditWorldSavedData.SiteKind.HIDEOUT
                || kind == BanditWorldSavedData.SiteKind.CORVIN
                || kind == BanditWorldSavedData.SiteKind.GARLAND;
    }

    private static boolean prepareLocate(CommandSourceStack source, boolean flushGeneration) {
        ServerLevel level = source.getLevel();
        if (!level.dimension().equals(Level.OVERWORLD)) {
            source.sendFailure(Component.translatable("commands.villagers.locate.overworld_only"));
            return false;
        }
        if (flushGeneration
                && (BanditSiteGenerator.needsGeneration(level) || BanditGenerationScheduler.isRunning(level))) {
            BanditGenerationScheduler.runUntilDoneOrTimeout(level, 500);
        }
        return true;
    }

    private static BlockPos locateOrigin(CommandSourceStack source, ServerLevel level) {
        return source.getEntity() != null ? source.getEntity().blockPosition() : level.getSharedSpawnPos();
    }

    private static List<BanditWorldSavedData.SiteRecord> sortedMatches(
            CommandSourceStack source, Predicate<BanditWorldSavedData.SiteKind> kindFilter) {
        ServerLevel level = source.getLevel();
        BlockPos from = locateOrigin(source, level);
        BanditWorldSavedData data = BanditWorldSavedData.get(level);
        List<BanditWorldSavedData.SiteRecord> matches = new ArrayList<>();
        for (BanditWorldSavedData.SiteRecord site : data.sites()) {
            if (kindFilter.test(site.kind())) {
                matches.add(site);
            }
        }
        matches.sort(Comparator.comparingDouble(site -> from.distSqr(site.origin())));
        return matches;
    }

    private static CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> suggestSiteIndices(
            CommandContext<CommandSourceStack> ctx,
            SuggestionsBuilder builder,
            Predicate<BanditWorldSavedData.SiteKind> kindFilter) {
        CommandSourceStack source = ctx.getSource();
        if (!source.getLevel().dimension().equals(Level.OVERWORLD)) {
            return builder.buildFuture();
        }
        List<BanditWorldSavedData.SiteRecord> matches = sortedMatches(source, kindFilter);
        List<String> options = new ArrayList<>(matches.size());
        for (int i = 1; i <= matches.size(); i++) {
            options.add(Integer.toString(i));
        }
        return SharedSuggestionProvider.suggest(options, builder);
    }

    private static String tpRunCommand(BlockPos pos) {
        return String.format(Locale.ROOT, "/tp @s %d %d %d", pos.getX(), pos.getY(), pos.getZ());
    }

    private static MutableComponent withClickTeleport(MutableComponent line, BlockPos pos) {
        return line.withStyle(style -> style
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, tpRunCommand(pos)))
                .withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.villagers.locate.tp_click"))));
    }

    private static int locateBanditSite(CommandSourceStack source, Predicate<BanditWorldSavedData.SiteKind> kindFilter) {
        if (!prepareLocate(source, true)) {
            return 0;
        }
        List<BanditWorldSavedData.SiteRecord> matches = sortedMatches(source, kindFilter);
        if (matches.isEmpty()) {
            source.sendFailure(Component.translatable("commands.villagers.locate.not_found"));
            return 0;
        }
        BanditWorldSavedData.SiteRecord site = matches.getFirst();
        BlockPos pos = site.origin();
        BlockPos from = locateOrigin(source, source.getLevel());
        int dist = (int) Math.sqrt(from.distSqr(pos));
        MutableComponent message = withClickTeleport(
                Component.translatable(
                        "commands.villagers.locate.success",
                        site.kind().name().toLowerCase(),
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        dist),
                pos);
        source.sendSuccess(() -> message, false);
        source.sendSuccess(
                () -> Component.translatable("commands.villagers.locate.tp_hint", tpRunCommand(pos)), false);
        return 1;
    }

    private static int listBanditSites(CommandSourceStack source, Predicate<BanditWorldSavedData.SiteKind> kindFilter) {
        if (!prepareLocate(source, true)) {
            return 0;
        }
        List<BanditWorldSavedData.SiteRecord> matches = sortedMatches(source, kindFilter);
        if (matches.isEmpty()) {
            source.sendFailure(Component.translatable("commands.villagers.locate.not_found"));
            return 0;
        }
        BlockPos from = locateOrigin(source, source.getLevel());
        source.sendSuccess(() -> Component.translatable("commands.villagers.locate.list_header", matches.size()), false);
        for (int i = 0; i < matches.size(); i++) {
            BanditWorldSavedData.SiteRecord site = matches.get(i);
            BlockPos pos = site.origin();
            int dist = (int) Math.sqrt(from.distSqr(pos));
            int index = i + 1;
            source.sendSuccess(
                    () -> withClickTeleport(
                            Component.translatable(
                                    "commands.villagers.locate.list_line",
                                    index,
                                    site.kind().name().toLowerCase(),
                                    pos.getX(),
                                    pos.getY(),
                                    pos.getZ(),
                                    dist),
                            pos),
                    false);
        }
        source.sendSuccess(() -> Component.translatable("commands.villagers.locate.list_tp_hint"), false);
        return matches.size();
    }

    private static int tpToListedSite(
            CommandSourceStack source, Predicate<BanditWorldSavedData.SiteKind> kindFilter, int index) {
        if (!prepareLocate(source, true)) {
            return 0;
        }
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.translatable("commands.villagers.locate.tp_player_only"));
            return 0;
        }
        List<BanditWorldSavedData.SiteRecord> matches = sortedMatches(source, kindFilter);
        if (index < 1 || index > matches.size()) {
            source.sendFailure(Component.translatable("commands.villagers.locate.tp_bad_index", index, matches.size()));
            return 0;
        }
        BanditWorldSavedData.SiteRecord site = matches.get(index - 1);
        BlockPos pos = site.origin();
        ServerLevel overworld = source.getServer().overworld();
        player.teleportTo(overworld, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.getYRot(), player.getXRot());
        source.sendSuccess(
                () -> Component.translatable(
                        "commands.villagers.locate.tp_success",
                        index,
                        site.kind().name().toLowerCase(),
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()),
                true);
        return 1;
    }
}
