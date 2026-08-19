package com.rschao.plugins.divineObjects.enchant;

import com.rschao.enchants.DimensionalManipEnchant;
import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.divineObjects.enchant.tech.OblivionKingTechs;
import com.rschao.plugins.divineObjects.enchant.tech.PrimalKatana;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.register.TechniqueNameManager;
import com.rschao.plugins.techniqueAPI.tech.util.PlayerTechniqueManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OblivionKing extends EasyEnchant {
    final String name;
    public OblivionKing() {
        super("oblivion_blade", "showdowncore", ChatColor.DARK_RED + ColorCodes.BOLD.getCode() + "Oblivion's blood");
        CustomEnchantment e = this.getCustomEnchantment();
        e.setMaxLevel(1);
        e.setSupportedItem("#minecraft:enchantable/sharp_weapon");
        saveBukkitEnchantment(e);
        name = ChatColor.DARK_RED + ColorCodes.BOLD.getCode() + "Oblivion's blood";
        OblivionKingTechs.register();
    }

    Map<Player, Boolean> hasDropped = new HashMap<>();
    String groupId = "divine_oblivion_blade";
    Map<Player, String> chosenId = new HashMap<>();
    Enchantment e;
    @EventHandler(priority = EventPriority.HIGH)
    void onMagic(PlayerInteractEvent event) {
        if(hasDropped.getOrDefault(event.getPlayer(), false)) return;
        e = this.getCustomEnchantment().toBukkitEnchantment();
        ItemStack item = event.getItem();
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        Enchantment en = this.getCustomEnchantment().toBukkitEnchantment();
        Enchantment byKey = Enchantment.getByKey(this.getKey());

        boolean hasEnchant = (en != null && item.containsEnchantment(e))
                || (byKey != null && item.containsEnchantment(byKey));
        if (!hasEnchant) return; // <-- CORRECCIÓN: antes devolvías cuando SÍ tenía la encantación


        String group = chosenId.getOrDefault(event.getPlayer(), groupId);

        if(event.getItem().getItemMeta().hasEnchant(e)){
            if(!event.getPlayer().isSneaking()) return;
            event.setCancelled(true);
            Player p = event.getPlayer();

            int techIndex;
            techIndex = PlayerTechniqueManager.getCurrentTechnique(event.getPlayer().getUniqueId(), group);
            if(event.getAction().toString().contains("LEFT")){
                Technique technique = TechRegistry.getAllTechniques(group).get(techIndex);
                if(technique == null) return;
                technique.use(new TechniqueContext(p, p.getInventory().getItemInMainHand()));
            }
            else if(event.getAction().toString().contains("RIGHT")){
                PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), group, (techIndex + 1) % TechRegistry.getAllTechniques(group).size());
                techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), group);
                p.sendMessage("You have switched to technique: " + TechniqueNameManager.getDisplayName(p, TechRegistry.getAllTechniques(group).get(techIndex)));
            }

        }
    }
    @EventHandler
    void onSwitchToChaos(PlayerItemHeldEvent event) {
        e = this.getCustomEnchantment().toBukkitEnchantment();
        if(e == null) return;
        ItemStack sword = event.getPlayer().getInventory().getItem(event.getNewSlot());
        Player player = event.getPlayer();
        if(sword == null) return;
        if(!sword.hasItemMeta()) return;
        if(sword.getItemMeta().hasEnchant(e)){
            String group = chosenId.getOrDefault(event.getPlayer(), groupId);
            int techIndex = PlayerTechniqueManager.getCurrentTechnique(event.getPlayer().getUniqueId(), group);
            List<Technique> techs = TechRegistry.getAllTechniques(group);
            if (techs == null || techs.isEmpty() || techIndex < 0 || techIndex >= techs.size()) {
                hotbarMessage.sendHotbarMessage(player, "No technique selected.");
                return;
            }
            String techName = techs.get(techIndex).getDisplayName();
            if(Objects.equals(group, groupId)){
                hotbarMessage.sendHotbarMessage(player, "Technique: " + techName + " (Enchant " + this.name +  ChatColor.RESET + ")" );
            }
            else{
                hotbarMessage.sendHotbarMessage(player, "Technique: " + techName + " (Enchant " + ChatColor.DARK_PURPLE + ColorCodes.BOLD.getCode() + "Dimensional Manipulator" +  ChatColor.RESET + ")" );
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onDrop(PlayerDropItemEvent ev) {
        e = this.getCustomEnchantment().toBukkitEnchantment();
        Player p = ev.getPlayer();
        if(!p.isSneaking()) return;
        ItemStack item = ev.getItemDrop().getItemStack();
        if(!item.hasItemMeta()) return;
        if(item.containsEnchantment(e)){
            CustomEnchantment dimentioEnchant = new DimensionalManipEnchant().getCustomEnchantment();
            ItemStack helm = p.getInventory().getHelmet();
            if(!helm.hasItemMeta()) return;
            if(helm.containsEnchantment(dimentioEnchant.toBukkitEnchantment())){
                ev.setCancelled(true);
                hasDropped.put(p, true);
                Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> hasDropped.put(p, false), 20L);
                String str = chosenId.getOrDefault(p, groupId);
                if(Objects.equals(str, groupId)){
                    chosenId.put(p, "magician");
                    p.sendMessage("You have switched to enchantment " + ChatColor.DARK_PURPLE + ColorCodes.BOLD.getCode() + "Dimensional Manipulator");
                }
                else {
                    chosenId.put(p, groupId);
                    p.sendMessage("You have switched to enchantment " + this.name);
                }
            }
        }
    }

    public boolean hasEnchantInInv(Player p){
        for(ItemStack item : p.getInventory().getContents()){
            if(item == null) continue;
            if(!item.hasItemMeta()) continue;
            if(item.containsEnchantment(this.getCustomEnchantment().toBukkitEnchantment())) return true;
        }
        return false;
    }
}
