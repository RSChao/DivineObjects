package com.rschao.plugins.divineObjects.event;

import com.rschao.enchants.OblivionEnchant;
import com.rschao.events.definitions.ItemOblivionEvent;
import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.divineObjects.enchant.DivineBlessing;
import com.rschao.plugins.divineObjects.enchant.GodlyEmblem;
import com.rschao.plugins.divineObjects.enchant.tech.AegisCoresTechniques;
import com.rschao.plugins.divineObjects.enchant.tech.DivineEmblem;
import com.rschao.plugins.divineObjects.enchant.tech.PrimalKatana;
import com.rschao.plugins.divineObjects.event.definition.KatanaSheathEvent;
import com.rschao.plugins.divineObjects.item.DivineItems;
import com.rschao.plugins.divineObjects.item.TempleTeleportItem;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.context.TechniqueContext;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.bukkit.util.Vector;

import java.util.*;

public class Events implements Listener {

    public static Map<Player, Boolean> isSheathTechOn = new HashMap<>();
    public static Map<Player, Boolean> isGlitchTechOn = new HashMap<>();
    public static Map<Player, Boolean> isOblivionTechOn = new HashMap<>();
    public static Map<Player, Boolean> isOblivionGenoTechOn = new HashMap<>();
    public static Map<Player, List<ItemStack>> forgottenItems = new HashMap<>();


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


    @EventHandler
    void onPlayerClick(PlayerInteractEvent ev){
        if(ev.getItem() == null) return;
        if(!ev.getItem().hasItemMeta()) return;
        if(ev.getItem().getItemMeta().getPersistentDataContainer().has(TempleTeleportItem.key)){
            TempleTeleportItem.teleportToTemple(ev.getPlayer(), ev.getItem());
        }
    }




    @EventHandler
    void onPlayerGlitchTech(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player damager)) return;
        if (!isGlitchTechOn.getOrDefault(damager, false)) return;
        Entity target = e.getEntity();
        if (!(target instanceof LivingEntity le)) return;
        le.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20*20, 234));
        le.addPotionEffect(PotionEffectType.SLOWNESS.createEffect(20*20, 234));
    }

    @EventHandler
    void onPlayerOblivionTech(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player damager)) return;
        if (!isOblivionTechOn.getOrDefault(damager, false)) return;
        Entity target = e.getEntity();
        if (!(target instanceof Player pl)) return;
        int rng = new Random().nextInt(100);
        if(rng < 40) OblivionEnchant.oblivion(damager, pl);
    }

    @EventHandler
    void DamageEffects (EntityDamageByEntityEvent ev){
        if(!(ev.getDamager() instanceof Player killer)) return;
        if(!(ev.getEntity() instanceof Player player)) return;
        if(isOblivionGenoTechOn.getOrDefault(killer, false)) {
            List<PotionEffect> effects = new ArrayList<>(player.getActivePotionEffects());
            int dmg = 400;
            ev.setDamage(dmg); // Example: double the damage
            player.sendMessage(ChatColor.DARK_RED + "=}");
            ev.getEntity().sendMessage(ChatColor.DARK_RED + "Enjoy =}");
            for(PotionEffect e : effects){
                if(e.getType().getCategory().equals(PotionEffectTypeCategory.HARMFUL)) continue;
                killer.addPotionEffect(e);
            }
        }
    }

    @EventHandler
    void onInteractWithCore(PlayerInteractEvent ev){
        Player p = ev.getPlayer();
        ItemStack i = ev.getItem();
        if(i == null) return;
        if(!i.hasItemMeta()) return;
        if(i.getItemMeta().hasEnchant(new DivineBlessing().getCustomEnchantment().toBukkitEnchantment())){
            if(i.isSimilar(DivineItems.LogosAegisCore())){
                i.setAmount(0);
                AegisCoresTechniques.logos.use(p);
                Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () ->{
                    p.getLocation().getWorld().dropItemNaturally(p.getLocation(), DivineItems.LogosAegisCore());
                    p.sendMessage("A Shadow Aegis Core has been dropped!");
                }, 20*60*5);
            }
        }
    }

    @EventHandler
    void onOblivion(ItemOblivionEvent ev){
        Player p = ev.getPlayer();
        ItemStack i = ev.getItem();
        List<ItemStack> items = forgottenItems.getOrDefault(p, new ArrayList<>());
        items.add(i);
        forgottenItems.put(p, items);
    }

}
