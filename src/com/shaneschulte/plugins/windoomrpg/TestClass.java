/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg;

import com.sucy.skill.api.classes.RPGClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Shane
 */
class TestClass extends RPGClass {

    public TestClass() {
        super("test", new ItemStack(Material.AIR),20);
        addSkill("Spin Attack");
        addSkill("Stomp");
    }
    
}
