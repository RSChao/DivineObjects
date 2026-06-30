package com.rschao.plugins.divineObjects.enchant.tech;

import com.craftmend.openaudiomc.api.ClientApi;
import com.craftmend.openaudiomc.api.MediaApi;
import com.craftmend.openaudiomc.api.clients.Client;
import com.craftmend.openaudiomc.api.media.Media;
import com.delta.plugins.events.BossEvents;
import com.delta.plugins.events.events;
import com.delta.plugins.techs.roaring_soul;
import com.rschao.enchants.OblivionEnchant;
import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.divineObjects.enchant.PrimalOblivion;
import com.rschao.plugins.divineObjects.event.definition.KatanaSheathEvent;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.cooldown.CooldownManager;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PrimalKatana {
    static final String TECH_ID = "divine_primal_katana";
    static final Plugin plugin = Plugin.getPlugin(Plugin.class);
    public static void register() {
        TechRegistry.registerTechnique(TECH_ID, change_model);
        TechRegistry.registerTechnique(TECH_ID, oblivionSlash);
        TechRegistry.registerTechnique(TECH_ID, sheathOfOblivion);
        TechRegistry.registerTechnique(TECH_ID, witheringWorld);
        TechRegistry.registerTechnique(TECH_ID, EternalGlitch);
        TechRegistry.registerTechnique(TECH_ID, forgotten_magic);
        TechRegistry.registerTechnique(TECH_ID, katana_world_apocalypse);
    }

    static Technique change_model = new Technique("change_model", "Change Model", new TechniqueMeta(false, 0, List.of("Changes the model of the weapon")), TargetSelectors.self(), (ctx, token) ->{
        Player p = ctx.caster();
        ItemStack i = p.getInventory().getItemInMainHand();
        if(i.containsEnchantment(new PrimalOblivion().getCustomEnchantment().toBukkitEnchantment())) {
            String m = i.getItemMeta().getItemModel().getKey();
            ItemMeta meta = i.getItemMeta();
            if(m.equals("oblivion_katana_l")) {
                meta.setItemModel(NamespacedKey.minecraft("oblivion_katana_r"));
                i.setItemMeta(meta);
                p.sendMessage("Switched to right hand model!");
                if(p.getMainHand().equals(MainHand.LEFT)){
                    KatanaSheathEvent sheathEvent = new KatanaSheathEvent(p);
                    Bukkit.getPluginManager().callEvent(sheathEvent);
                }
            }
            else if(m.equals("oblivion_katana_r")) {
                meta.setItemModel(NamespacedKey.minecraft("oblivion_katana_l"));
                i.setItemMeta(meta);
                p.sendMessage("Switched to left hand model!");
                if(p.getMainHand().equals(MainHand.RIGHT)){
                    KatanaSheathEvent sheathEvent = new KatanaSheathEvent(p);
                    Bukkit.getPluginManager().callEvent(sheathEvent);
                }
            }
        } else {
            p.sendMessage("You must have the Primal Oblivion enchantment to use this technique.");
        }

    });

    static Technique oblivionSlash = new Technique(
            "oblivion_slash",
            "Oblivion Slash",
            new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(5), List.of("Unleash a slashing particle and damage players in your FOV (7 blocks).")),
            TargetSelectors.self(),
            (ctx, token) -> {
                Player p = ctx.caster();
                if (p == null) return;

                // Damage players inside the player's field of view (<=7 blocks, within angle)
                double maxDist = 8.0;
                double maxAngleDeg = 45.0; // 90-degree cone total

                Location eye = p.getEyeLocation();
                Vector dir = eye.getDirection().clone().normalize();
                spawnSlashEffect(p);
                for (Player t : p.getWorld().getPlayers()) {
                    if (t == null || !t.isValid() || t.equals(p)) continue;
                    if (t.getLocation().distance(p.getLocation()) > maxDist) continue;

                    Vector to = t.getEyeLocation().toVector().subtract(eye.toVector());
                    if (to.lengthSquared() < 0.0001) continue;
                    to = to.normalize();
                    double dot = dir.dot(to);
                    if (Double.isNaN(dot)) continue;
                    double angle = Math.toDegrees(Math.acos(Math.max(-1.0, Math.min(1.0, dot))));
                    if (angle <= maxAngleDeg) {
                        try {
                            t.damage(300.0, p);
                            OblivionEnchant.oblivion(p, t);
                        } catch (Throwable ignored) {}
                    }
                }
            }
    );

    public static void spawnSlashEffect(Player p) {
        World world = p.getWorld();
        Vector direction = p.getLocation().getDirection().normalize();
        Location center = p.getLocation();
        for (int i = -2; i <= 2; i++) {
            Vector spread = direction.clone().rotateAroundY(i * Math.PI / 16);
            Arrow arrow = world.spawnArrow(center.clone().add(0, 1.5, 0), spread, 2.0f, 0.1f);
            arrow.setDamage(0);
        }
    }

    static Technique sheathOfOblivion = new Technique(
            "sheath_of_death",
            "Sheath of Massacre",
            new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(5), List.of("Sheath your katana to kill nearby opponents.")),
            TargetSelectors.self(),
            (ctx, token) -> {
                Player p = ctx.caster();
                BossEvents.isSheathTechOn.put(p, true);
            }
    );

    static Technique witheringWorld = new Technique(
            "withering_world",
            "Withering World",
            new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(10), List.of("Create a withering world around you that damages and withers enemies.")),
            TargetSelectors.self(),
            (ctx, token) -> {
                Player p = ctx.caster();
                Location center = p.getLocation();
                double radius = 100.0;
                new BukkitRunnable() {
                    int ticks = 0;
                    @Override
                    public void run() {
                        if (ticks++ >= 20 *40) { // Lasts for 10 seconds
                            this.cancel();
                            return;
                        }
                        for (Player t : p.getWorld().getPlayers()) {
                            if (t == null || !t.isValid() || t.equals(p)) continue;
                            if (t.getLocation().distance(center) > radius) continue;
                            try {
                                p.addPotionEffect(PotionEffectType.WITHER.createEffect(20 * 5, 1));
                            } catch (Throwable ignored) {}
                        }
                    }
                }.runTaskTimer(plugin, 0, 1); // Run every second
            }
    );

    static Technique EternalGlitch = new Technique(
            "eternal_glitch",
            "Eternal Glitch",
            new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(4), List.of("Glitch your victims.")),
            TargetSelectors.self(),
            (ctx, token) -> {
                Player p = ctx.caster();
                BossEvents.isGlitchTechOn.put(p, true);
                Bukkit.getScheduler().runTaskLater(plugin, () -> BossEvents.isGlitchTechOn.put(p, false), 20*20);
            }
    );

    static Technique forgotten_magic = new Technique(
            "forgotten_magic",
            "Forgotten Magic",
            new TechniqueMeta(true, cooldownHelper.minutesToMiliseconds(15), List.of("A forgotten magic spell that erases a user's magic.")),
            TargetSelectors.self(),
            (ctx, token) -> {
                Player p = ctx.caster();
                Player target = roaring_soul.getClosestPlayer(p.getLocation());
                if (target == null) return;
                int abysses = events.getGroupIdCount(p);
                List<String> ids = new ArrayList<>();
                for(int i = 0; i < abysses; i++) {
                    ids.add(events.getGroupId(p, i));
                }
                Random rand = new Random();
                int random = rand.nextInt(ids.size());
                List<String> ids2 = new ArrayList<>();
                for(int i = 0; i < ids.size(); i++) {
                    ids2.add(ids.get(i));
                }
                ids2.remove(random);
                plugin.getConfig().set(target.getUniqueId() + ".groupids", ids2);
                plugin.saveConfig();
                plugin.reloadConfig();
                target.sendMessage("Your magic " + ids.get(random) + " has been forgotten.");
                Bukkit.getScheduler().runTaskLater(plugin, () ->{
                    plugin.getConfig().set(target.getUniqueId() + ".groupids", ids);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    target.sendMessage("Your magic has been restored.");
                }, 20*60*5);
            }
    );

    // --- KATANA WORLD APOCALYPSE ---
    static Technique katana_world_apocalypse = new Technique(
            "chaos_domain",
            "Domain of Chaos",
            new TechniqueMeta(true, cooldownHelper.minutesToMiliseconds(15), List.of("Creates a protective bedrock sphere, limits gamemode changes, kills pearls/arrows, buffs the caster.")),
            TargetSelectors.radialPlayers(100),
            (ctx, token) -> {
                Player user = ctx.caster();
                if (user == null) return;
                int radius = 100;
                int duration = 20 * (2 * 60 + 47); // ticks
                Location center = user.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                World world = center.getWorld();

                boolean gamemode = user.hasPermission("minecraft.command.gamemode");
                boolean permaAwaken = user.hasPermission("techapi.awakening.perma");
                PermissionAttachment a = user.addAttachment(plugin);
                a.setPermission("minecraft.command.gamemode", true); // Prevent user from changing gam
                a.setPermission("techapi.awakening.perma", true);

                // Build sphere and save original block states
                Set<Block> shell = roaring_soul.sphereAround(center, radius);
                Set<BlockState> replaced = new HashSet<>();
                for (Block b : shell) {
                    try {
                        replaced.add(b.getState());
                        int rng = new Random().nextInt(100);
                        Material mat;
                        if (rng < 5) mat = Material.BEDROCK;
                        else if(rng < 55) mat = Material.OBSIDIAN;
                        else mat = Material.TINTED_GLASS;
                        b.setType(mat, false);
                    } catch (Exception ignored) {}
                }

                for(LivingEntity t : ctx.targets()){
                    if(t instanceof Player pl){
                        Client mats = ClientApi.getInstance().getClient(pl.getUniqueId());
                        if (mats == null) {
                            System.out.println("Client is not loaded yet, you may need to wait a tick after joining");
                            return;
                        }

                        if (mats.isConnected()) {

                            Media sound = new Media("files:music/extra/chaos_heart_resurrection.mp3");

                            sound.setLoop(false);
                            MediaApi.getInstance().playFor(sound, mats);

                        }
                    }
                }

                // Listener to prevent gamemode changes for others
                Listener gmListener = new Listener() {
                    @EventHandler
                    public void onGameModeChange(PlayerGameModeChangeEvent e) {
                        if (e.getPlayer().equals(user)) return;
                        if(!ctx.targets().contains(e.getPlayer())) {}
                        // Cancel the change and force survival
                        e.setCancelled(true);
                        e.getPlayer().setGameMode(GameMode.SURVIVAL);
                    }
                };
                Bukkit.getPluginManager().registerEvents(gmListener, plugin);

                // Task: remove enderpearls and tipped arrows with blindness inside sphere that are not from the user
                BukkitRunnable entityCleaner = new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Entity e : world.getEntities()) {
                            if (e.getLocation().distance(center) > radius) continue;
                            if (e instanceof EnderPearl) {
                                EnderPearl ep = (EnderPearl) e;
                                if (!(ep.getShooter() instanceof Player) || !ep.getShooter().equals(user)) {
                                    e.remove();
                                }
                            } else if (e instanceof TippedArrow) {
                                TippedArrow ta = (TippedArrow) e;
                                boolean hasBlind = false;
                                try {
                                    for (PotionEffect eff : ta.getCustomEffects()) {
                                        if (eff.getType().equals(PotionEffectType.BLINDNESS)) {
                                            hasBlind = true;
                                            break;
                                        }
                                    }
                                } catch (Exception ignored) {}
                                if (hasBlind) {
                                    if (!(ta.getShooter() instanceof Player) || !((Player) ta.getShooter()).equals(user)) {
                                        e.remove();
                                    }
                                }
                            }
                        }
                    }
                };
                entityCleaner.runTaskTimer(plugin, 0L, 2L);

                // Task: apply user buffs repeatedly while active
                BukkitRunnable buffTask = new BukkitRunnable() {
                    int ticks = 0;

                    @Override
                    public void run() {
                        if (ticks >= duration) {
                            // restore blocks
                            for (BlockState bs : replaced) {
                                try {
                                    bs.update(true, false);
                                } catch (Exception ignored) {}
                            }
                            // cleanup
                            HandlerList.unregisterAll(gmListener);
                            entityCleaner.cancel();
                            this.cancel();

                            a.setPermission("minecraft.command.gamemode", gamemode);
                            a.setPermission("techapi.awakening.perma", permaAwaken);
                            // After the time ends, damage the user by 1000
                            user.damage(1000, user);
                            CooldownManager.removeAllCooldowns(user);
                            return;
                        }

                        // Apply short-duration buffs every 2 ticks (duration 40 ticks = 2 seconds)
                        user.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 40, 0, false, false, true));
                        user.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 40, 5, false, false, true));
                        user.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2, false, false, true));
                        user.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 40, 2, false, false, true));
                        user.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0, false, false, true));
                        user.removePotionEffect(PotionEffectType.WITHER);

                        ticks += 2;
                    }
                };
                buffTask.runTaskTimer(plugin, 0L, 2L);
            }
    );
}
