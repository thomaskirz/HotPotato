package io.github.tombom4.hotpotato;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Thomas on 17.03.2015
 */
public class PlayerEventListener implements Listener {
    private HotPotatoPlugin plugin;
    private MongoStats mongoStats;

    public PlayerEventListener(HotPotatoPlugin plugin) {
        this.plugin = plugin;
        this.mongoStats = plugin.mongoStats;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void playerLoginEvent(PlayerJoinEvent evt) {
        mongoStats.statsInitializer(evt.getPlayer());
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerMove(PlayerMoveEvent evt) {
        Player p = evt.getPlayer();
        if (p.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.WATER)) {
            plugin.game.explode(p);
        }
    }
}