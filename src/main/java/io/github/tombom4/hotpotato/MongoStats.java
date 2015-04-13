package io.github.tombom4.hotpotato;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static io.github.tombom4.hotpotato.Game.PREFIX_1;

/**
 * This class represents a statistics system using a Mongo database
 *
 * @author TomBom4
 */
public class MongoStats {
    static MongoCollection<Document> collection;

    public static void initializeDatabase() {
        collection = HotPotatoPlugin.collection;
    }

    /**
     * Increments the wins counter when a player wins
     * @param p Player that won
     */
    public static void addWin(Player p) {
        String uuid = p.getUniqueId().toString();
        Document record = new Document("$inc", new Document("wins", 1));

        Bson filter = eq("_id", uuid);

        ArrayList<Document> list = collection.find(filter).into(new ArrayList<Document>());

        if (list.size() < 0) {
            collection.insertOne(record);
        } else if (list.size() == 1) {
            collection.updateOne(filter, record);
        } else {
            p.sendMessage(PREFIX_1 + "Fehler! Melde bitte Folgende Fehlermeldung einem Admin:\n" +
                    ChatColor.YELLOW + "Unerwartete Anzahl an Einträgen für " + uuid);
        }
    }

    public static void stats(CommandSender cs, Player p) {
        Bson filter = eq("_id", p.getUniqueId().toString());
        Document doc = collection.find(filter).first();
        int wins = doc.getInteger("wins");
        cs.sendMessage(PREFIX_1 + "Stats für " + ChatColor.YELLOW + p.getName() + ":");
        cs.sendMessage(ChatColor.GREEN + "Wins: " + ChatColor.YELLOW + wins);
    }

    public static void stats(Player p) {
        stats(p, p);
    }

    /**
     * Creates a stats document in the database for a player (if it doesn't exist) on login *
     */
    public static void statsInitializer(Player p) {
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
}