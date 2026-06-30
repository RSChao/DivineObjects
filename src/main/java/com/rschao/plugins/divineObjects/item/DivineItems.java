package com.rschao.plugins.divineObjects.item;

import com.rschao.enchants.GenoEnchant;
import com.rschao.enchants.GlitchEnchant;
import com.rschao.enchants.OblivionEnchant;
import com.rschao.enchants.WitherEnchant;
import com.rschao.items.weapons;
import com.rschao.plugins.divineObjects.enchant.DivineBlessing;
import com.rschao.plugins.divineObjects.enchant.DivineForgery;
import com.rschao.plugins.divineObjects.enchant.GodlyEmblem;
import com.rschao.plugins.divineObjects.enchant.PrimalOblivion;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.registry.EnchantmentRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class DivineItems {
    public static ItemStack primalKatana(Player p){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Primal Oblivion Katana");
        meta.setLore(java.util.Arrays.asList(
                ChatColor.GRAY + "A blade forged by the god",
                ChatColor.GRAY + "who created all of Showdown.",
                ChatColor.GRAY + "It is said that this blade",
                ChatColor.GRAY + "can cut through anything,",
                ChatColor.GRAY + "even time and space."
        ));
        meta.addEnchant((new DivineForgery()).getCustomEnchantment().toBukkitEnchantment(), 3, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 4, true);
        meta.addEnchant((new GenoEnchant()).getCustomEnchantment().toBukkitEnchantment(), 4, true);
        meta.addEnchant((new OblivionEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new GlitchEnchant()).getCustomEnchantment().toBukkitEnchantment(), 2, true);
        meta.addEnchant((new WitherEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        Enchantment e = EnchantmentRegistry.getCustomEnchantment("minecraft", "drain");
        if(e != null){
            meta.addEnchant(e, 3, true);
        }
        meta.addEnchant((new PrimalOblivion()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        MainHand mh = p.getMainHand();
        meta.setItemModel(NamespacedKey.minecraft("oblivion_katana_" + ((mh.equals(MainHand.RIGHT)) ? "r" : "l")));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack primalKatana(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Primal Oblivion Katana");
        meta.setLore(java.util.Arrays.asList(
                ChatColor.GRAY + "A blade forged by the god",
                ChatColor.GRAY + "who created all of Showdown.",
                ChatColor.GRAY + "It is said that this blade",
                ChatColor.GRAY + "can cut through anything,",
                ChatColor.GRAY + "even time and space."
        ));
        meta.addEnchant((new DivineForgery()).getCustomEnchantment().toBukkitEnchantment(), 3, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 4, true);
        meta.addEnchant((new GenoEnchant()).getCustomEnchantment().toBukkitEnchantment(), 4, true);
        meta.addEnchant((new OblivionEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new GlitchEnchant()).getCustomEnchantment().toBukkitEnchantment(), 2, true);
        meta.addEnchant((new WitherEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        Enchantment e = EnchantmentRegistry.getCustomEnchantment("minecraft", "drain");
        if(e != null){
            meta.addEnchant(e, 3, true);
        }
        meta.addEnchant((new PrimalOblivion()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        MainHand mh = MainHand.RIGHT; // Default to right hand if no player context
        meta.setItemModel(NamespacedKey.minecraft("oblivion_katana_" + ((mh.equals(MainHand.RIGHT)) ? "r" : "l")));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack divineEmblem(){
        ItemStack i = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = i.getItemMeta();
        meta.setItemName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Showdown SMP Divine Emblem");
        meta.setLore(List.of(
                ChatColor.GRAY + "An emblem that radiates divine power.",
                ChatColor.GRAY + "Those who wield it are considered gods among mortals,",
                ChatColor.GRAY + "but breaking the emblem takes the user's soul as payment."
        ));
        meta.addEnchant((new DivineBlessing()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.getPersistentDataContainer().set(weapons.CHKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(new NamespacedKey("minecraft", "emblem"));
        i.setItemMeta(meta);
        return i;
    }
}
