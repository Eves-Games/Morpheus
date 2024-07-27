package net.worldmc.morpheus;

import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.worldmc.morpheus.api.MorpheusAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public final class Morpheus extends JavaPlugin implements MorpheusAPI {

    public enum TitleDuration {
        SHORT,
        LONG
    }

    private static Morpheus instance;
    private String prefix;
    private MiniMessage miniMessage;
    private String primaryColor;
    private String secondaryColor;
    private Map<TitleDuration, Title.Times> titleDurations;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Morpheus getInstance() {
        return instance;
    }

    public static MorpheusAPI getAPI() {
        return getInstance();
    }

    private void loadConfig() {
        saveDefaultConfig();
        prefix = getConfig().getString("prefix", "<gray>[<b><gradient:#00AA00:#FFAA00>WorldMC</gradient></b>] ");

        Map<String, String> colorTags = new HashMap<>();
        primaryColor = getConfig().getString("colors.primary", "<green>");
        secondaryColor = getConfig().getString("colors.secondary", "<blue>");
        colorTags.put("secondary", getConfig().getString("colors.secondary", "<blue>"));
        colorTags.put("accent", getConfig().getString("colors.accent", "<red>"));
        colorTags.put("neutral", getConfig().getString("colors.neutral", "<dark_gray>"));

        TagResolver colorResolver = TagResolver.builder()
                .resolvers(colorTags.entrySet().stream()
                        .map(entry -> TagResolver.resolver(entry.getKey(),
                                Tag.inserting(MiniMessage.miniMessage().deserialize(entry.getValue()))))
                        .toArray(TagResolver[]::new))
                .build();

        miniMessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(colorResolver)
                        .build())
                .build();

        titleDurations = new HashMap<>();
        titleDurations.put(TitleDuration.LONG, Title.Times.times(
                Duration.ofMillis(getConfig().getLong("title-durations.long.fade-in", 20) * 50),
                Duration.ofMillis(getConfig().getLong("title-durations.long.stay", 100) * 50),
                Duration.ofMillis(getConfig().getLong("title-durations.long.fade-out", 20) * 50)
        ));
        titleDurations.put(TitleDuration.SHORT, Title.Times.times(
                Duration.ofMillis(getConfig().getLong("title-durations.short.fade-in", 10) * 50),
                Duration.ofMillis(getConfig().getLong("title-durations.short.stay", 40) * 50),
                Duration.ofMillis(getConfig().getLong("title-durations.short.fade-out", 10) * 50)
        ));
    }

    /**
     * Sends a global message to all online players.
     * @param message The message to be sent
     */
    @Override
    public void sendGlobalMessage(String message) {
        Component parsedMessage = miniMessage.deserialize(prefix + primaryColor + message);

        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(parsedMessage));
    }

    /**
     * Sends a message to a specific player.
     * @param player The player to receive the message
     * @param message The message to be sent
     */
    @Override
    public void sendPlayerMessage(Player player, String message) {
        Component parsedMessage = miniMessage.deserialize(prefix + primaryColor + message);
        player.sendMessage(parsedMessage);
    }

    /**
     * Sends a global title and subtitle to all online players with specified duration.
     * @param title The title to be sent
     * @param subtitle The subtitle to be sent
     * @param duration The duration type (SHORT or LONG)
     */
    @Override
    public void sendGlobalTitle(String title, String subtitle, TitleDuration duration) {
        Component parsedTitle = miniMessage.deserialize(primaryColor + title);
        Component parsedSubtitle = miniMessage.deserialize(secondaryColor + subtitle);

        Title titleObj = Title.title(parsedTitle, parsedSubtitle, titleDurations.get(duration));

        Bukkit.getOnlinePlayers().forEach(player ->
                player.showTitle(titleObj));
    }

    /**
     * Sends a title and subtitle to a specific player with specified duration.
     * @param player The player to receive the title
     * @param title The title to be sent
     * @param subtitle The subtitle to be sent
     * @param duration The duration type (SHORT or LONG)
     */
    @Override
    public void sendPlayerTitle(Player player, String title, String subtitle, TitleDuration duration) {
        Component parsedTitle = miniMessage.deserialize(primaryColor + title);
        Component parsedSubtitle = miniMessage.deserialize(secondaryColor + subtitle);

        Title titleObj = Title.title(parsedTitle, parsedSubtitle, titleDurations.get(duration));

        player.showTitle(titleObj);
    }

    /**
     * Sends a global action bar message to all online players.
     * @param message The message to be sent
     */
    @Override
    public void sendGlobalActionBar(String message) {
        Component parsedMessage = miniMessage.deserialize(primaryColor + message);

        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendActionBar(parsedMessage));
    }

    /**
     * Sends an action bar message to a specific player.
     * @param player The player to receive the message
     * @param message The message to be sent
     */
    @Override
    public void sendPlayerActionBar(Player player, String message) {
        Component parsedMessage = miniMessage.deserialize(primaryColor + message);
        player.sendActionBar(parsedMessage);
    }
}