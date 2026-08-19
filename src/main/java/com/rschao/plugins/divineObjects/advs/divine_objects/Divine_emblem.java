package com.rschao.plugins.divineObjects.advs.divine_objects;
import com.fren_gor.ultimateAdvancementAPI.util.AdvancementKey;
import com.rschao.plugins.divineObjects.advs.AdvancementTabNamespaces;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.visibilities.HiddenVisibility;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Divine_emblem extends RootAdvancement implements HiddenVisibility {

  public static AdvancementKey KEY = new AdvancementKey(AdvancementTabNamespaces.divine_objects_NAMESPACE, "divine_emblem");

  static ItemStack icon = new ItemStack(Material.NETHER_STAR);
  static{
    ItemMeta meta = icon.getItemMeta();
    meta.setItemModel(NamespacedKey.minecraft("emblem_3d"));
    icon.setItemMeta(meta);
  }

  public Divine_emblem(AdvancementTab tab, float x, float y) {
    super(tab ,KEY.getKey(), new AdvancementDisplay(icon, "§5§lShowdown §6§lSMP §dDivine Emblem", AdvancementFrameType.CHALLENGE, true, true,  x, y , "§aObtain the §5§lShowdown §6§lSMP §dDivine Emblem§a, the treasure of the Tribe of Ancients" ), "textures/block/raw_gold_block.png", 1);
    this.registerEvent(PlayerPickupItemEvent.class, event -> {
      ItemStack item = event.getItem().getItemStack();
      if(item == null) return;
      if(!item.hasItemMeta()) return;
      if(item.getItemMeta().hasItemModel()) {
        if(item.getItemMeta().getItemModel().equals(NamespacedKey.minecraft("emblem_3d"))) {
          this.incrementProgression(event.getPlayer().getUniqueId());
        }
      }
    });
  }
}