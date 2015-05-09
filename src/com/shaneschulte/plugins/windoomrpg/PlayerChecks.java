/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author Lemons1805
 */
public class PlayerChecks {   
    
    public static boolean hasArmorType(ItemStack item, Material type) {
        return (item == null ? false : item.getType() == type);
    }

    public static boolean hasDArmor(Player player) {
        PlayerInventory inventory = player.getInventory();

        if(hasArmorType(inventory.getHelmet(), Material.DIAMOND_HELMET)
        || hasArmorType(inventory.getChestplate(), Material.DIAMOND_CHESTPLATE)
        || hasArmorType(inventory.getLeggings(), Material.DIAMOND_LEGGINGS)
        || hasArmorType(inventory.getBoots(), Material.DIAMOND_BOOTS))
        {
            return true;
        }    
        
        return false;
    
    }
    
    public static void removeDArmor(Player player) {
        PlayerInventory inventory = player.getInventory();
    
        //Helmets
        if (hasArmorType(inventory.getHelmet(), Material.DIAMOND_HELMET)) {
            ItemStack helm = player.getInventory().getHelmet();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(helm);
            else player.getWorld().dropItem(player.getLocation(), helm);
        }
        
        //Chests
        if (hasArmorType(inventory.getChestplate(), Material.DIAMOND_CHESTPLATE)) {
            ItemStack helm = player.getInventory().getChestplate();
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(helm);
            else player.getWorld().dropItem(player.getLocation(), helm);
        }
        
        //Leggings
        if (hasArmorType(inventory.getLeggings(), Material.DIAMOND_LEGGINGS)) {
            ItemStack helm = player.getInventory().getLeggings();
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(helm);
            else player.getWorld().dropItem(player.getLocation(), helm);
        }
        
        //Shoues
       if (hasArmorType(inventory.getBoots(), Material.DIAMOND_BOOTS)) {
            ItemStack helm = player.getInventory().getBoots();
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            if (!hasFullInventory(player))
                player.getInventory().addItem(helm);
            else player.getWorld().dropItem(player.getLocation(), helm);
        }       
    }
 
    public static boolean hasFullInventory(Player player) {
        boolean isFull = false;
        if (player.getInventory().firstEmpty() == -1) isFull = true;
        
        return isFull;
    }



}
