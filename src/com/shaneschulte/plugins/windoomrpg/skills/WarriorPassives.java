/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills;

import com.shaneschulte.plugins.windoomrpg.Permissions;
import com.shaneschulte.plugins.windoomrpg.PlayerChecks;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Hikeru
 */
public class WarriorPassives extends BukkitRunnable implements Listener {
    //Extends BukkitRunnable to create a run method
 
    private final JavaPlugin plugin;
    //Declares your plugin variable

    public WarriorPassives(JavaPlugin plugin) {
    this.plugin = plugin;
    }
    //This is called from your main class and sets your plugin variable
    
    @EventHandler
    public void onEquipArmor(InventoryClickEvent ev) {
        
    }

    //run from main class like every 2 seconds
    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (!p.hasPermission(Permissions.EQUIP_DIAMOND_ARMOR.getPermission()) && PlayerChecks.hasDArmor(p)) {
                WDmsg.getInstance().bad(p, Permissions.EQUIP_DIAMOND_ARMOR.getWarning());
                PlayerChecks.removeDArmor(p);
            }
        }

    }
    
}
