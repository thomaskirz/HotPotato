package io.github.tombom4.hotpotato;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Random;

/**
 * Important methods for the game
 * @author TomBom4
 */
public class Game implements EventListener {
    private HotPotatoPlugin plugin;
    private List<Player> possiblePlayers = new ArrayList<>();
    private Player potatoPlayer;

    public static final String PREFIX_1 = ChatColor.RED + "[HotPotato] " + ChatColor.GREEN;
    public static final String PREFIX_2 = ChatColor.RED + "[HotPotato] " + ChatColor.YELLOW;

    public Game(HotPotatoPlugin plugin) {
        this.plugin = plugin;
    }

    public List<Player> getPossiblePlayers() {
        return possiblePlayers;
    }

    public void addPossiblePlayer(Player p) {
        possiblePlayers.add(p);
    }

    public Player getRandomPlayer() {
        int random = new Random().nextInt(possiblePlayers.size());
        return possiblePlayers.get(random);
    }

    public Player getPotatoPlayer() {
        return this.potatoPlayer;
    }

    public void setPotatoPlayer(Player p) {
        this.potatoPlayer = p;
        Bukkit.getServer().broadcastMessage(PREFIX_2 + potatoPlayer.getName() + ChatColor.GREEN + " hat die Kartoffel");

        Inventory inv = potatoPlayer.getInventory();

        ItemStack potato = new ItemStack(Material.POTATO_ITEM);
        ItemMeta imPotato = potato.getItemMeta();
        imPotato.setDisplayName(ChatColor.RED + "Hot Potato");
        List<String> lores = new ArrayList<>();
        lores.add("Du hast die Kartoffel");
        lores.add("Linksklicke mit ihr einen Spieler, um sie weiterzugeben");
        imPotato.setLore(lores);
        potato.setItemMeta(imPotato);

        inv.clear();
        inv.addItem(potato);
    }

    public void startNewRound(HotPotatoPlugin plugin) {
        for (Player p : possiblePlayers) {
//            plugin.getServer().getPluginManager().registerEvents(new Trap(), plugin);
            p.setGameMode(GameMode.ADVENTURE);
            p.setHealth(20);
            p.setSaturation(1000000);
            p.setAllowFlight(false);
//            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 2, true, false));
            p.getInventory().clear();
        }

        plugin.getServer().getPluginManager().registerEvents(new PlayerEventListener(), plugin);

        this.potatoPlayer = getRandomPlayer();
        setPotatoPlayer(this.potatoPlayer);
        new DeathCountownTask(plugin, 25).runTaskTimer(plugin, 0, 20);
    }

    public void explode(Player p) {
        Location loc = p.getLocation();
        World world = loc.getWorld();
        world.createExplosion(loc, 0f);

        p.setGameMode(GameMode.SPECTATOR);
        p.sendMessage(PREFIX_1 + "Du bist gestorben und aus dem Spiel ausgeschieden.");
        p.removePotionEffect(PotionEffectType.SPEED);

        possiblePlayers.remove(p);

        Bukkit.getScheduler().cancelAllTasks();

        if (possiblePlayers.size() > 1) {
            plugin.getServer().getScheduler().cancelAllTasks();
            plugin.getServer().broadcastMessage(PREFIX_2 + potatoPlayer.getName() + ChatColor.GREEN + " ist ausgeschieden!");
            plugin.getServer().broadcastMessage(PREFIX_1 + "Eine neue Runde beginnt!");
            startNewRound(plugin);
        } else {
            Player winner = possiblePlayers.get(0);
            MongoStats.addWin(winner);
            plugin.getServer().broadcastMessage(PREFIX_2 + winner.getName() + ChatColor.GREEN + " hat gewonnen.");
            HandlerList.unregisterAll(new PlayerEventListener());
            Bukkit.getScheduler().cancelAllTasks();
            plugin.getServer().broadcastMessage(PREFIX_1 + "Das Spiel ist beendet");
        }
    }
}
