package net.worldmc.morpheus;

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

    private static Morpheus instance;
    private String prefix;
    private String textColor;
    private MiniMessage miniMessage;
    private Map<TitleDuration, Title.Times> titleDurations;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
    }

    private void loadConfig() {
        saveDefaultConfig();
        prefix = getConfig().getString("prefix", "<gray>[<b><gradient:#00AA00:#FFAA00>WorldMC</gradient></b>] ");
        textColor = getConfig().getString("text-color", "<green>");

        miniMessage = MiniMessage.miniMessage();

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
     * Gets the API instance for the Morpheus plugin.
     *
     * @return The MorpheusAPI instance.
     */
    public static MorpheusAPI getAPI() {
        return instance;
    }

    /**
     * Processes a message by applying the prefix (if specified) and text color.
     *
     * @param message The message to process.
     * @param usePrefix Whether to include the prefix in the processed message.
     * @return A Component representing the processed message.
     */
    private Component processMessage(String message, boolean usePrefix) {
        String processedMessage = usePrefix ? prefix + textColor + message : textColor + message;
        return miniMessage.deserialize(processedMessage);
    }

    /**
     * Sends a global message to all online players.
     *
     * @param message The message to send.
     * @param usePrefix Whether to include the prefix in the message.
     */
    public void sendGlobalMessage(String message, boolean usePrefix) {
        Component parsedMessage = processMessage(message, usePrefix);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(parsedMessage));
    }

    /**
     * Sends a message to a specific player.
     *
     * @param player The player to send the message to.
     * @param message The message to send.
     * @param usePrefix Whether to include the prefix in the message.
     */
    public void sendPlayerMessage(Player player, String message, boolean usePrefix) {
        Component parsedMessage = processMessage(message, usePrefix);
        player.sendMessage(parsedMessage);
    }

    /**
     * Sends a global title and subtitle to all online players.
     *
     * @param title The title to display.
     * @param subtitle The subtitle to display.
     * @param duration The duration to display the title for.
     */
    public void sendGlobalTitle(String title, String subtitle, TitleDuration duration) {
        Component parsedTitle = processMessage(title, false);
        Component parsedSubtitle = processMessage(subtitle, false);

        Title titleObj = Title.title(parsedTitle, parsedSubtitle, titleDurations.get(duration));

        Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(titleObj));
    }

    /**
     * Sends a title and subtitle to a specific player.
     *
     * @param player The player to send the title to.
     * @param title The title to display.
     * @param subtitle The subtitle to display.
     * @param duration The duration to display the title for.
     */
    public void sendPlayerTitle(Player player, String title, String subtitle, TitleDuration duration) {
        Component parsedTitle = processMessage(title, false);
        Component parsedSubtitle = processMessage(subtitle, false);

        Title titleObj = Title.title(parsedTitle, parsedSubtitle, titleDurations.get(duration));

        player.showTitle(titleObj);
    }

    /**
     * Sends a global action bar message to all online players.
     *
     * @param message The message to display in the action bar.
     */
    public void sendGlobalActionBar(String message) {
        Component parsedMessage = processMessage(message, false);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(parsedMessage));
    }

    /**
     * Sends an action bar message to a specific player.
     *
     * @param player The player to send the action bar message to.
     * @param message The message to display in the action bar.
     */
    public void sendPlayerActionBar(Player player, String message) {
        Component parsedMessage = processMessage(message, false);
        player.sendActionBar(parsedMessage);
    }
}