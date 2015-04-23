package io.github.tombom4.hotpotato;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import static io.github.tombom4.hotpotato.Game.PREFIX_1;

/**
 * @author TomBom4
 */
public class StartCountDownTask extends BukkitRunnable {
    private HotPotatoPlugin plugin;
    private int counter;

    public StartCountDownTask(HotPotatoPlugin plugin, int counter) {
        this.plugin = plugin;
        plugin.game = new Game(plugin);
        if (counter < 1) {
            throw new IllegalArgumentException("counter must be greater than 0");
        } else {
            this.counter = counter;
        }
    }

    @Override
    public void run() {
        if (counter > 0) {
            plugin.getServer().broadcastMessage(PREFIX_1 + "Das Spiel beginnt in " + ChatColor.YELLOW + counter-- + ChatColor.GREEN + " Sekunden.");
        } else {
            plugin.getServer().broadcastMessage(PREFIX_1 + "Das Spiel hat begonnen.");
            this.cancel();
            plugin.game.startNewRound(this.plugin);
        }
    }

}