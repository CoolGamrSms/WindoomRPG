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
public class Radius implements IFunction {

    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {
        if (args.length == 2) {
            if (AreaManager.getFortressByName(args[0]) == null) {
                WDmsg.bad(sender, "&b" + args[0] + WDmsg.bad + " is not a real fortress.");
                return;
            }

            try {
                int num = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                WDmsg.bad(sender, "&b" + args[1] + WDmsg.bad + " is not a real number.");
                return;
            }

            //set radius
            AreaManager.getFortressByName(args[0]).setCapRadius(Integer.parseInt(args[1]));
            WDmsg.nice(sender, args[0] + WDmsg.nice + "'s cap radius is now &e" + args[1]);

            
            //save config
            WindoomRPG.fortress.saveConfig();
            AreaManager.loadFortressesFromConfig();

        } else {
            command.displayHelp(sender, 1);
        }
    }

}
