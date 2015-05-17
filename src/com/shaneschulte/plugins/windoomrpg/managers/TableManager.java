/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.managers;

import com.rit.sucy.config.Config;
import com.shaneschulte.plugins.windoomrpg.EnchantingTable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Shane
 */
public class TableManager {
    
    private static HashMap<Location, EnchantingTable> tables;
    private static JavaPlugin plugin;
    private static Config tableConfig;
    
    public static List<EnchantWeight> eWeights;
    public static HashMap<Enchantment, int[]> eLevels;
    
    public final class EnchantWeight {
        private Enchantment e;
        private int w;
        public EnchantWeight(Enchantment e, int w) {
            this.e = e;
            this.w = w;
        }
        public Enchantment getEnchant() {
            return e;
        }
        public int getWeight() {
            return w;
        }
    }
    
    public TableManager(JavaPlugin pl) { 
        eLevels = new HashMap<>();
        eLevels.put(Enchantment.PROTECTION_ENVIRONMENTAL, new int[]{10, 10, 4});
        eLevels.put(Enchantment.PROTECTION_FIRE, new int[]{1, 8, 4});
        eLevels.put(Enchantment.PROTECTION_FALL, new int[]{5, 6, 4});
        eLevels.put(Enchantment.PROTECTION_EXPLOSIONS, new int[]{1, 8, 4});
        eLevels.put(Enchantment.OXYGEN, new int[]{10, 10, 3});
        eLevels.put(Enchantment.PROTECTION_PROJECTILE, new int[]{1, 6, 4});
        eLevels.put(Enchantment.WATER_WORKER, new int[]{1, 40, 1});
        eLevels.put(Enchantment.THORNS, new int[]{10, 20, 3});
        eLevels.put(Enchantment.DEPTH_STRIDER, new int[]{10, 10, 3});
        eLevels.put(Enchantment.DAMAGE_ALL, new int[]{10, 10, 5});
        eLevels.put(Enchantment.DAMAGE_UNDEAD, new int[]{1, 8, 5});
        eLevels.put(Enchantment.DAMAGE_ARTHROPODS, new int[]{1, 8, 5});
        eLevels.put(Enchantment.KNOCKBACK, new int[]{5, 20, 2});
        eLevels.put(Enchantment.FIRE_ASPECT, new int[]{10, 20, 2});
        eLevels.put(Enchantment.LOOT_BONUS_MOBS, new int[]{15, 9, 3});
        eLevels.put(Enchantment.ARROW_DAMAGE, new int[]{10, 10, 5});
        eLevels.put(Enchantment.ARROW_KNOCKBACK, new int[]{8, 20, 2});
        eLevels.put(Enchantment.ARROW_FIRE, new int[]{20, 30, 1});
        eLevels.put(Enchantment.ARROW_INFINITE, new int[]{20, 30, 1});
        eLevels.put(Enchantment.LOOT_BONUS_BLOCKS, new int[]{15, 9, 3});
        eLevels.put(Enchantment.DIG_SPEED, new int[]{1, 10, 5});
        eLevels.put(Enchantment.DURABILITY, new int[]{1, 8, 3});
        eLevels.put(Enchantment.SILK_TOUCH, new int[]{15, 50, 1});
        eLevels.put(Enchantment.LUCK, new int[]{15, 9, 3});
        eLevels.put(Enchantment.LURE, new int[]{15, 9, 3});
        eWeights = Arrays.asList(
            new EnchantWeight(Enchantment.PROTECTION_ENVIRONMENTAL, 10),
            new EnchantWeight(Enchantment.PROTECTION_FALL, 5),
            new EnchantWeight(Enchantment.PROTECTION_FIRE, 5),
            new EnchantWeight(Enchantment.PROTECTION_PROJECTILE, 5),
            new EnchantWeight(Enchantment.WATER_WORKER, 2),
            new EnchantWeight(Enchantment.PROTECTION_EXPLOSIONS, 2),
            new EnchantWeight(Enchantment.OXYGEN, 2),
            new EnchantWeight(Enchantment.DEPTH_STRIDER, 2),
            new EnchantWeight(Enchantment.THORNS, 1),
            new EnchantWeight(Enchantment.DAMAGE_ALL, 10),
            new EnchantWeight(Enchantment.DAMAGE_ARTHROPODS, 5),
            new EnchantWeight(Enchantment.KNOCKBACK, 5),
            new EnchantWeight(Enchantment.DAMAGE_UNDEAD, 5),
            new EnchantWeight(Enchantment.FIRE_ASPECT, 2),
            new EnchantWeight(Enchantment.LOOT_BONUS_MOBS, 2),
            new EnchantWeight(Enchantment.DIG_SPEED, 7),
            new EnchantWeight(Enchantment.DURABILITY, 8),
            new EnchantWeight(Enchantment.LOOT_BONUS_BLOCKS, 2),
            new EnchantWeight(Enchantment.SILK_TOUCH, 1),
            new EnchantWeight(Enchantment.ARROW_DAMAGE, 10),
            new EnchantWeight(Enchantment.ARROW_FIRE, 2),
            new EnchantWeight(Enchantment.ARROW_KNOCKBACK, 2),
            new EnchantWeight(Enchantment.ARROW_INFINITE, 1),
            new EnchantWeight(Enchantment.LUCK, 5),
            new EnchantWeight(Enchantment.LURE, 5)
        );
        tables = new HashMap<>();
        plugin = pl;
        tableConfig = new Config(plugin, "tables");
    }
    
    public static boolean isTable(Location l) {
        return tables.containsKey(l);
    }
    
    public static EnchantingTable getTable(Location l) {
        return tables.get(l);
    }
    
    public static void registerTable(Location l) {
        tables.put(l, new EnchantingTable(l));
        saveTables();
    }
    
    public static void deregisterTable(Location l) {
        EnchantingTable t = tables.get(l);
        tables.remove(l);
        t.emptyTable();
        saveTables();
    }
    
    public static String getStringLocation(final Location l) {
    if (l == null) {
        return "";
    }
    return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }
    public static Location getLocationString(final String s) {
    if (s == null || s.trim().equals("")) {
        return null;
    }
    final String[] parts = s.split(",");
    if (parts.length == 4) {
        final World w = plugin.getServer().getWorld(parts[0]);
        final int x = Integer.parseInt(parts[1]);
        final int y = Integer.parseInt(parts[2]);
        final int z = Integer.parseInt(parts[3]);
        return new Location(w, x, y, z);
    }
    return null;
    }
 
    public static void saveTables() {
        //TODO 
        tableConfig.clear();
        for(Entry<Location, EnchantingTable> entry : tables.entrySet()) {
            tableConfig.getConfig().set("tables."+getStringLocation(entry.getKey()),entry.getValue().getContents());
        }
        tableConfig.saveConfig();
    }
    
    public static void loadTables() {
        tables.clear();
        if(!tableConfig.getConfig().isConfigurationSection("tables")) return;
        Set<String> locs = tableConfig.getConfig().getConfigurationSection("tables").getKeys(false);
        for(String s : locs) {
            Location l = getLocationString(s);
            List<ItemStack> contents = (List<ItemStack>)tableConfig.getConfig().getList("tables."+s);
            tables.put(l, new EnchantingTable(l, contents));
        }
    }
    
}
