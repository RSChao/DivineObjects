package com.rschao.plugins.divineObjects.item;

import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.divineObjects.animation.PortalAnim;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class TempleTeleportItem {

    public static final NamespacedKey key = new NamespacedKey("divinetemple", "teleport_loc");
    public static ItemStack createTempleTeleporter(Location loc, String item){
        ItemStack i = new ItemStack(randomizeMaterialForGem());
        ItemMeta meta = i.getItemMeta();
        meta.setEnchantmentGlintOverride(true);
        meta.setItemName(ChatColor.LIGHT_PURPLE + "Divine Key Gemstone");
        meta.setLore(List.of("Grants access to a divine temple.", "Those who use it may obtain a Divine Object.", "Divine object linked", item));
        meta.setMaxStackSize(1);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, fromLocation(loc));
        i.setItemMeta(meta);
        return i;
    }


    public static void teleportToTemple(Player player, ItemStack teleporter){
        final Location loc = player.getEyeLocation().clone().add(0, 7, 0);
        new BukkitRunnable(){
            int ticks = 0;
            @Override
            public void run() {
                PortalAnim.anim(new Vector(0, 1, 0), loc, 2, new Particle.DustOptions(Color.AQUA, 1), new Particle.DustOptions(Color.PURPLE, 0.6f));
                player.addPotionEffect(PotionEffectType.LEVITATION.createEffect(20, 0));
                ticks++;
                if(ticks >=20*6 || player.getEyeLocation().getY()-loc.getY() >=1) {
                    this.cancel();
                    String loc = teleporter.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "null");
                    Location location = fromString(loc);
                    player.teleport(location);
                }
            }
        }.runTaskTimer(Plugin.getPlugin(Plugin.class), 0, 1);
    }


    private static Material randomizeMaterialForGem(){
        List<Material> mats = List.of(Material.EMERALD, Material.DIAMOND, Material.AMETHYST_SHARD, Material.QUARTZ);
        return mats.get(new Random().nextInt(4));
    }

    private static Location fromString(String loc){
        String[] data = loc.split(",");
        if(data.length < 4) return null;
        World w = Bukkit.getWorld(data[0]);
        if(w == null) return null;
        double x = Double.valueOf(data[1]);
        double y = Double.valueOf(data[2]);
        double z = Double.valueOf(data[3]);
        return new Location(w, x, y, z);
    }

    private static String fromLocation(Location loc){
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }
}
