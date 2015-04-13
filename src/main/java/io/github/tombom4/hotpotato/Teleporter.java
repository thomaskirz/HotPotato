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
 * Implement a command specifying blocks that teleport players to another block when they run over it
 */
public class Teleporter implements CommandExecutor, Listener {
    static HashMap<List<Integer>, Location> teleporters = new HashMap<>();

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleporter")) {
            if (cs instanceof Player) {
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

    @SuppressWarnings("unused")
    @EventHandler
    public void playerMoveListener(PlayerMoveEvent evt) {
        Player p = evt.getPlayer();
        Location loc = p.getLocation();
        List locFirst = Arrays.asList(loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ());

        if (teleporters.containsKey(locFirst)) {
            p.teleport(teleporters.get(locFirst));}

    }
}
