/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.capture;

import com.shaneschulte.plugins.windoomrpg.BossBarApi;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AtomEffect;
import de.slikey.effectlib.effect.BleedEffect;
import de.slikey.effectlib.effect.BleedEntityEffect;
import de.slikey.effectlib.effect.VortexEffect;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        this.type = "fortress";  //dynamically used for permissions, regions and config
        this.plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");
        this.capTimeInSeconds = 20;
        this.capRadius = 8;
        this.capPoint = capPoint;
        this.name = name;
        this.id = id;
        this.clanInControl = clanInControl;
        this.tag = "&b" + name;

        super.onNeutral();

        finish();
    }

    @Override
    public void update() {
        //Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "hi " + ChatColor.AQUA + "" + getPlayersInArea());
        for (Player p : getPlayersInArea()) {
            BossBarApi.sendFortressInfo(p, this, health);
            BossBarApi.removeBar(p, 20 * 2);
        }

        for (final Player p : this.getPlayersInCaptureRadius()) {
            //health --;
            if (getClanInControl() != null && !plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()).equals(getClanInControl())) {
                if (health > 0) {
                    health--;

                    if (health > 298) {
                        startAttack(plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()));
                    }

                }
            } else if (getClanInControl() != null && plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()).equals(getClanInControl())) {
                //Bukkit.getServer().broadcastMessage(ChatColor.AQUA + getClanInControl().getTagLabel() + " " + ChatColor.WHITE + plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()).getTagLabel());
                if (health < 300) {
                    health++;
                }
            }

            if (health <= 0) {
                //Bukkit.getServer().broadcastMessage(p.getName());
                for (Player conqueror : this.getPlayersInCaptureRadius()) {
                    if (getClanInControl() != null && !plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()).equals(getClanInControl())) {
                        setClanInControl(plugin.getClanManager().getClanByPlayerUniqueId(p.getUniqueId()));
                        health = 300;
                    }
                }
            }

            if (/*WindoomRPG.effects*/true) {
                /* VortexEffect ve = new VortexEffect(WindoomRPG.effectManager);
                 ve.period = 10;
                 ve.grow = 2;
                 ve.radius = capRadius;
                 ve.speed = 0.2f;
                 ve.visibleRange = 80;
                 //Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "vortex" + ChatColor.WHITE + "!");*/

            }

        }
    }

    private void reloadVars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNeutral() {
    }

    private void finish() {
        AreaManager.addFortress(this);
        plugin.getFortressConfig().saveConfig();
        //if (plugin.getFortressConfig().getConfig().getConfigurationSection(type + "." + id) != null) AreaManager.saveFortressesToConfig();
    }

}
