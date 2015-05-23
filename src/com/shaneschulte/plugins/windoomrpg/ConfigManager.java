/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import com.rit.sucy.config.Config;
import java.util.HashMap;
import org.bukkit.Bukkit;

/**
 *
 * @author Hikeru
 */
public class ConfigManager {

    private static WindoomRPG plugin;
    private static Config lang, fortress;

    private static final HashMap<String, Boolean> bools = new HashMap<>();
    private static final HashMap<String, Double> dubs = new HashMap<>();

    public static void setup() {
        plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");

        //Save default language config
        lang = new Config(plugin, "lang");
        lang.saveConfig();

        fortress = new Config(plugin, "fortress");
        fortress.saveConfig();

        plugin.saveDefaultConfig();

        //            //
        bools.put("f-hideonfullhealth", plugin.getConfig().getBoolean("f-hideonfullhealth"));

        dubs.put("f-autohealamt", plugin.getConfig().getDouble("f-autohealamt"));
    }

    public static Config getLang() {
        return lang;
    }

    public static Config getFortress() {
        return fortress;
    }

    public static void setFortress(Config fortress) {
        ConfigManager.fortress = fortress;
    }
    
    //
    
    //
    
    public static boolean fHideOnFullHealth () {
        return bools.get("f-hideonfullhealth");
    }

    
    public static double fAutoHealAmt() {
        return dubs.get("f-autohealamt");
    }
}
