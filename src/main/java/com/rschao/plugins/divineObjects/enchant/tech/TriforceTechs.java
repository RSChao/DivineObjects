package com.rschao.plugins.divineObjects.enchant.tech;

import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TriforceTechs {

    static final String ID = "divine_triforce_sword";
    static final Plugin pl = Plugin.getPlugin(Plugin.class);

    public static void register(){
        TechRegistry.registerTechnique(ID, chickens);
        TechRegistry.registerTechnique(ID, deku);
    }

    static Technique chickens = new Technique("chicken_war", "Chicken war", false, cooldownHelper.minutesToMiliseconds(4), List.of("Spawns buffed chicken jockeys"), TargetSelectors.radialPlayers(40), (ctx, token) -> {
        for(LivingEntity e : ctx.targets()){
            Location loc = e.getLocation().add(0,1,0);
            World w = loc.getWorld();

            Chicken chicken = w.spawn(loc, Chicken.class);

            Zombie zombie = w.spawn(loc, Zombie.class);
            zombie.setBaby(true);
            zombie.setCustomName("Chicken Jockey");
            zombie.setCustomNameVisible(true);
            ItemStack h = new ItemStack(Material.NETHERITE_HELMET);
            ItemMeta hm = h.getItemMeta();
            hm.addEnchant(Enchantment.PROTECTION, 2, false);
            h.setItemMeta(hm);
            zombie.getEquipment().setHelmet(h);
            ItemStack c = new ItemStack(Material.NETHERITE_HELMET);
            ItemMeta cm = c.getItemMeta();
            cm.addEnchant(Enchantment.PROTECTION, 2, false);
            c.setItemMeta(cm);
            zombie.getEquipment().setChestplate(c);
            ItemStack l = new ItemStack(Material.NETHERITE_HELMET);
            ItemMeta lm = l.getItemMeta();
            lm.addEnchant(Enchantment.PROTECTION, 2, false);
            l.setItemMeta(lm);
            zombie.getEquipment().setLeggings(l);
            ItemStack b = new ItemStack(Material.NETHERITE_HELMET);
            ItemMeta bm = h.getItemMeta();
            bm.addEnchant(Enchantment.PROTECTION, 2, false);
            b.setItemMeta(bm);
            zombie.getEquipment().setBoots(b);
            zombie.getEquipment().setHelmetDropChance(0);
            zombie.getEquipment().setChestplateDropChance(0);
            zombie.getEquipment().setLeggingsDropChance(0);
            zombie.getEquipment().setBootsDropChance(0);
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2*60*60, 2)); // 60s speed II
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 2*60*60, 2)); // 60s speed II

            chicken.addPassenger(zombie);
        }
    });

    static Technique deku = new Technique("deku", "Deku", false, cooldownHelper.minutesToMiliseconds(5), List.of("Pay a small price,", "obtain temporari immunity"), TargetSelectors.self(), (ctx, token) ->{
        Player p = ctx.caster();
        double health = p.getHealth();
        if(health-14 <= 0){
            p.setHealth(1);
            p.setNoDamageTicks(0);
            p.damage(999);
        }
        else p.setHealth(health-14);
        p.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20*3, 255));
        p.addPotionEffect(PotionEffectType.SLOWNESS.createEffect(20*3, 255));
        p.addPotionEffect(PotionEffectType.JUMP_BOOST.createEffect(20*3, 255));
        p.addPotionEffect(PotionEffectType.MINING_FATIGUE.createEffect(20*3, 255));

        Bukkit.getScheduler().runTaskLater(pl, () ->{
            p.addPotionEffect(PotionEffectType.RESISTANCE.createEffect(30, 255));
            p.addPotionEffect(PotionEffectType.STRENGTH.createEffect(3*20, 2));
            p.addPotionEffect(PotionEffectType.SPEED.createEffect(3*20, 2));
            p.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(3*20, 4));

        }, 20*3);
    });
}
