/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills;

import com.shaneschulte.plugins.windoomrpg.RPGperms;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagePassives implements Listener {
    
    @EventHandler
    public void onPlayerInteractBlock (PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
            if (!event.getPlayer().hasPermission(RPGperms.ENCHANT_ITEMS.getPermission())) {
                WDmsg.bad(event.getPlayer(), RPGperms.ENCHANT_ITEMS.getWarning());
                event.setCancelled(true);
            }
        }
    }
    
}
