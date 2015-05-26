/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.commands.fortress;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.capture.AreaManager;
import com.shaneschulte.plugins.windoomrpg.capture.Fortress;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class Teleport implements IFunction {

    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (AreaManager.getFortressByName(args[0]) == null) {
                WDmsg.bad(sender, "&b" + args[0] + WDmsg.bad + " is not a real fortress.");
                return;
            }

            Player p = (Player) sender;
            Fortress fort = AreaManager.getFortressByName(args[0]);

            //get smooth cap point :D
            Location to = fort.getCapPoint();
            to.setPitch(p.getLocation().getPitch());
            to.setYaw(p.getLocation().getYaw());
            
            p.teleport(to.add(0, 2, 0), PlayerTeleportEvent.TeleportCause.PLUGIN);

            WDmsg.nice(p, "Teleported to &b" + fort.getTag() + WDmsg.nice + "'s &ecapture point&b  ");
            

        } else {
            command.displayHelp(sender, 1);
        }
    }

}
