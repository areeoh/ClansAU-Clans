package com.areeoh.clans.shops;

import com.areeoh.spigot.framework.Repository;
import com.areeoh.spigot.repository.InsertQuery;
import com.areeoh.spigot.repository.RepositoryManager;
import com.areeoh.spigot.utility.UtilMessage;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ShopRepository extends Repository {

    public ShopRepository(RepositoryManager manager) {
        super(manager, "ShopRepository");
    }

    @Override
    public synchronized void onEnable() {
        FindIterable<Document> documents = getMongoCollection().find();
        int count = 0;

        for (Document document : documents) {
            String shopName = document.getString("shop_name");
            String worldName = document.getString("world_name");
            Integer x = document.getInteger("x");
            Integer y = document.getInteger("y");
            Integer z = document.getInteger("z");

            Shop shop = (Shop) getManager(ShopManager.class).getModule(shopName);
            shop.spawn(new Location(Bukkit.getWorld(worldName), x, y, z, 0, 0));

            count++;
        }

        UtilMessage.broadcast("Shops", "Loaded " + count + " shops.");
    }

    public void saveShop(Shop shop) {
        Document document = new Document();
        document.append("shop_name", shop.getName());
        document.append("x", shop.getEntity().getBukkitEntity().getLocation().getX());
        document.append("y", shop.getEntity().getBukkitEntity().getLocation().getY());
        document.append("z", shop.getEntity().getBukkitEntity().getLocation().getZ());
        document.append("pitch", shop.getEntity().getBukkitEntity().getLocation().getPitch());
        document.append("yaw", shop.getEntity().getBukkitEntity().getLocation().getYaw());
        getManager().addQuery(new InsertQuery(document, this));
    }

    @Override
    public MongoCollection<Document> getMongoCollection() {
        return getMongoDatabase().getCollection("shops");
    }
}