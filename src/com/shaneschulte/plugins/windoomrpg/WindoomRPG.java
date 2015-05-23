/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import com.rit.sucy.commands.CommandManager;
import com.rit.sucy.commands.ConfigurableCommand;
import com.rit.sucy.commands.SenderType;
import com.rit.sucy.config.Config;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.CreateFortress;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.List;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.ModTag;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.Name;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.Point;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.Radius;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.Remove;
import com.shaneschulte.plugins.windoomrpg.commands.fortress.SetClan;
import com.shaneschulte.plugins.windoomrpg.commands.RecruiterCommand;
import com.shaneschulte.plugins.windoomrpg.capture.AreaManager;
import com.shaneschulte.plugins.windoomrpg.capture.CapturableArea;
import com.shaneschulte.plugins.windoomrpg.capture.Fortress;
import com.shaneschulte.plugins.windoomrpg.commands.SoundCommand;
import com.shaneschulte.plugins.windoomrpg.enchanting.TableManager;
import com.shaneschulte.plugins.windoomrpg.protection.ArmorStandManager;
import com.shaneschulte.plugins.windoomrpg.protection.ProtectionListener;
import com.shaneschulte.plugins.windoomrpg.skills.ArmorPassives;
import com.shaneschulte.plugins.windoomrpg.skills.BlacksmithingPassives;
import com.shaneschulte.plugins.windoomrpg.skills.MagePassives;
import com.shaneschulte.plugins.windoomrpg.skills.RoguePassives;
import com.shaneschulte.plugins.windoomrpg.skills.warrior.SpinAttack;
import com.shaneschulte.plugins.windoomrpg.skills.warrior.WarriorPassives;
import com.shaneschulte.plugins.windoomrpg.traits.RecruiterTrait;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.SkillPlugin;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Shane
 */
public class WindoomRPG extends JavaPlugin implements SkillPlugin {

    //permissions prefix
    public static String p = "wrpg.";
    private SimpleClans sc;
    public static Config lang, fortress;
    private AreaManager am;

    public EffectManager effectManager;
    public boolean effects = false;

    private WindoomRPG plugin;

    public ArrayList<Fortress> fortresses = new ArrayList<>();

