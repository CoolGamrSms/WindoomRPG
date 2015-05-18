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
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class Point implements IFunction {

    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (AreaManager.getFortressByName(args[0]) == null) {
                WDmsg.bad(sender, "&b" + args[0] + WDmsg.bad + " is not a real fortress.");
                return;
            }

            Player p = (Player) sender;
            Location loc = p.getLocation();

            AreaManager.getFortressByName(args[0]).setCapPoint(p.getLocation());
            WDmsg.nice(p, "Set &ecapture point " + WDmsg.info + "to (&b"
                    + loc.getBlockX() + WDmsg.info
                    + ",&b " + loc.getBlockY() + WDmsg.info
                    + ",&b " + loc.getBlockZ() + WDmsg.info
                    + ")");

            WindoomRPG.fortress.saveConfig();
            AreaManager.loadFortressesFromConfig();

        } else {
            command.displayHelp(sender, 1);
        }
    }

}
