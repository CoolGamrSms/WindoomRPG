/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills;

import com.shaneschulte.plugins.windoomrpg.GhostFactory;
import com.shaneschulte.plugins.windoomrpg.RPGperms;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Hikeru
 */


public class RoguePassives implements Listener  {
    
    final private int lReqNight = 8, lReqDay = 10; 
    
    /*@EventHandler
    public void onShift(PlayerToggleSneakEvent ev) {
        Player name = ev.getPlayer();
        
        boolean isNight = !(name.getWorld().getTime() < 12300 || name.getWorld().getTime() > 23850);
        
        
        if (!name.isSneaking()) {
            
           name.getServer().broadcastMessage("Hi " + name.getWorld().getBlockAt(name.getLocation()).getLightLevel());
            
            //just shifted
            if (!isNight && name.getWorld().getBlockAt(name.getLocation()).getState().getLightLevel() < 10) {
                WindoomRPG.plugin.ghostFactory.addPlayer(name);
            } else 
                if (isNight && name.getWorld().getBlockAt(name.getLocation()).getState().getLightLevel() < 8) {
                WindoomRPG.plugin.ghostFactory.addPlayer(name);
            }
        } else {
            
            //just unshifted
            WindoomRPG.plugin.ghostFactory.removePlayer(name);
            
        }
        
    }*/
}
