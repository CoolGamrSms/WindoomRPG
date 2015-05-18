/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.commands.Fortress;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.capture.AreaManager;
import com.shaneschulte.plugins.windoomrpg.capture.Fortress;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Hikeru
 */
public class List implements IFunction {

    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {

        for (Fortress fort : AreaManager.getFortresses()) {
            String clanTag = "none";
            if (fort.getClanInControl() != null) {
                clanTag = fort.getClanInControl().getTagLabel();
            }
            
            //message
            WDmsg.info((Player) sender, "&oid: &e&o" + fort.getId() + WDmsg.info + ", name: &a" + fort.getName() + WDmsg.info + ", tag: "
                    + fort.getTag() + WDmsg.info + ", clan: &d" + clanTag);

        }
    }

}
