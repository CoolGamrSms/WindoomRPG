package com.shaneschulte.plugins.windoomrpg;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.shaneschulte.plugins.windoomrpg.capture.Fortress;
import org.bukkit.ChatColor;

public class BossBarApi {

    private static int enderdragonId;
    private static Plugin plugin = Bukkit.getPluginManager().getPlugins()[0];
    private static HashMap<String, BukkitRunnable> toHide = new HashMap<String, BukkitRunnable>();

    static {
        try {
            Field field = Class.forName(
                    "net.minecraft.server." + Bukkit.getServer().getClass().getName().split("\\.")[3] + ".Entity")
                    .getDeclaredField("entityCount");
            field.setAccessible(true);
            enderdragonId = field.getInt(null);
            field.set(null, enderdragonId + 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void removeBar(Player player) {
        removeBar(player, 2);
    }

    public static void removeBar(final Player player, int afterTicks) {
        if (player.hasMetadata("SeesEnderdragon") && !toHide.containsKey(player.getName())) {
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    player.removeMetadata("SeesEnderdragon", plugin);
                    sendRemovePacket(player);
                    toHide.remove(player.getName());
                }
            };
            runnable.runTaskLater(plugin, afterTicks);
            toHide.put(player.getName(), runnable);
        }
    }

    private static void sendRemovePacket(Player player) {
        try {
            PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            spawnPacket.getIntegerArrays().write(0, new int[]{enderdragonId});
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sendSpawnPacket(Player player, Location toSpawn, String message, float health) throws Exception {
        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        StructureModifier<Object> spawnPacketModifier = spawnPacket.getModifier();
        spawnPacketModifier.write(0, enderdragonId);
        spawnPacketModifier.write(1, (byte) 64); // EntityID of wither
        spawnPacketModifier.write(2, toSpawn.getBlockX() * 32);
        spawnPacketModifier.write(3, toSpawn.getBlockY() * 32);
        spawnPacketModifier.write(4, toSpawn.getBlockZ() * 32);
        // Make the datawatcher that turns it invisible
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) 32);
        watcher.setObject(2, message);
        watcher.setObject(6, health, true); // Set health
        watcher.setObject(10, message);
        watcher.setObject(20, 881);
        spawnPacket.getDataWatcherModifier().write(0, watcher);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket, false);
    }

    public static void setName(Player player, String message, float health) {
        try {
            if (!player.hasMetadata("SeesEnderdragon")) {
                player.setMetadata("SeesEnderdragon", new FixedMetadataValue(plugin, true));
            }
            if (toHide.containsKey(player.getName())) {
                toHide.remove(player.getName()).cancel();
            }
            Location toSpawn = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(23));

            sendSpawnPacket(player, toSpawn, message, health);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendFortressInfo(Player player, Fortress fort, /*String message, */ float health) {
        try {
            if (!player.hasMetadata("SeesEnderdragon")) {
                player.setMetadata("SeesEnderdragon", new FixedMetadataValue(plugin, true));
            }
            if (toHide.containsKey(player.getName())) {
                toHide.remove(player.getName()).cancel();
            }
            //Location toSpawn = fort.getCapPoint();
            Location toSpawn = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(23));
            //Location toSpawn = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), -20, player.getLocation().getBlockZ());

            String message;
            if (fort.getClanInControl() != null) {
                message = ChatColor.translateAlternateColorCodes('&', "&4&l<&3" + fort.getTag() + "&4&l>&7&o captured by &6&l<&7" + fort.getClanInControl().getTagLabel() + "&6&l> &d&l" + (double) Math.round((health / 3) * 10) / 10 + "&7%");
            } else {
                message = ChatColor.translateAlternateColorCodes('&', "&4&l<&3" + fort.getTag() + "&4&l>&7&o " + fort.getCurrentModeTitle() + " &d&l" + (double) Math.round((health / 3) * 10) / 10 + "&7%");
            }
            sendSpawnPacket(player, toSpawn, message, health);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
