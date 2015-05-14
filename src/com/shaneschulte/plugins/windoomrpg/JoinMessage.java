/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 *
 * @author Hikeru
 */


public class JoinMessage implements Listener  {
    
    @EventHandler
    public void onJoin(PlayerInteractEvent ev) {
                Bukkit.getServer().broadcastMessage("Hi :)");
        if (ev.getAction() != Action.LEFT_CLICK_AIR) return;
       

        Player name = ev.getPlayer();
        
        //bar counting down
        //BarAPI.setMessage(name, ChatColor.translateAlternateColorCodes('&', "&7Welcome to &eWindoomRPG&7! :D"), 12);

    }
    
}
