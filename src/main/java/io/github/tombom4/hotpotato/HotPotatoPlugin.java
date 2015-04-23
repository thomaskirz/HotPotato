package io.github.tombom4.hotpotato;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin main class
 * @author TomBom4
 */
public class HotPotatoPlugin extends JavaPlugin {
    public MongoClient client;
    public MongoDatabase database;
    public MongoCollection<Document> collection;
    public MongoStats mongoStats;
    public Game game;

    @Override
    public void onEnable() {
        client = new MongoClient(new MongoClientURI(Resources.MongoURI));
        database = client.getDatabase("hotpotato-stats");
        collection = database.getCollection("players");
        mongoStats = new MongoStats(collection, this);

        getCommand("HotPotato").setExecutor(new HotPotatoCommandExecutor(this));
        getCommand("wins").setExecutor(new HotPotatoCommandExecutor(this));
        getCommand("teleporter").setExecutor(new Teleporter(this));
        getCommand("generatearena").setExecutor(new ArenaGenerator(this));
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
    }

    @Override
    public void onDisable() {

    }
}
