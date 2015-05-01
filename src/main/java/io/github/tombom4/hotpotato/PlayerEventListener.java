package io.github.tombom4.hotpotato;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author TomBom4
 *         Main class for player EventListeners
 */
public class PlayerEventListener implements Listener {
    private HotPotatoPlugin plugin;
    private MongoStats mongoStats;

    public PlayerEventListener(HotPotatoPlugin plugin) {
        this.plugin = plugin;
        this.mongoStats = plugin.mongoStats;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        mongoStats.statsInitializer(evt.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        Player p = evt.getPlayer();
        if (p.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.WATER)) {
            plugin.game.explode();
        }
    }

    @EventHandler
    public void onPlayerGetDamage(EntityDamageByEntityEvent evt) {
        evt.setCancelled(true);
        if (evt.getDamager() instanceof Player && evt.getEntity() instanceof Player) {
            Player player = (Player) evt.getEntity();
            Player damager = (Player) evt.getDamager();
            if (damager.getName().equalsIgnoreCase(plugin.game.getPotatoPlayer().getName())/* && itemName.equalsIgnoreCase("§4Hot Potato")*/) {
                plugin.game.setPotatoPlayer(player);
                damager.getInventory().clear();
            }
        }
    }
}