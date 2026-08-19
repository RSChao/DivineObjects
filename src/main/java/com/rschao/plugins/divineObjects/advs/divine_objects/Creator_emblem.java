package com.rschao.plugins.divineObjects.advs.divine_objects;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.plugins.divineObjects.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Creator_emblem extends BaseAdvancement implements HiddenVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.divine_objects_NAMESPACE, "creator_emblem");

  static ItemStack icon = new ItemStack(Material.NETHER_STAR);
  static{
    ItemMeta meta = icon.getItemMeta();
    meta.setItemModel(NamespacedKey.minecraft("emblem_creator"));
    icon.setItemMeta(meta);
  }

  public Creator_emblem(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(icon, "§7Creator's Emblem", AdvancementFrameType.CHALLENGE, true, true,  x, y , "§aObtain the power of §7Creator of Showdown§a, the magic of the God of Creation"), parent, 1);
    this.registerEvent(PlayerPickupItemEvent.class, event -> {
      ItemStack item = event.getItem().getItemStack();
      if(item == null) return;
      if(!item.hasItemMeta()) return;
      if(item.getItemMeta().hasItemModel()) {
        if(item.getItemMeta().getItemModel().equals(NamespacedKey.minecraft("emblem_creator"))) {
          this.incrementProgression(event.getPlayer().getUniqueId());
        }
      }
    });
  }
}