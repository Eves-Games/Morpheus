package net.worldmc.morpheus.api;

import org.bukkit.entity.Player;
import net.worldmc.morpheus.Morpheus.TitleDuration;

public interface MorpheusAPI {
    void sendGlobalMessage(String message);
    void sendPlayerMessage(Player player, String message);
    void sendGlobalTitle(String title, String subtitle, TitleDuration duration);
    void sendPlayerTitle(Player player, String title, String subtitle, TitleDuration duration);
    void sendGlobalActionBar(String message);
    void sendPlayerActionBar(Player player, String message);
}