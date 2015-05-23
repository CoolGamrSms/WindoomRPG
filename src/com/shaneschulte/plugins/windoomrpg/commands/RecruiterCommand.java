/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.commands;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.shaneschulte.plugins.windoomrpg.traits.RecruiterTrait;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.classes.RPGClass;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Shane
 */
public class RecruiterCommand implements IFunction {

    // Executes your function when it's called
    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {

        NPC ThisNPC = ((Citizens) plugin.getServer().getPluginManager().getPlugin("Citizens")).getNPCSelector().getSelected(sender);
        if(ThisNPC == null) {
            WDmsg.bad(sender, "You must have an NPC selected to use this command.");
            return;
        }
        if (!ThisNPC.hasTrait(RecruiterTrait.class)) {
            WDmsg.bad(sender, "That command must be performed on a Recruiter!");
            return;
        }
        if(args.length == 2) {
            String tag = args[0];
            String rival = args[1];
            WindoomRPG wrpg = (WindoomRPG)plugin;
            Clan clan1 = wrpg.getClanManager().getClan(tag);
            Clan clan2 = wrpg.getClanManager().getClan(rival);
            if(clan1 == null || clan2 == null) {
                WDmsg.bad(sender, "That clan doesn't exist.");
                return;
            }
            RecruiterTrait rt = ThisNPC.getTrait(RecruiterTrait.class);
            rt.setClan(clan1, clan2);
            WDmsg.nice(sender, "Clan set successfully!");
            return;
        }
        else if(args.length == 1) {
            String tag = args[0];
            //Test if it's a skill instead
            RPGClass c = SkillAPI.getClass(tag);
            if(c != null) {
                RecruiterTrait rt = ThisNPC.getTrait(RecruiterTrait.class);
                rt.setClass(c);
                WDmsg.nice(sender, "Class set successfully!");
                return;
            }
            WDmsg.bad(sender, "That class doesn't exist.");
            return;
        }

        // This is how you can display the command usage to something
        // The '1' is just the page to display
        command.displayHelp(sender, 1);
    }
}