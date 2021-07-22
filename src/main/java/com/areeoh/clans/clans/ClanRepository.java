package com.areeoh.clans.clans;

import com.areeoh.spigot.framework.Repository;
import com.areeoh.spigot.repository.DeleteQuery;
import com.areeoh.spigot.repository.InsertQuery;
import com.areeoh.spigot.repository.RepositoryManager;
import com.areeoh.spigot.repository.UpdateQuery;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClanRepository extends Repository {

    public ClanRepository(RepositoryManager manager) {
        super(manager, "ClanRepository");
    }

    @Override
    public synchronized void onEnable() {
        FindIterable<Document> documents = getMongoCollection().find();
        for (Document document : documents) {
            String name = document.getString("name");
            Clan clan = new Clan(name);

            Long age = document.getLong("age");
            Document home = (Document) document.get("home");
            String world = home.getString("world");
            Double x = home.getDouble("x");
            Double y = home.getDouble("y");
            Double z = home.getDouble("z");
            Double pitch = home.getDouble("pitch");
            Double yaw = home.getDouble("yaw");
            if(world != null && x != null && y != null && z != null && pitch != null && yaw != null) {
                clan.setHome(new Location(Bukkit.getWorld(world), x, y, z, pitch.floatValue(), yaw.floatValue()));
            }
            int energy = document.getInteger("energy");
            Boolean admin = document.getBoolean("admin");
            Boolean safe = document.getBoolean("safe");

            clan.setAge(age);
            clan.setEnergy(energy);
            clan.setAdmin(admin);
            clan.setSafe(safe);

            List<String> claims = document.getList("claims", String.class);
            clan.getClaims().addAll(claims);

            List<Document> members = (List<Document>) document.get("members");
            for (Document member : members) {
                clan.getMemberMap().put(UUID.fromString(member.getString("uuid")), Clan.MemberRole.valueOf(member.getString("role")));
            }

            List<Document> alliances = (List<Document>) document.get("alliances");
            for (Document alliance : alliances) {
                clan.getAllianceMap().put(alliance.getString("other"), alliance.getBoolean("trusted"));
            }

            List<Document> enemies = (List<Document>) document.get("enemies");
            for (Document enemy : enemies) {
                clan.getEnemyMap().put(enemy.getString("other"), enemy.getInteger("points"));
            }

            getManager(ClanManager.class).addClan(clan);
        }
    }

    public void saveClan(Clan clan) {
        Document document = new Document();
        document.append("name", clan.getName());
        document.append("age", clan.getAge());
        Document homeObject = new Document();
        homeObject.append("world", clan.getHome() == null ? null : clan.getHome().getWorld());
        homeObject.append("x", clan.getHome() == null ? null : clan.getHome().getX());
        homeObject.append("y", clan.getHome() == null ? null : clan.getHome().getY());
        homeObject.append("z", clan.getHome() == null ? null : clan.getHome().getZ());
        homeObject.append("pitch", clan.getHome() == null ? null : clan.getHome().getPitch());
        homeObject.append("yaw", clan.getHome() == null ? null : clan.getHome().getYaw());
        document.append("home", homeObject);
        document.append("energy", clan.getEnergy());
        document.append("admin", clan.isAdmin());
        document.append("safe", clan.isSafe());
        document.append("claims", clan.getClaims());
        List<Object> members = new ArrayList<>();
        clan.getMemberMap().forEach((uuid, memberRole) -> {
            Document member = new Document();
            member.append("uuid", uuid.toString());
            member.append("role", memberRole.name());
            members.add(member);
        });
        document.append("members", members);

        List<Object> alliances = new ArrayList<>();
        clan.getAllianceMap().forEach((s, aBoolean) -> {
            Document alliance = new Document();
            alliance.append("other", s);
            alliance.append("trusted", aBoolean);
            alliances.add(alliance);
        });
        document.append("alliances", alliances);

        List<Object> enemies = new ArrayList<>();
        clan.getEnemyMap().forEach((s, integer) -> {
            Document enemy = new Document();
            enemy.append("other", s);
            enemy.append("points", integer);
        });
        document.append("enemies", enemies);

        getManager().addQuery(new InsertQuery(document, this));
    }

    public void deleteClan(Clan clan) {
        getManager(ClanManager.class).getClanSet().remove(clan);

        clan.getEnemyMap().forEach((s, integer) -> {
            Clan enemy = getManager(ClanManager.class).getClan(s);
            enemy.getEnemyMap().remove(clan.getName());
            updateEnemies(enemy);
        });

        clan.getAllianceMap().forEach((s, aBoolean) -> {
            Clan alliance = getManager(ClanManager.class).getClan(s);
            alliance.getAllianceMap().remove(clan.getName());
            updateAlliances(alliance);
        });

        getManager().addQuery(new DeleteQuery(new Document("name", clan.getName()), this));
    }

    public void updateMembers(Clan clan) {
        List<Object> members = new ArrayList<>();
        clan.getMemberMap().forEach((uuid, memberRole) -> {
            Document member = new Document();
            member.append("uuid", uuid.toString());
            member.append("role", memberRole.name());
            members.add(member);
        });
        Document doc = new Document().append("members", members);

        getManager().addQuery(new UpdateQuery(doc, this, new Document().append("name", clan.getName())));
    }

    public void updateEnergy(Clan clan) {
        Document document = new Document();
        document.append("energy", clan.getEnergy());

        getManager().addQuery(new UpdateQuery(document, this, new Document().append("name", clan.getName())));
    }

    public void updateEnemies(Clan clan) {
        List<Object> enemies = new ArrayList<>();
        clan.getEnemyMap().forEach((s, Integer) -> {
            Document enemy = new Document();
            enemy.append("other", s);
            enemy.append("points", Integer);
            enemies.add(enemy);
        });
        Document doc = new Document().append("enemies", enemies);

        getManager().addQuery(new UpdateQuery(doc, this, new Document().append("name", clan.getName())));
    }

    public void updateAlliances(Clan clan) {
        List<Object> alliances = new ArrayList<>();
        clan.getAllianceMap().forEach((s, aBoolean) -> {
            Document alliance = new Document();
            alliance.append("other", s);
            alliance.append("trusted", aBoolean);
            alliances.add(alliance);
        });
        Document doc = new Document().append("alliances", alliances);

        getManager().addQuery(new UpdateQuery(doc, this, new Document().append("name", clan.getName())));
    }

    public void updateClaims(Clan clan) {
        Document document = new Document();
        document.append("claims", clan.getClaims());
        getManager().addQuery(new UpdateQuery(document, this, new Document().append("name", clan.getName())));
    }

    public void updateHome(Clan clan) {
        Document homeObject = new Document();
        homeObject.append("world", clan.getHome() == null ? null : clan.getHome().getWorld().getName());
        homeObject.append("x", clan.getHome() == null ? null : clan.getHome().getX());
        homeObject.append("y", clan.getHome() == null ? null : clan.getHome().getY());
        homeObject.append("z", clan.getHome() == null ? null : clan.getHome().getZ());
        homeObject.append("pitch", clan.getHome() == null ? null : clan.getHome().getPitch());
        homeObject.append("yaw", clan.getHome() == null ? null : clan.getHome().getYaw());
        getManager().addQuery(new UpdateQuery(new Document().append("home", homeObject), this, new Document().append("name", clan.getName())));
    }

    @Override
    public MongoCollection<Document> getMongoCollection() {
        return getMongoDatabase().getCollection("clans");
    }
}