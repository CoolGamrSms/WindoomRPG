/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.traits;

import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 *
 * @author Shane
 */
//This is your trait that will be applied to a npc using the /trait mytraitname command. Each NPC gets its own instance of this class.
//the Trait class has a reference to the attached NPC class through the protected field 'npc' or getNPC().
//The Trait class also implements Listener so you can add EventHandlers directly to your trait.
public class RecruiterTrait extends Trait {

    public RecruiterTrait() {
        super("recruiter");
        plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");
    }

    WindoomRPG plugin = null;

    @Persist String tag = null;


    // An example event handler. All traits will be registered automatically as Bukkit Listeners.
    @EventHandler
    public void click(net.citizensnpcs.api.event.NPCRightClickEvent event) {
        if(tag == null) return;
        if(event.getNPC() == this.getNPC()) {
            Clan clan = plugin.getClanManager().getClan(tag);
            if(clan == null) return;
            Player p = event.getClicker();
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(p);
            if(cp == null || cp.getClan() == null) {
                //Attempt to recruit
                if(p.isSneaking()) {
                    WDmsg.nice((CommandSender)p, String.format(plugin.getString("recruited"), clan.getName()));
                    if(cp != null) plugin.getClanManager().deleteClanPlayer(cp);
                    plugin.getClanManager().getCreateClanPlayer(p.getUniqueId()).setClan(clan);
                }
                else {
                    WDmsg.info((CommandSender)p, String.format(plugin.getString("recruit"), clan.getName()));
                }
            }
            else {
                if(cp.getClan().equals(plugin.getClanManager().getClan(tag))) {
                    WDmsg.nice((CommandSender)p, plugin.getString("recruited-positive"));
                }
                else {
                    WDmsg.bad((CommandSender)p, plugin.getString("recruited-negative"));
                }
            }
        }
    }

    // Called every tick
    @Override
    public void run() {
    }

    //Run code when your trait is attached to a NPC. 
    //This is called BEFORE onSpawn, so npc.getBukkitEntity() will return null
    //This would be a good place to load configurable defaults for new NPCs.
    @Override
    public void onAttach() {
            plugin.getServer().getLogger().info(npc.getName() + " has been assigned Recruiter!");
    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getBukkitEntity() is still valid.
    @Override
    public void onDespawn() {
    }

    //Run code when the NPC is spawned. Note that npc.getBukkitEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
    }

    public void setClan(Clan clan) {
        this.tag = clan.getTag();
    }
 
}