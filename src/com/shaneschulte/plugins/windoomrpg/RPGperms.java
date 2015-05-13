/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import org.bukkit.ChatColor;


/**
 *
 * @author Hikeru
 * 
 */

public enum RPGperms {

    //add permissions here
    USE_DIAMOND_ARMOR(WindoomRPG.p + "DiamondArmor", "This armor is too heavy." /*"You must be a Warrior to equip diamond armor"*/), 
    ENCHANT_ITEMS(WindoomRPG.p + "ItemEnchants", "This book is far to complex for me."),
    USE_IRON_ARMOR(WindoomRPG.p + "IronArmor", "This armor is too heavy."),
    USE_LOCKPICK(WindoomRPG.p + "Lockpicking", "I have no idea how to use this.");
    
    //get permissions and warnings v  v
    private final String permission;
    private final String warning;

    private RPGperms(String perm, String warning) {
        this.permission = perm;
        this.warning = ChatColor.translateAlternateColorCodes('&', warning);
    }

    public String getPermission() {
        return permission;
    }

    public String getWarning() {
        return warning;
    }
}
