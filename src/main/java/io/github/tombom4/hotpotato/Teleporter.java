package io.github.tombom4.hotpotato;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static io.github.tombom4.hotpotato.Game.PREFIX_1;
import static io.github.tombom4.hotpotato.Game.PREFIX_2;

/**
 * Implements "teleporters": When a player moves to a location, it is teleported to another location
 */
public class Teleporter implements CommandExecutor, Listener {

    /**
     * A HashMap storing the locations of the teleporters and the locations where the players are teleported.
     * The coordinates of the teleporter are a List with 3 Integers. This List may be changed to a Location sometime.
     */
    static HashMap<List<Integer>, Location> teleporters = new HashMap<>();
    private HotPotatoPlugin plugin;

    public Teleporter(HotPotatoPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleporter")) {
            if (cs instanceof Player) {
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
                Player p = ((Player) cs);
                List<Integer> locFirst;
                Location locSecond;
                if (args.length == 3) {
                    Location loc = p.getLocation();
                    locFirst = Arrays.asList(loc.getBlockX(),
                            loc.getBlockY(),
                            loc.getBlockZ());

                    locSecond = new Location(loc.getWorld(),
                            Double.parseDouble(args[0]),
                            Double.parseDouble(args[1]),
                            Double.parseDouble(args[2]));
                } else if (args.length == 6) {
                    locFirst = Arrays.asList(Integer.parseInt(args[0]),
                            Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]));

                    locSecond = new Location(p.getWorld(),
                            Double.parseDouble(args[0]),
                            Double.parseDouble(args[1]),
                            Double.parseDouble(args[2]));
                } else {
                    return false;
                }
                if (teleporters.containsKey(locFirst)) {
                    p.sendMessage(PREFIX_1 + "An dieser Stelle ist schon ein Teleporter vorhanden");
                } else {
                    teleporters.put(locFirst, locSecond);
                    p.sendMessage(PREFIX_2 + "Teleporter " + ChatColor.GREEN + "aktiviert!");
                }
                return true;
            } else {
                cs.sendMessage(PREFIX_1 + " Only players can use this command");
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void playerMoveListener(PlayerMoveEvent evt) {
        Player p = evt.getPlayer();
        Location loc = p.getLocation();
        List locFirst = Arrays.asList(loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ());

        if (teleporters.containsKey(locFirst)) {
            p.teleport(teleporters.get(locFirst));
        }

    }
}
