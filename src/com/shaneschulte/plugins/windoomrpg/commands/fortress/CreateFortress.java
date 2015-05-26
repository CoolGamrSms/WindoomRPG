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
import com.shaneschulte.plugins.windoomrpg.capture.Fortress;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class CreateFortress implements IFunction {

    WindoomRPG plugin = null;

    @Override
    public void execute(ConfigurableCommand command, Plugin pl, CommandSender sender, String[] args) {
        plugin = (WindoomRPG) Bukkit.getServer().getPluginManager().getPlugin("WindoomRPG");

        Player p = (Player) sender;

        String name;
        if (args.length == 1) {
            Selection sel = WindoomRPG.getWorldEdit().getSelection(p);
            if (sel == null) {
                WDmsg.bad(p, "No selection! Uses plugin: &e" + "worldedit");
                return;
            }

            name = args[0];

            //set initial 
            Fortress fort = new Fortress(plugin, name, name, p.getLocation(), null);
            fort.setQ1(new BlockVector(sel.getNativeMinimumPoint()));
            fort.setQ2(new BlockVector(sel.getNativeMaximumPoint()));
            fort.setCapTimeInSeconds(20);
            fort.setCapRadius(8);
            fort.setCapPoint(p.getLocation());
            fort.setName(name);
            fort.setClanInControl(null);
            fort.setTag("&b" + name);

            //Type is always fortress for fortress command
            //Create worldguard region with prefix fortress_
            ProtectedCuboidRegion region = new ProtectedCuboidRegion("fortress" + "_" + fort.getId(),
                    new BlockVector(sel.getNativeMinimumPoint()),
                    new BlockVector(sel.getNativeMaximumPoint()));

            WindoomRPG.getWorldGuard().getRegionManager(p.getWorld()).addRegion(region);

            //add to fortress config
            WDmsg.nice(p, "Created area &e" + name + WDmsg.nice + ".");

            WDmsg.nice(p, "Set &ecapture point " + WDmsg.nice + "to (&e"
                    + fort.getCapPoint().getBlockX() + WDmsg.nice
                    + ",&e " + fort.getCapPoint().getBlockY() + WDmsg.nice
                    + ",&e " + fort.getCapPoint().getBlockZ() + WDmsg.nice
                    + ") with radius &e" + fort.getCapRadius() + WDmsg.nice + ".");
            return;
        }
        command.displayHelp(sender, 1);
    }

}
