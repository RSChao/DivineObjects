package com.rschao.plugins.divineObjects.event;

import com.rschao.plugins.divineObjects.enchant.GodlyEmblem;
import com.rschao.plugins.divineObjects.enchant.tech.DivineEmblem;
import com.rschao.plugins.divineObjects.enchant.tech.PrimalKatana;
import com.rschao.plugins.divineObjects.event.definition.KatanaSheathEvent;
import com.rschao.plugins.divineObjects.item.DivineItems;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static com.delta.plugins.events.BossEvents.isSheathTechOn;

public class Events implements Listener {

    @EventHandler
    void onKatanaSheath(KatanaSheathEvent ev){
        Player p = ev.getPlayer();
        if(isSheathTechOn.getOrDefault(p, false)){
            isSheathTechOn.put(p, false);
            Location eye = p.getEyeLocation();
            Vector dir = eye.getDirection().clone().normalize();
            PrimalKatana.spawnSlashEffect(p);
            for (Player t : p.getWorld().getPlayers()) {
                if (t == null || !t.isValid() || t.equals(p)) continue;
                if (t.getLocation().distance(p.getLocation()) > 20) continue;

                Vector to = t.getEyeLocation().toVector().subtract(eye.toVector());
                if (to.lengthSquared() < 0.0001) continue;
                to = to.normalize();
                double dot = dir.dot(to);
                if (Double.isNaN(dot)) continue;
                double angle = Math.toDegrees(Math.acos(Math.clamp(dot, -1.0, 1.0)));
                if (angle <= 45) {
                    try {
                        t.damage(3000.0, p);
                    } catch (Throwable ignored) {}
                }
            }
        }
    }

    @EventHandler
    void onKill(PlayerDeathEvent ev){
        if(ev.getEntity().getKiller() != null){
            if(ev.getEntity().getName().equals(ev.getEntity().getKiller().getName())) return;
            Player p = ev.getEntity().getKiller();
            ItemStack offHand = p.getInventory().getItemInOffHand();
            if(offHand.hasItemMeta() && offHand.getItemMeta().hasEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment())) {
                DivineEmblem.regenerateEmblem_kills.use(p);
            } else {
                offHand = p.getInventory().getItemInMainHand();
                if(offHand.hasItemMeta() && offHand.getItemMeta().hasEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment())) {
                    DivineEmblem.regenerateEmblem_kills.use(p);
                }
            }

        }
    }
}
