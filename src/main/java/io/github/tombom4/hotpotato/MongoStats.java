package io.github.tombom4.hotpotato;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static io.github.tombom4.hotpotato.Game.PREFIX_1;

/**
 * This class represents a statistics system using a Mongo database
 *
 * @author TomBom4
 */
public class MongoStats {
    private MongoCollection<Document> collection;
    private HotPotatoPlugin plugin;

    /**
     * Constructor. Configures the mongo database
     * @param plugin       Plugin main class
     * @param pluginConfig FileConfiguration for getting the MongoClientURI
     */
    public MongoStats(HotPotatoPlugin plugin, FileConfiguration pluginConfig) {
        try {
            MongoClientURI uri = new MongoClientURI(pluginConfig.getString("MongoURI"));
            MongoClient client = new MongoClient(uri);
            MongoDatabase database = client.getDatabase("hotpotato-stats");
            collection = database.getCollection("players");
            this.plugin = plugin;
        } catch (MongoException e) {
            plugin.getLogger().info(PREFIX_1 + "MongoDB could not be loaded. Please check your MongoURI in the config.yml");
        } catch (NullPointerException e) {
            plugin.getLogger().info(PREFIX_1 + "MongoDB could not be loaded. Please fill in your MongoURI in the config.yml");
        }
    }

    /**
     * Increments the wins counter when a player wins
     *
     * @param p The Player that has won
     */
    public void addWin(Player p) {
        String uuid = p.getUniqueId().toString();
        Document record = new Document("$inc", new Document("wins", 1));

        Bson filter = eq("_id", uuid);

        ArrayList<Document> list = collection.find(filter).into(new ArrayList<>());

        if (list.size() < 0) {
            collection.insertOne(record);
        } else if (list.size() == 1) {
            collection.updateOne(filter, record);
        } else {
            p.sendMessage(PREFIX_1 + "Fehler! Melde bitte Folgende Fehlermeldung einem Admin:\n" +
                    ChatColor.YELLOW + "Unerwartete Anzahl an Einträgen für " + uuid);
        }
    }

    /**
     * Displays the game statistics of a player
     *
     * @param cs   The command sender
     * @param uuid The uuid of the player whose stats should be shown
     */
    public void stats(CommandSender cs, UUID uuid) {
        Bson filter = eq("_id", uuid.toString());
        Document doc = collection.find(filter).first();
        int wins = doc.getInteger("wins");
        String name = doc.getString("name");
        cs.sendMessage(PREFIX_1 + "Stats für " + ChatColor.YELLOW + name + ":");
        cs.sendMessage(ChatColor.GREEN + "Wins: " + ChatColor.YELLOW + wins);
    }

    /**
     * Overloaded stats(CommandSender, UUID) when the CommandSender and the player are the same
     *
     * @param p The player
     */
    public void stats(Player p) {
        stats(p, p.getUniqueId());
    }

    /**
     * Overloaded stats(CommandSender, UUID) for getting the player by a UUID String
     *
     * @param cs   The CommandSender
     * @param UUID The UUID of the player as a String
     */
    public void stats(CommandSender cs, String UUID) {
        stats(cs, java.util.UUID.fromString(UUID));
    }

    /**
     * Creates a stats document in the database for a player (if it doesn't exist) on login
     *
     * @param p The player
     */
    public void statsInitializer(Player p) {
        String uuid = p.getUniqueId().toString();
        Bson filter = eq("_id", uuid);
        long counter = collection.count(filter);
        if (counter < 1) {
            Document doc = new Document("_id", uuid)
                    .append("wins", 0)
                    .append("name", p.getDisplayName());
            collection.insertOne(doc);
        }
    }

    /**
     * Checks if the database has an entry with a specified name and returns the uuid of the player (as a String) if true.
     *
     * @param playerName The name of the player
     * @return The uuid of the given player or null if there is no entry
     */
    public String hasEntry(String playerName) {
        Bson filter = eq("name", playerName);
        ArrayList<Document> doc = collection.find(filter).into(new ArrayList<>());
        int counter = doc.size();
        return counter > 0 ? doc.get(0).getString("_id") : null;
    }
}