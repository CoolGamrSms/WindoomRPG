/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.protection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 *
 * @author Shane
 */
public class ProtectionListener implements Listener {
    
    private final int threshold = -1536;
    private final String clan1 = "West";
    private final String clan2 = "East";
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBucket(PlayerBucketEmptyEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onBucket(PlayerBucketFillEvent e) {
        e.setCancelled(true);
    }
   
}
