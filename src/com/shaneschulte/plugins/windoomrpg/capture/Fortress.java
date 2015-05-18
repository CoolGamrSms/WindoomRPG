/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.capture;

import com.shaneschulte.plugins.windoomrpg.BossBarApi;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Hikeru
 */
public class Fortress extends CapturableArea {

    WindoomRPG plugin = null;

    //There is a clanInControl always
    public Fortress(JavaPlugin pl, String id, String name, Location capPoint, Clan clanInControl) {
        super(pl);

        //set basic information
        this.type = "fortress";  //dynamically used for permissions, regions and config
        this.plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");
        this.capTimeInSeconds = 20;
        this.capRadius = 8;
        this.capPoint = capPoint;
        this.name = name;
        this.id = id;
        this.clanInControl = clanInControl;
        this.tag = "&b" + name;

        //if is not not its first time existing
        if (wplugin.getFortressConfig().getConfig().get(type + "." + id + ".health") == null) {
            super.onNeutral();
        }

        //avoids leaks
        finish();
    }

    /**
     * Called every update defined in AreaManager
     */
    @Override
    public void update() {
        for (Player p : getPlayersInArea()) {
            BossBarApi.sendFortressInfo(p, this, health);
            BossBarApi.removeBar(p, 20 * 2);
        }

        //for every player that is within capture radius
        for (final Player p : this.getPlayersInCaptureRadius()) {
            //if not a member of the clan in control
            if (getClanInControl() != null && !plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()).equals(getClanInControl())) {
                if (health > 0) {
                    health--;

                    //attack message, starts attack
                    if (health > 298) {
                        startAttack(plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()));
                    }

                }
                //if a member of the clan in control
            } else if (getClanInControl() != null && plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()).equals(getClanInControl())) {
                if (health < 300) {
                    health++;
                }

            }

            //transfer over clan privledges if conqured
            if (health <= 0) {
                //finds a player of the capuring clan(s) and set's it to them
                for (Player conqueror : this.getPlayersInCaptureRadius()) {
                    if (getClanInControl() != null && !plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()).equals(getClanInControl())) {
                        forceClaim(plugin.getClanManager().getClanByPlayerUniqueId(conqueror.getUniqueId()));
                        health = 300;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onNeutral() {
    }

    private void finish() {
        AreaManager.addFortress(this);
        plugin.getFortressConfig().saveConfig();
    }

}
