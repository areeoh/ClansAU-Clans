package com.areeoh.clans;

import com.areeoh.database.DatabaseManager;
import com.areeoh.database.DatabaseTypes;
import com.areeoh.framework.Module;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class ClanRepository extends Module<DatabaseManager> {

    public ClanRepository(DatabaseManager manager) {
        super(manager, "ClanRepository");
    }

    @Override
    public void initialize(JavaPlugin javaPlugin) {
        new BukkitRunnable() {
            public void run() {
                getManager(DatabaseManager.class).getModule(ClanRepository.class).loadClans();
            }
        }.runTaskAsynchronously(javaPlugin);
    }

    public void saveClan(Clan clan) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", clan.getName());
        jsonObject.put("age", clan.getAge());

        JSONObject homeObject = new JSONObject();
        homeObject.put("world", clan.getHome() == null ? null : clan.getHome().getWorld().getName());
        homeObject.put("x", clan.getHome() == null ? null : clan.getHome().getX());
        homeObject.put("y", clan.getHome() == null ? null : clan.getHome().getY());
        homeObject.put("z", clan.getHome() == null ? null : clan.getHome().getZ());
        homeObject.put("pitch", clan.getHome() == null ? null : clan.getHome().getPitch());
        homeObject.put("yaw", clan.getHome() == null ? null : clan.getHome().getYaw());

        jsonObject.put("home", homeObject);
        jsonObject.put("energy", clan.getEnergy());
        jsonObject.put("admin", clan.isAdmin());
        jsonObject.put("safe", clan.isSafe());

        JSONArray claims = new JSONArray();
        claims.addAll(clan.getClaims());
        jsonObject.put("territory", claims);

        JSONArray members = new JSONArray();
        clan.getMemberMap().forEach((uuid, memberRole) -> {
            final JSONObject member = new JSONObject();
            member.put("uuid", uuid.toString());
            member.put("role", memberRole.name());
            members.add(member);
        });
        jsonObject.put("members", members);

        JSONArray alliances = new JSONArray();
        clan.getAllianceMap().forEach((other, trusted) -> {
            final JSONObject alliance = new JSONObject();
            alliance.put("other", other);
            alliance.put("trusted", trusted);
            alliances.add(alliance);
        });
        jsonObject.put("alliance", alliances);

        JSONArray enemies = new JSONArray();
        clan.getEnemyMap().forEach((s, integer) -> {
            final JSONObject enemy = new JSONObject();
            enemy.put("other", s);
            enemy.put("trusted", integer);
            enemies.add(enemy);
        });
        jsonObject.put("enemies", enemies);

        try (FileWriter fileWriter = new FileWriter(getPlugin().getDataFolder() + "/Clans/" + clan.getName() + ".json")) {
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
    }

    public synchronized void loadClans() {
        final File parent = new File(getPlugin().getDataFolder() + "/Clans/");

        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (parent != null) {
            for (File file : parent.listFiles()) {
                JSONParser parser = new JSONParser();
                try {
                    JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));

                    final String clanName = jsonObject.get("name").toString();
                    final long age = (long) jsonObject.get("age");

                    final int energy = (int) (long) jsonObject.get("energy");
                    final boolean admin = (boolean) jsonObject.get("admin");
                    final boolean safe = (boolean) jsonObject.get("safe");
                    Location home = null;
                    try {
                        final JSONObject homeObject = (JSONObject) jsonObject.get("home");
                        home = new Location(Bukkit.getWorld(homeObject.get("world").toString()),(double)homeObject.get("x"), (double)homeObject.get("y"), (double)homeObject.get("z"), (float)(double)homeObject.get("yaw"), (float)(double)homeObject.get("pitch"));
                    } catch (NullPointerException ignored) {
                    }
                    final JSONArray claims = (JSONArray) jsonObject.get("territory");
                    final JSONArray members = (JSONArray) jsonObject.get("members");
                    final JSONArray alliance = (JSONArray) jsonObject.get("alliance");
                    final JSONArray enemies = (JSONArray) jsonObject.get("enemies");

                    final Clan clan = new Clan(clanName);
                    clan.setHome(home);
                    clan.setAge(age);
                    clan.setEnergy(energy);
                    clan.setAdmin(admin);
                    clan.setSafe(safe);
                    claims.forEach(o -> clan.getClaims().add((String) o));
                    members.forEach(o -> {
                        JSONObject member = (JSONObject) o;
                        clan.getMemberMap().put(UUID.fromString(member.get("uuid").toString()), Clan.MemberRole.valueOf(member.get("role").toString()));
                    });
                    alliance.forEach(o -> {
                        JSONObject ally = (JSONObject) o;
                        clan.getAllianceMap().put((String) ally.get("other"), (Boolean) ally.get("trusted"));
                    });
                    enemies.forEach(o -> {
                        JSONObject enemy = (JSONObject) o;
                        clan.getEnemyMap().put((String)enemy.get("other"), (int) (long)enemy.get("points"));
                    });

                    getManager(ClanManager.class).addClan(clan);
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteClan(Clan clan) {
        clan.getAllianceMap().keySet().forEach(s -> getManager(ClanManager.class).getClan(s).getAllianceMap().remove(clan.getName()));
        clan.getEnemyMap().keySet().forEach(s -> getManager(ClanManager.class).getClan(s).getEnemyMap().remove(clan.getName()));

        clan.getAllianceMap().keySet().forEach(s -> getManager(DatabaseManager.class).getModule(ClanRepository.class).updateAlliances(getManager(ClanManager.class).getClan(s)));
        clan.getEnemyMap().keySet().forEach(s -> getManager(DatabaseManager.class).getModule(ClanRepository.class).updateEnemies(getManager(ClanManager.class).getClan(s)));

        final File f = new File(getPlugin().getDataFolder() + DatabaseTypes.CLANS.getPath() + clan.getName() + ".json");

        if(f.exists()) {
            f.delete();
        }
        getManager(ClanManager.class).getClanSet().remove(clan);
    }

    public void updateMembers(Clan clan) {
        JSONArray members = new JSONArray();
        clan.getMemberMap().forEach((uuid, memberRole) -> {
            final JSONObject member = new JSONObject();
            member.put("uuid", uuid.toString());
            member.put("role", memberRole.name());
            members.add(member);
        });

        DatabaseTypes.CLANS.SetObject(getManager(), clan.getName() + ".json", "members", members);
    }

    public void updateEnergy(Clan clan) {
        DatabaseTypes.CLANS.SetObject(getManager(), clan.getName() + ".json", "energy", clan.getEnergy());
    }

    public void updateEnemies(Clan clan) {
        JSONArray enemies = new JSONArray();
        clan.getEnemyMap().forEach((other, points) -> {
            final JSONObject member = new JSONObject();
            member.put("other", other);
            member.put("points", points);
            enemies.add(member);
        });

        DatabaseTypes.CLANS.SetObject(getManager(), clan.getName() + ".json", "enemies", enemies);
    }

    public void updateAlliances(Clan clan) {
        JSONArray alliance = new JSONArray();
        clan.getAllianceMap().forEach((other, trusted) -> {
            final JSONObject member = new JSONObject();
            member.put("other", other);
            member.put("trusted", trusted);
            alliance.add(member);
        });

        DatabaseTypes.CLANS.SetObject(getManager(), clan.getName() + ".json", "alliance", alliance);
    }

    public void updateClaims(Clan clan) {
        JSONArray claims = new JSONArray();
        claims.addAll(clan.getClaims());

        DatabaseTypes.CLANS.SetObject(getManager(), clan.getName() + ".json", "territory", claims);
    }

    public void updateHome(Clan clan) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("world", clan.getHome() == null ? null : clan.getHome().getWorld().getName());
        jsonObject.put("x", clan.getHome() == null ? null : clan.getHome().getX());
        jsonObject.put("y", clan.getHome() == null ? null : clan.getHome().getY());
        jsonObject.put("z", clan.getHome() == null ? null : clan.getHome().getZ());
        jsonObject.put("pitch", clan.getHome() == null ? null : clan.getHome().getPitch());
        jsonObject.put("yaw", clan.getHome() == null ? null : clan.getHome().getYaw());

        DatabaseTypes.CLANS.SetObject(getManager(), clan.getName() + ".json", "home", jsonObject);
    }
}