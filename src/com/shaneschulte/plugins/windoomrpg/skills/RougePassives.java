/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills;

import com.shaneschulte.plugins.windoomrpg.RPGperms;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Hikeru
 */


public class RougePassives implements Listener  {

    
    @EventHandler
    public void onAboutToCraftSomething(PrepareItemCraftEvent ev) {
        Player name = (Player) ev.getView().getPlayer();
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_HELMET && name.hasPermission(RPGperms.CRAFT_CHAINMAIL.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_HELMET));  
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_CHESTPLATE && name.hasPermission(RPGperms.CRAFT_CHAINMAIL.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_CHESTPLATE));  
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_LEGGINGS && name.hasPermission(RPGperms.CRAFT_CHAINMAIL.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_LEGGINGS));  
        
        if (ev.getRecipe().getResult().getType() == Material.IRON_BOOTS && name.hasPermission(RPGperms.CRAFT_CHAINMAIL.getPermission()))
            ev.getInventory().setResult(new ItemStack(Material.CHAINMAIL_BOOTS));   
    }
}
