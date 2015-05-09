/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import com.shaneschulte.plugins.windoomrpg.skills.MagePassives;
import com.shaneschulte.plugins.windoomrpg.skills.RougePassives;
import com.shaneschulte.plugins.windoomrpg.skills.WarriorPassives;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Shane
 */
public class WindoomRPG extends JavaPlugin {
    
    //permissions prefix
    static String p = "wrpg.";
    
    @Override
    public void onEnable() {
        
        //2 seconds
        BukkitTask diamondArmorCheck = new WarriorPassives(this).runTaskTimer(this, 20, 40);
        
        //register events
        PluginManager pm = getServer().getPluginManager();
            pm.registerEvents(new WarriorPassives(this), this);
            pm.registerEvents(new MagePassives(), this);
            pm.registerEvents(new RougePassives(), this);

            
            
        //recipies
        ShapedRecipe chainHelm = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_HELMET)).shape("ooo", "oxo", "xxx").
        setIngredient('o', Material.IRON_INGOT).setIngredient('x', Material.AIR);
        getServer().addRecipe(chainHelm);

        
    }
    @Override
    public void onDisable() {
        
    }
}
