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

public class Aegis_blade extends BaseAdvancement implements HiddenVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.divine_objects_NAMESPACE, "aegis_blade");

  static ItemStack icon = new ItemStack(Material.NETHER_STAR);
  static{
    ItemMeta meta = icon.getItemMeta();
    meta.setItemModel(NamespacedKey.minecraft("aegis_sword"));
    icon.setItemMeta(meta);
  }

  public Aegis_blade(Advancement parent, float x, float y) {
    super(KEY.getKey(), new AdvancementDisplay(icon, "§a§lAegis's blade", AdvancementFrameType.CHALLENGE, true, true,  x, y , "Obtain the §a§lAegis's Blade§r, blade wielded by the §dAishia, the Spirit of Showdown" ), parent, 1);
    this.registerEvent(PlayerPickupItemEvent.class, event -> {
      ItemStack[] items = event.getPlayer().getInventory().getContents();
      for (ItemStack item : items) {
        if(item == null) continue;
        if(!item.hasItemMeta()) continue;
        if(item.getItemMeta().hasItemModel()) {
          if(item.getItemMeta().getItemModel().equals(NamespacedKey.minecraft("aegis_sword"))) {
            this.incrementProgression(event.getPlayer());
            break;
          }
        }
      }
    });
  }
}