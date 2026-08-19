package com.rschao.plugins.divineObjects.enchant;

import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Random;

public class AegisBlessingEnchant extends EasyEnchant {
    final String name;
    static Enchantment e;
    public AegisBlessingEnchant() {
        super("aegis_aishia_blessing", "showdowncore", ChatColor.WHITE + ColorCodes.BOLD.getCode() + "Aegis's Blessing");
        name = ChatColor.WHITE + ColorCodes.BOLD.getCode() + "Aegis's Blade";
        CustomEnchantment e = makeEnchantment("showdowncore", name);
        e.setSupportedItem("#minecraft:enchantable/sharp_weapon");
        e.setMaxLevel(1);
        e.addExcludedEnchantment("minecraft:genocidal");
        e.addExcludedEnchantment("minecraft:glitch");
        e.addExcludedEnchantment("minecraft:wither");
        saveBukkitEnchantment(e);
    }

    @Override @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        e = this.getCustomEnchantment().toBukkitEnchantment();
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof Player damaged){
            ItemStack itemUsed = player.getInventory().getItemInMainHand();
            if(itemUsed.getType() == Material.AIR) return;
            int rng = (new Random()).nextInt(0, 100);

            if(!itemUsed.getItemMeta().hasEnchant(e)) return;
            int level = 3;
            if(rng <= (level)){
                ev.setDamage(1000);
                damaged.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "You feel like you're making undesired enemies");
                player.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "Just a little bit more!");
            }
        }
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof LivingEntity damaged){
            ItemStack itemUsed = player.getInventory().getItemInMainHand();
            if(itemUsed.getType() == Material.AIR) return;
            int rng = (new Random()).nextInt(0, 100);
            if(!itemUsed.getItemMeta().hasEnchant(e)) return;
            int level = 3;
            if(rng < 28+(5*level) && rng > 28){
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3*20, 255));
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 3*20, 255));
                damaged.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "You have been Glitched");
                player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "Your enemy has been Glitched");
            }
        }
        if(ev.getDamager() instanceof Player player && ev.getEntity() instanceof LivingEntity damaged){
            if(player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
            if(!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(e)) return;
            int rng = (new Random()).nextInt(0, 100);
            if(rng > 47 || rng < 37) return;

            damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 5*20, 5));
            damaged.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "You have been Withered");
            player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GRAY + "Your enemy has been Withered");
        }

    }
}
