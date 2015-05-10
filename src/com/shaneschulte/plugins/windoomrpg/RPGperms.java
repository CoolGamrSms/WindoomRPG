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
    EQUIP_DIAMOND_ARMOR(WindoomRPG.p + "wDiamondArmor", "This armor is too heavy. &eClass: &cWarrior" /*"You must be a Warrior to equip diamond armor"*/), 
    BREW_POTIONS(WindoomRPG.p + "mPotionMaking", "The magic on this device confuses me. &eClass: &cMage"),
    ENCHANT_ITEMS(WindoomRPG.p + "mItemEnchants", "This book is far to complex for me. &eClass: &cMage"),
    CRAFT_CHAINMAIL(WindoomRPG.p + "rCraftChainmail", "I am not skilled enough to do this. &eClass: &cRouge");
    
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
