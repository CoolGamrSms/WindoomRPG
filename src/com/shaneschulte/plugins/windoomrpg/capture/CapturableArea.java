/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.capture;

import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import static com.shaneschulte.plugins.windoomrpg.WindoomRPG.getWorldGuard;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Hikeru
 */
public class CapturableArea {

    //Attack states
    private static final int UNDERATTACK = -1, NEUTRAL = 0, UNDERCONTROL = 1;
    protected int currentMode = NEUTRAL;
    protected int health = 300; //ender dragon health on spigot servers

    WindoomRPG wplugin = null;

    protected String type = "CapturableArea";
    protected ArrayList<Player> playersInArea;
    protected ArrayList<Player> playersInCaptureArea;

    //default vars
    protected String name = "Undiscovered Fortress", tag = "&7Undiscovered Fortress", id = "Unknown";
    protected Location capPoint = null;
    protected BlockVector q1, q2;
    protected int capRadius = 8, capTimeInSeconds = 20;
    protected int c = 8, e = 20;
    protected Clan clanInControl = null;

    public CapturableArea(JavaPlugin plugin) {
        this.wplugin = (WindoomRPG) plugin;
        playersInArea = new ArrayList<>();
        playersInCaptureArea = new ArrayList<>();
    }

    public ArrayList<Player> getPlayersInArea() {
        return playersInArea;
    }

    public void update() {
        playersInArea.clear();
        playersInCaptureArea.clear();
        String regionNameCc = type + "_" + id;

        for (Player p : Bukkit.getServer().getWorld("world").getPlayers()) {
            if (isWithinRegion(p, regionNameCc)) {
                playersInArea.add(p);
                if (capPoint.distance(p.getLocation()) <= getCapRadius()) {
                    playersInCaptureArea.add(p);
                }
            }
        }
    }

