/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.protection;

import com.shaneschulte.plugins.windoomrpg.WDmsg;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

/**
 *
 * @author Shane
 */
public class ArmorStandManager implements Listener {
    @EventHandler
    public void onArmorStandUse(PlayerArmorStandManipulateEvent e) {
        ArmorStand a = e.getRightClicked();
        if(a.getCustomName() == null) {
            a.setCustomName(e.getPlayer().getUniqueId().toString());
            a.setCustomNameVisible(true);
            a.setGravity(false);
        }
        else if(!a.getCustomName().equals(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
            String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(a.getCustomName())).getName();
            WDmsg.bad(e.getPlayer(), "That armor stand belongs to "+name+".");
        }
    }
    @EventHandler
    public void onArmorStandBreak(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof ArmorStand) {
            ArmorStand a = (ArmorStand)e.getEntity();
            if(a.getCustomName() == null) return;
            e.setCancelled(true);
            if(e.getDamager() instanceof Player) {
                if(a.getCustomName().equals(e.getDamager().getUniqueId().toString())) {
                    e.setCancelled(false);
                }
                else {
                    String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(a.getCustomName())).getName();
                    WDmsg.bad(e.getDamager(), "That armor stand belongs to "+name+".");
                }
            }
        }
    }
}
