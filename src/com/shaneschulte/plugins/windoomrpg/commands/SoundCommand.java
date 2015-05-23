/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.commands;

import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.IFunction;
import com.shaneschulte.plugins.windoomrpg.WDmsg;
import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.shaneschulte.plugins.windoomrpg.traits.RecruiterTrait;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.classes.RPGClass;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Shane
 */
public class SoundCommand implements IFunction {

    // Executes your function when it's called
    @Override
    public void execute(ConfigurableCommand command, Plugin plugin, CommandSender sender, String[] args) {
        Player player = (Player)sender;
        player.playSound(player.getLocation(), Sound.valueOf(args[0]), 1, Float.parseFloat(args[1]));
        player.sendMessage(Sound.valueOf(args[0]).toString());
    }
}