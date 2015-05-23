/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.enchanting;

import com.shaneschulte.plugins.windoomrpg.enchanting.TableManager;
import com.shaneschulte.plugins.windoomrpg.enchanting.TableManager.EnchantWeight;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

/**
 *
 * @author Shane
 */
final public class EnchantingTable {
    private Location loc;
    private int power;
    ArrayList<ItemStack> contents;
    

    public EnchantingTable(Location loc) {
        this.loc   = loc;
        this.power = 0;
        contents = new ArrayList<>();
    }

    public EnchantingTable(Location loc, List<ItemStack> contents) {
        this.loc   = loc;
        this.power = 0;
        this.contents = new ArrayList<>();
        for(ItemStack i : contents) {
            this.contents.add(i);
            power += getGemPower(i);
        }
    }
    
    /**
     * Drops all of the contents of this table on the ground.
     */
    public void emptyTable() {
        for(ItemStack c : contents) {
            loc.getWorld().dropItem(loc, c);
        }
    }
    /**
     * Get the current power of this enchanting table.
     * @return The power level
     */
    public int getPower() {
        return power;
    }
    /**
     * Attempts to insert 'gems' into the enchanting table.
     * @param i - Item to attempt to add
     * @return TRUE if the item was accepted
     */
    public boolean addItem(ItemStack i) {
        if(!isGem(i)) return false;
        if(power + getGemPower(i) <= 30) {
            power += getGemPower(i);
            ItemStack c = i.clone();
            c.setAmount(1);
            contents.add(c);
            return true;
        }
        return false;
    }
    
    /**
     * Tests whether or not an item is an enchanting gem.
     * @param i - ItemStack to test
     * @return TRUE if the ItemStack is a gem
     */
    public boolean isGem(ItemStack i) {
        return (getGemPower(i) != 0);
    }
    
    public ItemStack enchant() {
        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        int level = (int)Math.round((double)power*(Math.random()*0.3d+0.85d));
        ArrayList<EnchantWeight> possiblePrune = new ArrayList<>(TableManager.eWeights);
        ArrayList<EnchantWeight> possible = (ArrayList<EnchantWeight>)possiblePrune.clone();
        for(EnchantWeight ew : possiblePrune) {
            if(level < TableManager.eLevels.get(ew.getEnchant())[0]) {
                possible.remove(ew);
            }
        }
        double totalWeight = 0.0d;
        for(EnchantWeight ew : possible) {
            totalWeight += ew.getWeight();
        }
        int index = -1;
        double random = Math.random()*totalWeight;
        for(int i = 0; i < possible.size(); ++i) {
            random -= possible.get(i).getWeight();
            if(random <= 0.0d) {
                index = i;
                break;
            }
        }
        Enchantment e = possible.get(index).getEnchant();
        int[] eInfo = TableManager.eLevels.get(e);
        int x = eInfo[0];
        int strength = 0;
        while(x <= level) {
            x += eInfo[1];
            ++strength;
        }
        strength = Math.min(strength, eInfo[2]);
        EnchantmentStorageMeta esm = (EnchantmentStorageMeta)is.getItemMeta();
        esm.addStoredEnchant(e, strength, true);
        is.setItemMeta(esm);
        power = 0;
        contents.clear();
        return is;
    }
    
    /**
     * Returns the strength of the enchanting gem.
     * @param i - ItemStack to get value of
     * @return value of ItemStack in enchanting process
     */
    public int getGemPower(ItemStack i) {
        Material m = i.getType();
        switch(m) {
            case DIAMOND:     return 6;
            case EMERALD:     return 10;
            case IRON_INGOT:  return 1;
            case GOLD_INGOT:  return 3;
            case LAPIS_BLOCK: return 1;
        }
        return 0;
    }
    public List<ItemStack> getContents() {
        return contents;
    }
}
