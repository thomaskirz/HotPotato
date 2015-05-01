package io.github.tombom4.hotpotato;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin main class
 *
 * @author TomBom4
 */
public class HotPotatoPlugin extends JavaPlugin {
    public MongoStats mongoStats;
    public Game game;
    public FileConfiguration config;
    public boolean useStats = false;

    @Override
    public void onEnable() {
        loadConfig();
        if (useStats = config.getBoolean("UseStats"))
            mongoStats = new MongoStats(this, config);

        getCommand("HotPotato").setExecutor(new HotPotatoCommandExecutor(this));
        getCommand("wins").setExecutor(new HotPotatoCommandExecutor(this));
        getCommand("teleporter").setExecutor(new Teleporter(this));
        getCommand("generatearena").setExecutor(new ArenaGenerator(this));
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
    }

    /**
     * Loads the FileConfiguration
     */
    public void loadConfig() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }
}