    public ArrayList<Player> getPlayersInCaptureRadius() {
        return playersInCaptureArea;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public String getCurrentModeTitle() {
        if (currentMode == UNDERATTACK) {
            return "Under Attack!";
        }
        if (currentMode == UNDERCONTROL) {
            return "Claimed";
        } else {
            return "Neutral";
        }
    }

    public void startAttack(Clan clan) {
        onAttack(clan);
        this.currentMode = UNDERATTACK;
    }

    public void forceClaim(Clan clan) {
        setClanInControl(clan);
        onClaim(clan);
        this.currentMode = UNDERCONTROL;
    }

    public void forceNeutral() {
        onNeutral();
        this.currentMode = NEUTRAL;
    }

    public void onClaim(Clan clan) {
        if (this.getClanInControl() != null) {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&l<" + this.getClanInControl().getTag() + "&6&l> &b"
                    + getTag() + " &7has been captured by &e" + clan.getColorTag() + "&7!"));
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b" + getTag() + " &7has been captured by &e" + clan.getColorTag() + "&7!"));
        }
    }

    public void onAttack(Clan clan) {
        if (this.getClanInControl() != null) {
        } else {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b " + getTag()
                    + " &7has been captured by &e&6&l<" + clan.getColorTag() + "&6&l>&7!"));
        }
    }

    public void onNeutral() {
        Bukkit.getServer().broadcastMessage(/*RPGperms.RECIEVE_CAPTURE_MESSAGES.getPermission(),*/ChatColor.translateAlternateColorCodes('&', getTag()
                        + "&7 is now &6neutral&7."));
    }

    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public void setTag(String tag) {
        this.tag = tag;
        wplugin.getFortressConfig().getConfig().set(type + "." + id + ".tag", tag);

    }

    public Clan getClanInControl() {
        return clanInControl;
    }

    public String getName() {
        return name;
    }

    public Location getCapPoint() {
        return capPoint;
    }

    public int getCapRadius() {
        return capRadius;
    }

    public int getCapTimeInSeconds() {
        return capTimeInSeconds;
    }

    public void setHealth(int health) {
        this.health = health;
        wplugin.getFortressConfig().getConfig().set(type + "." + id + ".health", health);

    }

    public void setName(String name) {
        this.name = name;
        wplugin.getFortressConfig().getConfig().set(type + "." + id + ".name", name);

    }

    public void setCapPoint(Location capPoint) {
        this.capPoint = capPoint;

        String cc = type + "." + id;
        if (capPoint.getWorld() == null) {
            wplugin.getFortressConfig().getConfig().set(cc + ".capPoint.world", "world");
        } else {
            wplugin.getFortressConfig().getConfig().set(cc + ".capPoint.world", capPoint.getWorld().getName());
        }
        wplugin.getFortressConfig().getConfig().set(cc + ".capPoint.x", capPoint.getBlockX());
        wplugin.getFortressConfig().getConfig().set(cc + ".capPoint.y", capPoint.getBlockY());
        wplugin.getFortressConfig().getConfig().set(cc + ".capPoint.z", capPoint.getBlockZ());
    }

    public void setCapRadius(int capRadius) {
        this.capRadius = capRadius;
        wplugin.getFortressConfig().getConfig().set(type + "." + id + ".capRadius", capRadius);
    }

    public void setCapTimeInSeconds(int capTimeInSeconds) {
        this.capTimeInSeconds = capTimeInSeconds;
        wplugin.getFortressConfig().getConfig().set(type + "." + id + ".capTimeInSeconds", capTimeInSeconds);

    }

    public void setClanInControl(Clan clanInControl) {
        if (clanInControl != null) {
            this.clanInControl = clanInControl;
        }
        this.clanInControl = clanInControl;
        if (clanInControl != null) {
            wplugin.getFortressConfig().getConfig().set(type + "." + id + ".clan", clanInControl.getTag());
        }

        //add owners to WorldGuard region
        if (WindoomRPG.getWorldGuard().getRegionManager(capPoint.getWorld()).getRegion("fortress" + "_" + getId()) != null) {
            if (getClanInControl() != null) {
                ProtectedRegion region = WindoomRPG.getWorldGuard().getRegionManager(capPoint.getWorld()).getRegion("fortress" + "_" + getId());

                //Set flag group to clan name
                DefaultDomain dm = new DefaultDomain();
                dm.addGroup(getClanInControl().getTag());
                region.setMembers(dm);

                //Set chest acceess to deny for anyone not in the clan
                region.setFlag(DefaultFlag.CHEST_ACCESS, StateFlag.State.DENY);
                RegionGroupFlag groupFlag = DefaultFlag.CHEST_ACCESS.getRegionGroupFlag();
                RegionGroup groupValue = null;
                try {
                    groupValue = groupFlag.parseInput(WindoomRPG.getWorldGuard(), null, "non_members");
                } catch (InvalidFlagFormat ex) {
                    Logger.getLogger(AreaManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Set enter and exit messages for clan regions
                StringFlag eflag = com.sk89q.worldguard.protection.flags.DefaultFlag.GREET_MESSAGE;
                StringFlag lflag = com.sk89q.worldguard.protection.flags.DefaultFlag.FAREWELL_MESSAGE;
                try {
                    region.setFlag(eflag, eflag.parseInput(WindoomRPG.getWorldGuard(), null, ChatColor.translateAlternateColorCodes('&',
                            "&7Now entering &6&l<&b" + getClanInControl().getColorTag() + "&6&l>&7's &e" + getTag())));
                    region.setFlag(lflag, lflag.parseInput(WindoomRPG.getWorldGuard(), null, ChatColor.translateAlternateColorCodes('&',
                            "&7Now leaving &6&l<&b" + getClanInControl().getColorTag() + "&6&l>&7's &e" + getTag())));
                } catch (InvalidFlagFormat ex) {
                    Logger.getLogger(CapturableArea.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Another catch
                if (groupValue != null) {
                    region.setFlag(groupFlag, groupValue);
                    //Bukkit.getServer().getLogger().info("'\non-members\' deny messed up :/");
                }
                
                //  :)  ~hikeru  --
            }
        }
    }

    public BlockVector getQ1() {
        return q1;
    }

    public void setQ1(BlockVector q1) {
        this.q1 = q1;
        if (q1 != null) {
            wplugin.getFortressConfig().getConfig().set(type + "." + id + ".q1.x", q1.getBlockX());
            wplugin.getFortressConfig().getConfig().set(type + "." + id + ".q1.y", q1.getBlockY());
            wplugin.getFortressConfig().getConfig().set(type + "." + id + ".q1.z", q1.getBlockZ());
        }
    }

    public void saveTempData() {
        wplugin.getFortressConfig().getConfig().set(type + "." + id + ".health", health);
    }

    public BlockVector getQ2() {
        return q2;
    }

    public void setQ2(BlockVector q2) {
        this.q2 = q2;
        if (q2 != null) {
            wplugin.getFortressConfig().getConfig().set(type + "." + id + ".q2.x", q2.getBlockX());
            wplugin.getFortressConfig().getConfig().set(type + "." + id + ".q2.y", q2.getBlockY());
            wplugin.getFortressConfig().getConfig().set(type + "." + id + ".q2.z", q2.getBlockZ());
        }
    }

    public boolean isWithinRegion(Player player, String region) {
        return isWithinRegion(player.getLocation(), region);
    }

    public boolean isWithinRegion(Block block, String region) {
        return isWithinRegion(block.getLocation(), region);
    }

    public boolean isWithinRegion(Location loc, String region) {
        WorldGuardPlugin guard = getWorldGuard();
        Vector v = BukkitUtil.toVector(loc.toVector());
        RegionManager manager = guard.getRegionManager(loc.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(v);
        for (ProtectedRegion each : set) {
            if (each.getId().equalsIgnoreCase(region)) {
                return true;
            }
        }
        return false;
    }

    /* */
}
