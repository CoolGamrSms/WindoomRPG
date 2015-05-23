/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shaneschulte.plugins.windoomrpg.skills.warrior;

import com.shaneschulte.plugins.windoomrpg.WindoomRPG;
import com.sucy.skill.api.skills.Skill;
import com.sucy.skill.api.skills.SkillAttribute;
import com.sucy.skill.api.skills.SkillShot;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.TurnEffect;
import de.slikey.effectlib.effect.TurnPlayerEffect;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Shane
 */
public class SpinAttack extends Skill implements SkillShot, Listener {
    
    public static final String NAME = "Spin Attack";
    private WindoomRPG plugin;
    
    public SpinAttack(WindoomRPG pl) {
        super(NAME, "AOE Attack", Material.DIAMOND_SWORD, 5);
        plugin = pl;

        getDescription().add("Spin a circle");
        getDescription().add("damaging nearby");
        getDescription().add("enemies");

        settings.set(SkillAttribute.LEVEL, 1, 1);
        settings.set(SkillAttribute.COST, 1, 0);
        settings.set(SkillAttribute.COOLDOWN, 10, -1);
        settings.set(SkillAttribute.MANA, 0);
    }

    @Override
    public boolean cast(LivingEntity le, int i) {
        Player p = (Player)le;
        TurnEffect tpe = new TurnEffect(plugin.effectManager);
        tpe.setEntity(p);
        tpe.step *= 4;
        tpe.iterations /= 8;
        tpe.start();
        CircleEffect ce = new CircleEffect(plugin.effectManager);
        ce.angularVelocityX = 0;
        ce.angularVelocityZ = 0;
        ce.setEntity(p);
        ce.particle = ParticleEffect.CRIT;
        ce.radius = 2.5f;
        ce.period = 1;
        ce.iterations = tpe.iterations;
        ce.start();
        p.playSound(p.getLocation(), Sound.WITHER_SHOOT, 0.8f, 0f);
        for(Entity e : le.getNearbyEntities(3, 3, 3)) {
            if(e instanceof LivingEntity) {
                LivingEntity t = (LivingEntity)e;
                t.damage(5, le);
            }
        }
        return true;
    }
    
}
