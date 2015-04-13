package io.github.tombom4.hotpotato;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static io.github.tombom4.hotpotato.Game.PREFIX_1;

/**
 * Created by Thomas on 17.03.2015
 */
public class PlayerEventListener implements Listener {
    @EventHandler
    @SuppressWarnings("unused")
    public void PlayerGetDamageListener(EntityDamageByEntityEvent evt) {
        evt.setCancelled(true);
        if (evt.getDamager() instanceof Player && evt.getEntity() instanceof Player) {
            Player player = (Player) evt.getEntity();
            Player damager = (Player) evt.getDamager();
//            String itemName = damager.getItemInHand().getItemMeta().getDisplayName();
            if (damager.getName().equalsIgnoreCase(StartCountDownTask.game.getPotatoPlayer().getName())/* && itemName.equalsIgnoreCase("§4Hot Potato")*/) {
                StartCountDownTask.game.setPotatoPlayer(player);
                damager.getInventory().clear();
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void playerLoginEvent(PlayerJoinEvent evt) {
        MongoStats.statsInitializer(evt.getPlayer());
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerMove(PlayerMoveEvent evt) {
        Player p = evt.getPlayer();
        if (p.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.WATER)) {
            StartCountDownTask.game.explode(p);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player p = evt.getPlayer();
        if (p.getName().equalsIgnoreCase("Brokoli_HD")) {
            p.sendMessage(PREFIX_1 + "Jonas, du darfst hier bauen c:");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent evt) {
        Player p = evt.getPlayer();
        String name = p.getName();
        if (!(name == "TomBom4" || name == "Alex_Bamuda" || name == "Brokoli_HD" || name == "Wangi_")) {
            evt.setCancelled(false);
        }
    }
}