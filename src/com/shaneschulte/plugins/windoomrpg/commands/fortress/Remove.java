/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.commands.fortress;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.rit.sucy.config.Config;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.shaneschulte.plugins.windoomrpg.capture.AreaManager;
import com.shaneschulte.plugins.windoomrpg.capture.Fortress;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class Remove implements IFunction {

    WindoomRPG plugin = null;

    @Override
    public void execute(ConfigurableCommand command, Plugin pl, CommandSender sender, String[] args) {
        plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");

        Player p = (Player) sender;
       // p.getServer().broadcastMessage(ChatColor.AQUA + Arrays.toString(args)/*.substring(1, args.length -1)*/);

        if (args.length == 1) {
            
            //remove region
            RegionManager rm = WindoomRPG.getWorldGuard().getRegionManager(p.getWorld());
            if (rm.getRegion("fortress_" + args[0]) == null) {
                WDmsg.bad(p, "region &efortress_&6" + args[0] + WDmsg.bad + " was not found");
            } else {
                rm.removeRegion("fortress_" + args[0]);
                WDmsg.nice(p, "region &efortress_&6" + args[0] + WDmsg.nice + " was removed");
            }

            //remove from config
            Config fortressN = plugin.getFortressConfig();
            if (fortressN.getConfig().getConfigurationSection("fortress." + args[0]) != null) {
                fortressN.getConfig().set("fortress." + args[0], null);
                WDmsg.nice(p, "configuration section &afortress.&2" + args[0] + WDmsg.nice + " was removed");
            } else {
                WDmsg.bad(p, "configuration section &afortress.&2" + args[0] + WDmsg.bad + " was not found");
            }

            //remove object area
            Fortress fort = AreaManager.getFortress(args[0]);
            if (fort == null) {
                WDmsg.bad(p, "fortress &c" + args[0] + WDmsg.bad + " was not found");

            } else {
                AreaManager.removeFortress(fort.getName());
                WDmsg.nice(p, "fortress &c" + args[0] + WDmsg.nice + " was removed");
            }
            
            return;
        }

        command.displayHelp(sender, 1);
    }

}
