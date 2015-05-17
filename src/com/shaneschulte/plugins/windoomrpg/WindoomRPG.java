/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import com.rit.sucy.commands.CommandManager;
import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.SenderType;
import com.rit.sucy.config.Config;
import com.shaneschulte.plugins.commands.RecruiterCommand;
import com.shaneschulte.plugins.windoomrpg.managers.TableManager;
import com.shaneschulte.plugins.windoomrpg.skills.ArmorPassives;
import com.shaneschulte.plugins.windoomrpg.skills.BlacksmithingPassives;
import com.shaneschulte.plugins.windoomrpg.skills.MagePassives;
import com.shaneschulte.plugins.windoomrpg.skills.RoguePassives;
import com.shaneschulte.plugins.windoomrpg.skills.WarriorPassives;
import com.shaneschulte.plugins.windoomrpg.traits.RecruiterTrait;
import java.util.logging.Level;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Shane
 */
public class WindoomRPG extends JavaPlugin {
    
    //permissions prefix
    static String p = "wrpg.";
    private SimpleClans sc;
    Config lang;
    private TableManager tableManager;
    
    @Override
    public void onEnable() {
        //Print enable message
        PluginDescriptionFile pdfFile = this.getDescription();
        this.getLogger().log(Level.INFO, "{0} version {1} by {2} is enabled!", 
                new Object[]{pdfFile.getName(), pdfFile.getVersion(), pdfFile.getAuthors().get(0)});
        
        //register events
        PluginManager pm = getServer().getPluginManager();
            pm.registerEvents(new WarriorPassives(this), this);
            pm.registerEvents(new ArmorPassives(this), this);
            pm.registerEvents(new MagePassives(), this);
            pm.registerEvents(new RoguePassives(), this);
            pm.registerEvents(new BlacksmithingPassives(), this);
            
        //check if Citizens is present and enabled.
        if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);	
            return;
        }

        //Register your trait with Citizens.
        try {
            net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(RecruiterTrait.class).withName("recruiter"));	
        }
        catch(Exception e) {
            
        }
        //Attempt to hook into SimpleClans
        if (!hookSimpleClans()) {
            this.getLogger().log(Level.SEVERE, "Failed to hook into SimpleClans! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        //Save default language config
        lang = new Config(this, "lang");
        lang.saveDefaultConfig();
        
        //Register the table manager
        tableManager = new TableManager(this);
        TableManager.loadTables();
        
        //Register commands
        ConfigurableCommand myRoot = new ConfigurableCommand(this, "recruiter", SenderType.PLAYER_ONLY);
        ConfigurableCommand myCommand = new ConfigurableCommand(
            this, 
            "set", 
            SenderType.PLAYER_ONLY, 
            new RecruiterCommand(),
            "Sets the recruiters clan", // A description for the command usage
            "<clan/class>", // Arguments for the command usage
            "citizens.admin" // The required permission for the command
        );
        myRoot.addSubCommand(myCommand);
        CommandManager.registerCommand(myRoot);

        
    }
    
    public String getString(String name) {
        return (String)lang.getConfig().get(name);
    }
    
    private boolean hookSimpleClans() {
        Plugin plug = getServer().getPluginManager().getPlugin("SimpleClans");
        if (plug != null) {
            sc = ((SimpleClans) plug);
            return true;
        }
        return false;
    }
    public ClanManager getClanManager() {
        return this.sc.getClanManager();
    }
    @Override
    public void onDisable() {
        
        TableManager.saveTables();
        
        //Print disable message
        PluginDescriptionFile pdfFile = this.getDescription();
        this.getLogger().log(Level.INFO, "{0} version {1} by {2} is disabled.", 
                new Object[]{pdfFile.getName(), pdfFile.getVersion(), pdfFile.getAuthors().get(0)});

    }
}
