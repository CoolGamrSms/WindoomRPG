/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import net.md_5.bungee.api.ChatColor;

/**
 *
 * @author Hikeru
 * 
 */

public enum Permissions {

    //add permissions here
    EQUIP_DIAMOND_ARMOR(WindoomRPG.p + "equipdiamondarmors", "This armor feels too heavy. &eClass: &cWarrior" /*"You must be a Warrior to equip diamond armor"*/), 
    ;
    
    //get permissions and warnings v  v
    private final String permission;
    private final String warning;

    private Permissions(String perm, String warning) {
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
