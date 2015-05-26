/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.commands.fortress;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.ConfigManager;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.capture.AreaManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class ModTag implements IFunction {

    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {
        if (args.length >= 2) {
            if (AreaManager.getFortressByName(args[0]) == null) {
                WDmsg.bad(sender, "&e" + args[0] + WDmsg.bad + " is not a real fortress.");
                return;
            }

            //build tag efficently
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                if (args.length - 1 != i) {
                    sb.append(" ");
                }

            }

            AreaManager.getFortressByName(args[0]).setTag(sb.toString());

            ConfigManager.getFortress().saveConfig();
            WDmsg.nice(sender, args[0] + WDmsg.nice + "'s tag was changed to &e" + sb.toString());
            AreaManager.loadFortressesFromConfig();

        } else {
            command.displayHelp(sender, 1);
        }
    }

}
