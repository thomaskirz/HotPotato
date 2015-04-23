package io.github.tombom4.hotpotato;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * Created by Thomas on 16.03.2015.
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
            plugin.game.explode(plugin.game.getPotatoPlayer());
            this.cancel();

        }
    }
}
