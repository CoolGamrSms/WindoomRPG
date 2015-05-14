/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author Lemons1805
 */
public class PlayerChecks {   
    
    public static String getArmorType(ItemStack item) {
        return (item == null ? "" : item.getType().toString().split("_")[0]);
    }
    
    public static boolean removeArmorType(Player player, String type) {
        PlayerInventory inventory = player.getInventory();
        boolean result = false;
        //Helmets
        if (getArmorType(inventory.getHelmet()).equalsIgnoreCase(type)) {
            ItemStack item = player.getInventory().getHelmet();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(item);
            else player.getWorld().dropItem(player.getLocation(), item);
            result = true;
        }
        
        //Chests
        if (getArmorType(inventory.getChestplate()).equalsIgnoreCase(type)) {
            ItemStack item = player.getInventory().getChestplate();
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(item);
            else player.getWorld().dropItem(player.getLocation(), item);
            result = true;
        }
        
        //Leggings
        if (getArmorType(inventory.getLeggings()).equalsIgnoreCase(type)) {
            ItemStack item = player.getInventory().getLeggings();
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(item);
            else player.getWorld().dropItem(player.getLocation(), item);
            result = true;
        }
        
        //Shoues
        if (getArmorType(inventory.getBoots()).equalsIgnoreCase(type)) {
            ItemStack item = player.getInventory().getBoots();
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(item);
            else player.getWorld().dropItem(player.getLocation(), item);
            result = true;
        }       
        return result;
    }
 
    public static boolean hasFullInventory(Player player) {
        return (player.getInventory().firstEmpty() == -1);
    }
    
    public static boolean isInAnvilInventory(Player player) {
        if (player.getInventory() != null) {
            return player.getOpenInventory().getType() == InventoryType.ANVIL;
        }
        
        return false;
    }
}
