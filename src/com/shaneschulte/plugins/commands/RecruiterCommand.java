/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.commands;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.shaneschulte.plugins.windoomrpg.traits.RecruiterTrait;
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
            WDmsg.bad(sender, "That command must be performed on a Sentry!");
            return;
        }
        if(args.length == 1) {
            String tag = args[0];
            WindoomRPG wrpg = (WindoomRPG)plugin;
            Clan clan = wrpg.getClanManager().getClan(tag);
            if(clan == null) {
                WDmsg.bad(sender, "That clan doesn't exist.");
                return;
            }
            RecruiterTrait rt = ThisNPC.getTrait(RecruiterTrait.class);
            rt.setClan(clan);
            WDmsg.nice(sender, "Clan set successfully!");
            return;
        }

        // This is how you can display the command usage to something
        // The '1' is just the page to display
        command.displayHelp(sender, 1);
    }
}