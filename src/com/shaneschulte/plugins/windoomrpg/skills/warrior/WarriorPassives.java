/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills.warrior;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Hikeru
 */
public class WarriorPassives implements Listener {
    //Extends BukkitRunnable to create a run method
 
    private final JavaPlugin plugin;
    //Declares your plugin variable

    public WarriorPassives(JavaPlugin plugin) {
    this.plugin = plugin;
    }
    
}
