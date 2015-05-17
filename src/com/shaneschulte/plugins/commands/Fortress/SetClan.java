/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.commands.Fortress;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.shaneschulte.plugins.windoomrpg.capture.AreaManager;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class SetClan implements IFunction {

    WindoomRPG plugin = null;

    @Override
    public void execute(ConfigurableCommand command, Plugin pl, CommandSender sender, String[] args) {
        if (args.length == 2) {
            if (AreaManager.getFortressByName(args[0]) == null) {
                WDmsg.bad(sender, "&e" + args[0] + WDmsg.bad + " is not a real fortress.");
                return;
            }
            this.plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");

            if (plugin.getClanManager().getClan(args[1]) == null) {
                WDmsg.bad(sender, "&7" + args[1] + WDmsg.bad + " is not a real clan. Use the &5clan tag" + WDmsg.bad + ".");
                return;
            }

            Clan clan = plugin.getClanManager().getClan(args[1]);

            WDmsg.nice(sender, args[0] + WDmsg.nice + "'s clan in control is now &e" + clan.getTag());
            AreaManager.getFortressByName(args[0]).setClanInControl(clan);
            WindoomRPG.fortress.saveConfig();
            AreaManager.loadFortressesFromConfig();

        } else {
            command.displayHelp(sender, 1);
        }
    }

}
