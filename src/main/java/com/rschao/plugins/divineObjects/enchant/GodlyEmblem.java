package com.rschao.plugins.divineObjects.enchant;

import com.rschao.enchants.DimensionalManipEnchant;
import com.rschao.plugins.divineObjects.enchant.tech.DivineEmblem;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.register.TechniqueNameManager;
import com.rschao.plugins.techniqueAPI.tech.util.PlayerTechniqueManager;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class GodlyEmblem extends EasyEnchant {
    private final String name;
    public GodlyEmblem() {
        super("god_emblem", "showdowncore", ColorCodes.LIGHT_PURPLE + "[" + ColorCodes.DARK_PURPLE + "Godly " + ColorCodes.GOLD + "Emblem" + ColorCodes.LIGHT_PURPLE + "]");
        CustomEnchantment enchantment = getCustomEnchantment();
        enchantment.setMaxLevel(1);
        enchantment.setSupportedItem("#minecraft:enchantable/armor");
        this.saveBukkitEnchantment(enchantment);
        name = ColorCodes.LIGHT_PURPLE.getCode() + "[" + ColorCodes.DARK_PURPLE.getCode() + "Godly " + ColorCodes.GOLD.getCode() + "Emblem" + ColorCodes.LIGHT_PURPLE.getCode() + "]";
        DivineEmblem.register();

    }




    String groupId = "divine_emblem";
    Enchantment e;
    @EventHandler(priority = EventPriority.HIGH)
    void onMagic(PlayerInteractEvent event) {
        e = this.getCustomEnchantment().toBukkitEnchantment();
        ItemStack item = event.getItem();
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        Enchantment en = this.getCustomEnchantment().toBukkitEnchantment();
        Enchantment byKey = Enchantment.getByKey(this.getKey());

        boolean hasEnchant = (en != null && item.containsEnchantment(e))
                || (byKey != null && item.containsEnchantment(byKey));
        if (!hasEnchant) return; // <-- CORRECCIÓN: antes devolvías cuando SÍ tenía la encantación
        if(!event.getPlayer().getInventory().getItemInMainHand().equals(item)) return; // Asegura que el objeto encantado esté en la mano principal

        String group = groupId;

        if(event.getItem().getItemMeta().hasEnchant(e)){
            event.setCancelled(true);
            Player p = event.getPlayer();

            int techIndex;
            techIndex = PlayerTechniqueManager.getCurrentTechnique(event.getPlayer().getUniqueId(), group);
            if(event.getAction().toString().contains("LEFT")){
                Technique technique = TechRegistry.getNormalTechniques(group).get(techIndex);
                if(technique == null) return;
                technique.use(new TechniqueContext(p, p.getInventory().getItemInMainHand()));
            }
            else if(event.getAction().toString().contains("RIGHT")){
                PlayerTechniqueManager.setCurrentTechnique(p.getUniqueId(), group, (techIndex + 1) % (TechRegistry.getNormalTechniques(group).size()));
                techIndex = PlayerTechniqueManager.getCurrentTechnique(p.getUniqueId(), group);
                p.sendMessage("You have switched to technique: " + TechniqueNameManager.getDisplayName(p, TechRegistry.getNormalTechniques(group).get(techIndex)));
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
            String group = groupId;
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
        }
    }

}

