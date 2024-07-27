package net.worldmc.morpheus.api;

import org.bukkit.entity.Player;

public interface MorpheusAPI {
    enum TitleDuration {
        SHORT,
        LONG
    }

    void sendGlobalMessage(String message, boolean usePrefix);
    void sendPlayerMessage(Player player, String message, boolean usePrefix);
    void sendGlobalTitle(String title, String subtitle, TitleDuration duration);
    void sendPlayerTitle(Player player, String title, String subtitle, TitleDuration duration);
    void sendGlobalActionBar(String message);
    void sendPlayerActionBar(Player player, String message);
}