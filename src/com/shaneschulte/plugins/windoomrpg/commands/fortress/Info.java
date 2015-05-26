/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.commands.fortress;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.shaneschulte.plugins.windoomrpg.capture.AreaManager;
import com.shaneschulte.plugins.windoomrpg.capture.Fortress;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class Info implements IFunction {

    WindoomRPG plugin = null;

    @Override
    public void execute(ConfigurableCommand command, Plugin pl, CommandSender sender, String[] args) {
        plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");
        Player p = (Player) sender;

        if (args.length == 0) {
            
            //if in a fort
            if (AreaManager.getFortressByPlayer(p) == null) {
                WDmsg.bad(sender, "&b" + p.getDisplayName() + WDmsg.bad + " is not in a fort.");
                return;
            }
            Fortress fort = AreaManager.getFortressByPlayer(p);

            String clanTag = "none";
            if (fort.getClanInControl() != null) {
                clanTag = fort.getClanInControl().getColorTag();
            }

            //message
            WDmsg.info((Player) sender, "&oid: &e&o" + fort.getId() + WDmsg.info + ", name: &a" + fort.getName() + WDmsg.info + ", tag: "
                    + fort.getTag() + WDmsg.info + ", clan: &d" + clanTag);

            return;

        }

        if (args.length == 1) {

            //if it wanted a fort name
            if (AreaManager.getFortressByName(args[0]) == null) {
                WDmsg.bad(sender, "&b" + args[0] + WDmsg.bad + " is not a real fortress.");
                return;
            }

            Fortress fort = AreaManager.getFortressByName(args[0]);

            String clanTag = "none";
            if (fort.getClanInControl() != null) {
                clanTag = fort.getClanInControl().getColorTag();
            }

            //message
            WDmsg.info((Player) sender, "&oid: &e&o" + fort.getId() + WDmsg.info + ", name: &a" + fort.getName() + WDmsg.info + ", tag: "
                    + fort.getTag() + WDmsg.info + ", clan: &d" + clanTag);

            return;
        }

        command.displayHelp(sender, 1);
    }

}
