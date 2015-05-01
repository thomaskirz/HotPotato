package io.github.tombom4.hotpotato;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Implements the countdown until the death of HotPotatoPlayer
 *
 * @author TomBom4
 */
public class DeathCountownTask extends BukkitRunnable {
    private HotPotatoPlugin plugin;
    private int counter;

    public DeathCountownTask(HotPotatoPlugin plugin, int counter) {
        this.plugin = plugin;
        this.counter = counter;
    }

    @Override
    public void run() {
        if (counter > 0) {
            for (Player p : plugin.game.getPossiblePlayers()) {
                p.setLevel(counter);
            }
            counter--;
        } else {
            for (Player p : plugin.game.getPossiblePlayers()) {
                p.setLevel(0);
            }
            plugin.game.explode();
            this.cancel();

        }
    }
}
