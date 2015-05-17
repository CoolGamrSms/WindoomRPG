/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills;

import com.shaneschulte.plugins.windoomrpg.PlayerChecks;
import com.shaneschulte.plugins.windoomrpg.RPGperms;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Shane
 */
public class ArmorPassives extends BukkitRunnable implements Listener {
    
    private final JavaPlugin plugin;
    
    public ArmorPassives(JavaPlugin plugin) {
        this.plugin = plugin;
        this.runTaskTimer(plugin, 20, 40);
    }
    
    @EventHandler
    public void onAboutToCraftSomething(PrepareItemCraftEvent ev) {
        Player name = (Player) ev.getView().getPlayer();
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_HELMET && !name.hasPermission(RPGperms.USE_IRON_ARMOR.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_HELMET));  
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_CHESTPLATE && !name.hasPermission(RPGperms.USE_IRON_ARMOR.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_CHESTPLATE));  
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_LEGGINGS && !name.hasPermission(RPGperms.USE_IRON_ARMOR.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_LEGGINGS));  
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_BOOTS && !name.hasPermission(RPGperms.USE_IRON_ARMOR.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_BOOTS));   
    }
    //run from main class like every 2 seconds
    @Override
    public void run() {
       /* for (Player p : plugin.getServer().getOnlinePlayers()) {
            boolean msg = false;
            if (!p.hasPermission(RPGperms.USE_IRON_ARMOR.getPermission()) && PlayerChecks.removeArmorType(p, "iron")) {
                WDmsg.bad(p, RPGperms.USE_IRON_ARMOR.getWarning());
                msg = true;
            }
            if (!p.hasPermission(RPGperms.USE_DIAMOND_ARMOR.getPermission()) && PlayerChecks.removeArmorType(p, "diamond")) {
                if(!msg) WDmsg.bad(p, RPGperms.USE_DIAMOND_ARMOR.getWarning());
            }
        }*/

    }
}
