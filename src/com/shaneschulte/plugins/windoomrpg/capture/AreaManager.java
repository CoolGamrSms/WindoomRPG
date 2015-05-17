/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.capture;

import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Hikeru
 */
public class AreaManager extends BukkitRunnable {

    private static WindoomRPG plugin = null;

    static ArrayList<Fortress> fortresses = new ArrayList<>();

    public AreaManager() {
        this.plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");

        this.runTaskTimer(plugin, 20, 20);
    }

    public static void loadFortressesFromConfig() {
        fortresses = new ArrayList<>();
        FileConfiguration conf = plugin.getFortressConfig().getConfig();
        
        plugin.getFortressConfig().saveConfig();

        if (conf.getConfigurationSection("fortress") != null) {
            for (String key : conf.getConfigurationSection("fortress").getKeys(false)) {
                ConfigurationSection cc = conf.getConfigurationSection("fortress." + key);
                if (cc != null) {
                    Fortress fort = new Fortress(plugin, key, cc.getString("name"), new Location(
                            Bukkit.getServer().getWorld(cc.getString("capPoint.world")), cc.getInt("capPoint.x"), cc.getInt("capPoint.y"), cc.getInt("capPoint.z")), null);
                    fort.setQ1(new BlockVector(cc.getInt("q1.x"), cc.getInt("q1.y"), cc.getInt("q1.z")));
                    fort.setQ2(new BlockVector(cc.getInt("q2.x"), cc.getInt("q2.y"), cc.getInt("q2.z")));
                    fort.setTag(cc.getString("tag"));
                    fort.setCapRadius(cc.getInt("capRadius"));
                    fort.setCapTimeInSeconds(cc.getInt("capTimeInSeconds"));
                    fort.id = key;

                    if (cc.getString("clan") != null) {
                        fort.setClanInControl(plugin.getClanManager().getClan(cc.getString("clan")));
                    }

                    ProtectedCuboidRegion region = new ProtectedCuboidRegion("fortress" + "_" + key,
                            fort.getQ1(),
                            fort.getQ2());

                    //DefaultDomain owners = new DefaultDomain();

                    if (fort.getClanInControl() != null) {
                        for (ClanPlayer p : plugin.getClanManager().getAllClanPlayers()) {
                            //owners.addPlayer(p.toPlayer());
                            region.getOwners().addPlayer(p.getName());
                        }
                    }
                    try {
                        WindoomRPG.getWorldGuard().getRegionManager(fort.capPoint.getWorld()).save();
                        //region.setOwners(owners);
                    } catch (ProtectionDatabaseException ex) {
                        Logger.getLogger(AreaManager.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    WindoomRPG.getWorldGuard().getRegionManager(fort.capPoint.getWorld()).addRegion(region);
                    AreaManager.addFortress(fort);

                    Bukkit.getLogger().log(Level.INFO, "{0}{1}loaded", new Object[]{ChatColor.AQUA, key});
                    /* List list = conf.getList("fortress." + key + ".capPoint");
                     Bukkit.getServer().broadcastMessage(list.size() + "");*/
                    //Fortress fort = new Fortress(plugin, conf.get("fortress."))
                }
            }
        }
    }

    @Override
    public void run() {
        for (Fortress f : fortresses) {
            if (f != null) {
                f.update();
            }
        }
    }

    public static ArrayList<Fortress> getFortresses() {
        return fortresses;
    }

    public static void removeFortress(String id) {
        for (int i = 0; i < fortresses.size(); i++) {
            if (fortresses.get(i).getId().equals(id)) {
                fortresses.remove(i);
            }
        }
    }

    public static void addFortress(Fortress fort) {
        for (int i = 0; i < fortresses.size(); i++) {
            if (fortresses.get(i).getId().equals(fort.getId())) {
                fortresses.remove(i);
            }
        }
        fortresses.add(fort);
    }

    public static Fortress getFortress(String id) {
        for (Fortress f : fortresses) {
            if (f.getId().equals(id)) {
                return f;
            }
        }

        return null;
    }

    public static Fortress getFortressByName(String name) {
        for (Fortress f : fortresses) {
            if (f.getName().equals(name)) {
                return f;
            }
        }

        return null;
    }

    public static void saveFortressesToConfig() {
        for (Fortress fort : fortresses) {
            plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".name", fort.getName());
            plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".tag", fort.tag);
            plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".capTimeInSeconds", fort.capTimeInSeconds);
            plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".capRadius", fort.capRadius);

            if (fort.capPoint != null) {
                if (fort.capPoint.getWorld() == null) {
                    plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".capPoint.world", "world");
                } else {
                    plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".capPoint.world", fort.capPoint.getWorld().getName());
                }
                plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".capPoint.x", fort.capPoint.getBlockX());
                plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".capPoint.y", fort.capPoint.getBlockY());
                plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".capPoint.z", fort.capPoint.getBlockZ());
            }

            if (fort.clanInControl != null) {
                plugin.getFortressConfig().getConfig().set(fort.type + "." + fort.id + ".clan", fort.clanInControl.getName());
            }
        }

        for (Fortress f : AreaManager.getFortresses()) {
            if (plugin.getFortressConfig().getConfig().getConfigurationSection("fortress." + f.getId()) == null)
                fortresses.remove(f);
        }

    }
}
