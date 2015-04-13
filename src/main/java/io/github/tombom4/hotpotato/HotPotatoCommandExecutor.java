package io.github.tombom4.hotpotato;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.tombom4.hotpotato.Game.PREFIX_1;

/**
 *
 *
 */
public class HotPotatoCommandExecutor implements CommandExecutor {
    HotPotatoPlugin plugin;

    public HotPotatoCommandExecutor(HotPotatoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("HotPotato")) {
            if (args.length == 1) {
                if (plugin.getServer().getOnlinePlayers().size() > 1) {
                    try {
                        new StartCountDownTask(this.plugin, Integer.parseInt(args[0])).runTaskTimer(this.plugin, 0, 20);
                        for (Player p : this.plugin.getServer().getOnlinePlayers()) {
                            StartCountDownTask.game.addPossiblePlayer(p);
                        }
                        return true;
                    } catch (NumberFormatException e) {
                        cs.sendMessage(PREFIX_1 + "Bitte gib eine gültige Zahl ein!");
                    } catch (IllegalArgumentException e) {
                        cs.sendMessage(PREFIX_1 + "Der Countdown muss mindestens 1 sein!");
                    }
                } else {
                    cs.sendMessage(PREFIX_1 + "Es müssen mindestens 2 Spieler online sein, um HotPotato zu spielen.");
                    return true;
                }
            }
            return false;
        } else if (cmd.getName().equalsIgnoreCase("wins")) {
            if (args.length == 0) {
                if (cs instanceof Player) {
                    Player sender = (Player) cs;
                    MongoStats.stats(sender);
                    return true;
                } else {
                    cs.sendMessage(PREFIX_1 + "Only players can use this command with no arguments");
                    return true;
                }
            } else if (args.length == 1) {
                try {
                    Player player = Bukkit.getPlayer(args[0]);
                    MongoStats.stats(cs, player);
                } catch (NullPointerException e) {
                    cs.sendMessage(PREFIX_1 + "Dieser Spieler wurde nicht gefunden!");
                }
                return true;
            }
        }
        return false;
    }
}