    @Override
    public void onEnable() {
        this.plugin = this;
        //Print enable message
        PluginDescriptionFile pdfFile = this.getDescription();

        //register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new WarriorPassives(this), this);
        pm.registerEvents(new ArmorPassives(this), this);
        pm.registerEvents(new MagePassives(), this);
        pm.registerEvents(new RoguePassives(), this);
        pm.registerEvents(new BlacksmithingPassives(), this);
        pm.registerEvents(new ArmorStandManager(), this);
        //pm.registerEvents(new ProtectionListener(), this);

        //pm.registerEvents(new CapturableArea(this), this);

        hookPlugins();

        //Save default language config
        lang = new Config(this, "lang");
        lang.saveConfig();

        fortress = new Config(this, "fortress");

        fortress.saveConfig();

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
        
        ConfigurableCommand soundCommand = new ConfigurableCommand(
                this,
                "sound",
                SenderType.PLAYER_ONLY,
                new SoundCommand(),
                "Plays a sound with a given pitch", // A description for the command usage
                "<sound> <pitch>", // Arguments for the command usage
                "citizens.admin" // The required permission for the command
        );
        CommandManager.registerCommand(soundCommand);

        //Register commands
        ConfigurableCommand fortressCommand = new ConfigurableCommand(this, "fortress", SenderType.PLAYER_ONLY);
        fortressCommand.setAliases(Arrays.asList("fort", "fr"));
        fortressCommand.addSubCommands(new ConfigurableCommand(
                this,
                "create",
                SenderType.ANYONE,
                new CreateFortress(),
                "Creates capturable area in worldedit selection",
                "<id>",
                RPGperms.FORTRESS_CREATE.getPermission()
        ),
                new ConfigurableCommand(
                        this,
                        "rename",
                        SenderType.ANYONE,
                        new Name(),
                        "rename a fortress",
                        "<name> <new name>",
                        RPGperms.FORTRESS_RENAME.p()
                ),
                new ConfigurableCommand(
                        this,
                        "setpoint",
                        SenderType.ANYONE,
                        new Point(),
                        "sets a fort's cap point at your location",
                        "<name>",
                        RPGperms.FORTRESS_RENAME.p()
                ),
                new ConfigurableCommand(
                        this,
                        "setradius",
                        SenderType.ANYONE,
                        new Radius(),
                        "sets a fort's cap point radius",
                        "<name> <radius>",
                        RPGperms.FORTRESS_RENAME.p()
                ),
                new ConfigurableCommand(
                        this,
                        "modtag",
                        SenderType.ANYONE,
                        new ModTag(),
                        "retag a fortress",
                        "<name> <new tag>",
                        RPGperms.FORTRESS_MODTAG.p()
                ),
                new ConfigurableCommand(
                        this,
                        "modclan",
                        SenderType.ANYONE,
                        new SetClan(),
                        "set the clan of a fortress",
                        "<name> <clan>",
                        RPGperms.FORTRESS_MODCLAN.p()
                ),
                new ConfigurableCommand(
                        this,
                        "remove",
                        SenderType.ANYONE,
                        new Remove(),
                        "remove a fortress",
                        "<fortress>",
                        RPGperms.FORTRESS_REMOVE.p()
                ),
                new ConfigurableCommand(
                        this,
                        "list",
                        SenderType.ANYONE,
                        new List(),
                        "list all fortresses",
                        "",
                        RPGperms.FORTRESS_LIST.p()
                )
        );
        CommandManager.registerCommand(fortressCommand);

        am = new AreaManager();
        AreaManager.loadFortressesFromConfig();
        
        TableManager tableManager = new TableManager(this);
        TableManager.loadTables();

        this.getLogger().log(Level.INFO, "{0} version {1} by {2} is enabled!",
                new Object[]{pdfFile.getName(), pdfFile.getVersion(), pdfFile.getAuthors().get(0)});
    }

    public String getLangString(String name) {
        return (String) lang.getConfig().get(name);
    }

    public Config getFortressConfig() {
        return fortress;
    }

    public AreaManager getAreaManager() {
        return am;
    }

    public void setFortressConfig(Config config) {
        fortress = config;
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
        PluginDescriptionFile pdfFile = this.getDescription();
        this.getLogger().log(Level.INFO, "{0} version {1} by {2} is disabled.",
                new Object[]{pdfFile.getName(), pdfFile.getVersion(), pdfFile.getAuthors().get(0)});

        for (Fortress fort : AreaManager.getFortresses()) {
            fort.saveTempData();
        }
        
        fortress.saveConfig();
        
        TableManager.saveTables();
        //effectManager.dispose();
        HandlerList.unregisterAll((Listener) this);
    }

    private void hookPlugins() {
        //check if Citizens is present and enabled.
        if (getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Register your trait with Citizens.
        try {
            net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(RecruiterTrait.class).withName("recruiter"));
        } catch (Exception e) {

        }
        
        //Attempt to hook into SimpleClans
        if (!hookSimpleClans()) {
            this.getLogger().log(Level.SEVERE, "Failed to hook into SimpleClans! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (EffectLib.instance() == null) {
            effects = false;
        } else {
            effects = true;
            EffectLib lib = EffectLib.instance();
            effectManager = new EffectManager(lib);
        }

    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public static WorldEditPlugin getWorldEdit() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldEditPlugin) plugin;
    }

    @Override
    public void registerSkills(SkillAPI sapi) {
        sapi.addSkill(new SpinAttack(this));
    }

    @Override
    public void registerClasses(SkillAPI sapi) {
        sapi.addClass(new TestClass());
    }
}
