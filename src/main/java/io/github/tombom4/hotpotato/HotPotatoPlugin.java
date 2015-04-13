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
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    @Override
    public void onEnable() {
        client = new MongoClient(new MongoClientURI(Resources.MongoURI));
        database = client.getDatabase("hotpotato-stats");
        collection = database.getCollection("players");
        getCommand("HotPotato").setExecutor(new HotPotatoCommandExecutor(this));
        getCommand("wins").setExecutor(new HotPotatoCommandExecutor(this));
        getCommand("teleporter").setExecutor(new Teleporter());
        MongoStats.initializeDatabase();
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
        getServer().getPluginManager().registerEvents(new Teleporter(), this);
    }

    @Override
    public void onDisable() {

    }
}
