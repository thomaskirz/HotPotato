package io.github.tombom4.hotpotato;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

import static io.github.tombom4.hotpotato.Game.PREFIX_1;

/**
 * Generate an arena of glass with a given radius
 *
 * @author TomBom4
 */
public class ArenaGenerator implements CommandExecutor {
    /**
     * Plugin main class
     */
    public HotPotatoPlugin plugin;

    /**
     * Constructor. Initializes plugin field
     *
     * @param plugin the HotPotatoPlugin
     */
    public ArenaGenerator(HotPotatoPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("generatearena")) {
            if (args.length == 1) {
                if (commandSender instanceof Player) {
                    Player p = (Player) commandSender;
                    try {
                        int radius = Integer.parseInt(args[0]);
                        Location center = p.getLocation();

                        ArrayList<Location> circle = getHemisphereLocations(center, radius);
                        circle.forEach(location -> location.getBlock().setType(Material.GLASS));
                        p.sendMessage(PREFIX_1 + circle.size() + " Blocks updated.");
                    } catch (NumberFormatException e) {
                        return false;
                    } catch (Exception e) {
                        p.sendMessage(PREFIX_1 + "Ein unbekannter Fehler ist aufgetreten. Bitte melde dies einem Admin!");
                        plugin.getLogger().info(Arrays.toString(e.getStackTrace()));
                    }
                    return true;
                } else {
                    commandSender.sendMessage(PREFIX_1 + "Nur Spieler duerfen diesen Befehl ausführen!");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Generate the locations of the hemisphere
     *
     * @param center Center of the hemisphere
     * @param radius Radius of the hemisphere
     * @return The ArrayList containing the locations of the hemisphere
     */
    public ArrayList<Location> getHemisphereLocations(Location center, int radius) {
        ArrayList<Location> locations = new ArrayList<>();
        World world = center.getWorld();

        Vector centerVector = new BlockVector(center.getBlockX(), center.getBlockY(), center.getBlockZ());


        int x1 = center.getBlockX() - radius,
                x2 = center.getBlockX() + radius,
                y1 = center.getBlockY(),
                y2 = center.getBlockY() + radius,
                z1 = center.getBlockZ() - radius,
                z2 = center.getBlockZ() + radius;

        // Iterate through the sphere
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Vector vecHere = new BlockVector(x, y, z);
                    Location locHere = new Location(world, x, y, z);
                    if (vecHere.distance(centerVector) <= radius + 0.5 && vecHere.distance(centerVector) >= radius - 0.5) {
                        locations.add(locHere);
                    }
                }
            }
        }

        return locations;
    }

}
