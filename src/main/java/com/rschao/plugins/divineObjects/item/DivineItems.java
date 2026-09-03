package com.rschao.plugins.divineObjects.item;

import com.rschao.enchants.GenoEnchant;
import com.rschao.enchants.GlitchEnchant;
import com.rschao.enchants.OblivionEnchant;
import com.rschao.enchants.WitherEnchant;
import com.rschao.items.weapons;
import com.rschao.plugins.divineObjects.enchant.*;
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
    public static ItemStack primalKatanaAwakened(Player p){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Primordial Blade of Oblivion");
        meta.setLore(java.util.Arrays.asList(
                ChatColor.GRAY + "A blade forged by the god",
                ChatColor.GRAY + "who created all of Showdown.",
                ChatColor.GRAY + "It is said that this blade",
                ChatColor.GRAY + "contains the power,",
                ChatColor.GRAY + "of the Aegis of Atemporality."
        ));
        meta.addEnchant((new DivineForgery()).getCustomEnchantment().toBukkitEnchantment(), 3, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 4, true);
        meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        meta.addEnchant((new GenoEnchant()).getCustomEnchantment().toBukkitEnchantment(), 4, true);
        meta.addEnchant((new OblivionEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new GlitchEnchant()).getCustomEnchantment().toBukkitEnchantment(), 2, true);
        meta.addEnchant((new WitherEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        Enchantment e = EnchantmentRegistry.getCustomEnchantment("minecraft", "drain");
        if(e != null){
            meta.addEnchant(e, 3, true);
        }
        meta.addEnchant((new BladeOfTheEnd()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        MainHand mh = p.getMainHand();
        meta.setItemModel(NamespacedKey.minecraft("oblivion_katana_aegis_" + ((mh.equals(MainHand.RIGHT)) ? "r" : "l")));
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
        meta.setItemName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Showdown " + ChatColor.GOLD + "" + ChatColor.BOLD + "SMP" + ChatColor.LIGHT_PURPLE + " Divine Emblem");
        meta.setLore(List.of(
                ChatColor.GRAY + "An emblem that radiates divine power.",
                ChatColor.GRAY + "Those who wield it are considered gods among mortals,",
                ChatColor.GRAY + "but breaking the emblem takes the user's soul as payment."
        ));
        meta.addEnchant((new DivineBlessing()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.getPersistentDataContainer().set(weapons.CHKey, PersistentDataType.INTEGER, 0);
        meta.setItemModel(new NamespacedKey("minecraft", "emblem_3d"));
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack oblivionSword(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLACK + "" + ChatColor.BOLD + "Oblivion King's blade");
        meta.setLore(java.util.Arrays.asList(
                ChatColor.GRAY + "A blade wielded by the Butcher of Oblivion.",
                ChatColor.GRAY + "It has the power to kill worlds"
        ));
        meta.addEnchant((new DivineForgery()).getCustomEnchantment().toBukkitEnchantment(), 2, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
        meta.addEnchant((new GenoEnchant()).getCustomEnchantment().toBukkitEnchantment(), 3, true);
        meta.addEnchant((new OblivionEnchant()).getCustomEnchantment().toBukkitEnchantment(), 4, true);
        meta.addEnchant((new GlitchEnchant()).getCustomEnchantment().toBukkitEnchantment(), 2, true);
        meta.addEnchant((new WitherEnchant()).getCustomEnchantment().toBukkitEnchantment(), 2, true);
        Enchantment e = EnchantmentRegistry.getCustomEnchantment("minecraft", "drain");
        if(e != null){
            meta.addEnchant(e, 3, true);
        }
        meta.addEnchant((new OblivionKing()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.setItemModel(NamespacedKey.minecraft("origin_sword"));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack MasterSword(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Master Sword");
        meta.setLore(java.util.Arrays.asList(
                ChatColor.GRAY + "A blade with an unknown power.",
                ChatColor.GRAY + "Might still be upgradable"
        ));
        meta.addEnchant(Enchantment.SHARPNESS, 10, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
        meta.addEnchant((new GenoEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new OblivionEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new GlitchEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new WitherEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        Enchantment e = EnchantmentRegistry.getCustomEnchantment("minecraft", "drain");
        if(e != null){
            meta.addEnchant(e, 3, true);
        }
        meta.addEnchant((new DivineBlessing()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.addEnchant((new TriforceSword()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.setItemModel(NamespacedKey.minecraft("master_sword"));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack AegisSword(){
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Sword of the Aegis");
        meta.setLore(java.util.Arrays.asList(
                ChatColor.LIGHT_PURPLE + "A sword wielded by the Aegis of Memory,",
                ChatColor.LIGHT_PURPLE + "better known as the Spirit of Showdown.",
                ChatColor.LIGHT_PURPLE + "This blade harnesses the power of Aishia herself"
        ));
        meta.addEnchant((new DivineForgery()).getCustomEnchantment().toBukkitEnchantment(), 2, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 5, true);
        meta.addEnchant((new AegisBlessingEnchant()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        Enchantment e = EnchantmentRegistry.getCustomEnchantment("minecraft", "drain");
        if(e != null){
            meta.addEnchant(e, 3, true);
        }
        meta.addEnchant((new AegisBlade()).getCustomEnchantment().toBukkitEnchantment(), 1, true);
        meta.setItemModel(NamespacedKey.minecraft("aegis_sword"));
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack AishiaAegisCore() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setEnchantmentGlintOverride(true);
        meta.setItemModel(NamespacedKey.minecraft("aegis_crystal"));
        meta.setItemName(ChatColor.GREEN + "Aegis Core Crystal");
        meta.setMaxStackSize(1);
        meta.setLore(List.of(
                "Core from which a powerful life form can be created",
                "Belongs to Aishia, the Spirit of Showdown"));
        meta.addEnchant(new DivineBlessing().getCustomEnchantment().toBukkitEnchantment(), 1, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack LogosAegisCore() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setEnchantmentGlintOverride(true);
        meta.setItemModel(NamespacedKey.minecraft("aegis_crystal_shadow"));
        meta.setItemName(ChatColor.BLACK + "Aegis Core Crystal");
        meta.setMaxStackSize(1);
        meta.setLore(List.of(
                "Core from which a powerful life form can be created",
                "Belongs to Logos, the Killer of Time"));
        meta.addEnchant(new DivineBlessing().getCustomEnchantment().toBukkitEnchantment(), 1, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack RedAegisCore() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setEnchantmentGlintOverride(true);
        meta.setItemModel(NamespacedKey.minecraft("aegis_crystal_red"));
        meta.setItemName(ChatColor.RED + "Aegis Core Crystal");
        meta.setMaxStackSize(1);
        meta.setLore(List.of(
                "Core from which a powerful life form can be created",
                "Belongs to Origin, the Butcher of Oblivion"));
        meta.addEnchant(new DivineBlessing().getCustomEnchantment().toBukkitEnchantment(), 1, true);
        item.setItemMeta(meta);
        return item;
    }

}
