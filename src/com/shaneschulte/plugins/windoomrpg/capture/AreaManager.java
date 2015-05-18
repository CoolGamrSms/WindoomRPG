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

    //refresh time for all areas
    int refreshTime = 20;

    public AreaManager() {
        this.plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");
        this.runTaskTimer(plugin, 20, refreshTime);
    }

    //reinitiates all areas
    public static void loadFortressesFromConfig() {
        fortresses = new ArrayList<>();
        FileConfiguration conf = plugin.getFortressConfig().getConfig();

        plugin.getFortressConfig().saveConfig();

        if (conf.getConfigurationSection("fortress") != null) {
            for (String key : conf.getConfigurationSection("fortress").getKeys(false)) {
                ConfigurationSection cc = conf.getConfigurationSection("fortress." + key);

                //get config for fortresses
                if (cc != null) {
                    //create new fort and set data
                    Fortress fort = new Fortress(plugin, key, cc.getString("name"), new Location(
                            Bukkit.getServer().getWorld(cc.getString("capPoint.world")), cc.getInt("capPoint.x"), cc.getInt("capPoint.y"), cc.getInt("capPoint.z")), null);
                    fort.setQ1(new BlockVector(cc.getInt("q1.x"), cc.getInt("q1.y"), cc.getInt("q1.z")));
                    fort.setQ2(new BlockVector(cc.getInt("q2.x"), cc.getInt("q2.y"), cc.getInt("q2.z")));
                    fort.setTag(cc.getString("tag"));
                    fort.setCapRadius(cc.getInt("capRadius"));
                    fort.setCapTimeInSeconds(cc.getInt("capTimeInSeconds"));
                    fort.id = key;
                    fort.health = cc.getInt("health");

                    //get clan
                    if (cc.getString("clan") != null) {
                        fort.setClanInControl(plugin.getClanManager().getClan(cc.getString("clan")));
                    }

                    //get region
                    ProtectedCuboidRegion region = new ProtectedCuboidRegion("fortress" + "_" + key,
                            fort.getQ1(),
                            fort.getQ2());

                    //add owners to region
                    if (fort.getClanInControl() != null) {
                        for (ClanPlayer p : plugin.getClanManager().getAllClanPlayers()) {
                            region.getOwners().addPlayer(p.getName());
                        }
                    }
                    try {
                        WindoomRPG.getWorldGuard().getRegionManager(fort.capPoint.getWorld()).save();
                    } catch (ProtectionDatabaseException ex) {
                        Logger.getLogger(AreaManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    WindoomRPG.getWorldGuard().getRegionManager(fort.capPoint.getWorld()).addRegion(region);
                    
                    //add fortress to array
                    AreaManager.addFortress(fort);

                    Bukkit.getLogger().log(Level.INFO, "{0}{1}loaded", new Object[]{ChatColor.AQUA, key});
                }
            }
        }
    }

    @Override
    public void run() {
        //update all fortresses
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
            String cc = fort.type + "." + fort.id;
            
            //save vars
            plugin.getFortressConfig().getConfig().set(cc + ".name", fort.getName());
            plugin.getFortressConfig().getConfig().set(cc + ".tag", fort.tag);
            plugin.getFortressConfig().getConfig().set(cc + ".capTimeInSeconds", fort.capTimeInSeconds);
            plugin.getFortressConfig().getConfig().set(cc + ".capRadius", fort.capRadius);

            if (fort.capPoint != null) {
                //defualt world is "world"
                if (fort.capPoint.getWorld() == null) {
                    plugin.getFortressConfig().getConfig().set(cc + ".capPoint.world", "world");
                } else {
                    plugin.getFortressConfig().getConfig().set(cc + ".capPoint.world", fort.capPoint.getWorld().getName());
                }
                plugin.getFortressConfig().getConfig().set(cc + ".capPoint.x", fort.capPoint.getBlockX());
                plugin.getFortressConfig().getConfig().set(cc + ".capPoint.y", fort.capPoint.getBlockY());
                plugin.getFortressConfig().getConfig().set(cc + ".capPoint.z", fort.capPoint.getBlockZ());
            }

            //save clan
            if (fort.clanInControl != null) {
                plugin.getFortressConfig().getConfig().set(cc + ".clan", fort.clanInControl.getName());
            }
        }

        //remove old fortresses
        for (Fortress f : AreaManager.getFortresses()) {
            if (plugin.getFortressConfig().getConfig().getConfigurationSection("fortress." + f.getId()) == null) {
                fortresses.remove(f);
            }
        }

    }
}
