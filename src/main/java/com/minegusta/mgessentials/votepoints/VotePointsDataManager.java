package com.minegusta.mgessentials.votepoints;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class VotePointsDataManager {
    static File file;
    static FileConfiguration conf;

    public static void createOrLoadPointsFile(Plugin p) {
        try {

            file = new File(p.getDataFolder(), "votepoints.yml");

            if (!file.exists()) {
                p.saveResource("votepoints.yml", false);
                Bukkit.getLogger().info("Successfully created " + file.getName() + ".");
            }
            conf = YamlConfiguration.loadConfiguration(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            conf.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getPlayerVotes(UUID p) {
        return conf.getInt(p.toString() + ".unclaimed", 0);
    }

    public static int getTotalVotes(String uuid) {
        return conf.getInt(uuid + ".total", 0);
    }

    public static List<String> getMostVotes() {
        List<String> users = Lists.newArrayList();
        int highest = 0;

        for (String s : conf.getKeys(false)) {
            int gotten = getTotalVotes(s);
            if (gotten > highest) highest = gotten;
        }

        for (String s : conf.getKeys(false)) {
            if (getTotalVotes(s) >= highest) {
                users.add(s);
            }
        }

        return users;
    }

    public static List<String> getMoreThan(int amount) {
        List<String> users = Lists.newArrayList();

        for (String s : conf.getKeys(false)) {
            if (getTotalVotes(s) >= amount) {
                users.add(s);
            }
        }
        return users;
    }


    public static void addVote(UUID p) {
        conf.set(p.toString() + ".unclaimed", getPlayerVotes(p) + 1);
        conf.set(p.toString() + ".total", getTotalVotes(p.toString()) + 1);
    }

    public static void resetvotes(UUID p) {
        if (conf.isSet(p.toString())) {
            conf.set(p.toString() + ".unclaimed", 0);
            conf.set(p.toString() + ".total", 0);
        }
    }

    public static void removeUnclaimedVote(UUID p) {
        conf.set(p.toString() + ".unclaimed", getPlayerVotes(p) - 1);
    }
}
