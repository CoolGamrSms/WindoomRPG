/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills;

import com.shaneschulte.plugins.windoomrpg.EnchantingTable;
import com.shaneschulte.plugins.windoomrpg.RPGperms;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.managers.TableManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MagePassives implements Listener {
    
    @EventHandler
    public void onBreakTable (BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.ENCHANTMENT_TABLE) {
            Location l = event.getBlock().getLocation();
            if(TableManager.isTable(l)) {
                TableManager.deregisterTable(l);
            }
        }
    }
    @EventHandler
    public void onPlayerEnchant (PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
            if (!event.getPlayer().hasPermission(RPGperms.ENCHANT_ITEMS.getPermission())) {
                WDmsg.bad(event.getPlayer(), RPGperms.ENCHANT_ITEMS.getWarning());
                event.setCancelled(true);
            }
            else {
                event.setCancelled(true);
                Location l = event.getClickedBlock().getLocation();
                ItemStack i = event.getPlayer().getItemInHand();
                if(!TableManager.isTable(l))
                    TableManager.registerTable(l);
                EnchantingTable et = TableManager.getTable(l);
                if(i != null && i.getType() == Material.BOOK && !i.hasItemMeta()) {
                    if(et.getPower() > 0) {
                        //Enchant the book
                        if(i.getAmount() == 1) event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                        else i.setAmount(i.getAmount()-1);
                        Location newl = l.clone();
                        newl.setY(l.getY()+1);    
                        event.getPlayer().getWorld().dropItem(newl, et.enchant());
                        WDmsg.nice(event.getPlayer(), "You condensed the enchanting table's charge into an enchanted book!");
                        return;
                    }
                }
                if(i == null || !et.isGem(i)) {
                    WDmsg.info(event.getPlayer(), "This enchanting table is charged to level "+ChatColor.AQUA+Integer.toString(et.getPower()));
                    return;
                }
                int power = et.getGemPower(i);
                if(et.addItem(i)) {
                    WDmsg.nice(event.getPlayer(), "The table's charge was increased by "+ChatColor.GREEN+Integer.toString(power));
                    if(i.getAmount() == 1) event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    else i.setAmount(i.getAmount()-1);
                }
                else {
                    WDmsg.bad(event.getPlayer(), "Adding this gem would exceed the charge limit of 30.");
                }
            }
        }
    }
    
}
