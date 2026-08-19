package com.rschao.plugins.divineObjects.enchant.tech;

import com.delta.plugins.events.events;
import com.delta.plugins.projectiles.DeterminationProjectile;
import com.delta.plugins.techs.Familiar_love;
import com.rschao.enchants.OblivionEnchant;
import com.rschao.events.soulEvents;
import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.divineObjects.event.Events;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.cooldown.CooldownManager;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import com.rschao.plugins.techniqueAPI.tech.util.PlayerTechniqueManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

import static com.delta.plugins.techs.roaring_soul.getClosestPlayer;

public class OblivionKingTechs {
    static final Plugin plugin = Plugin.getPlugin(Plugin.class);
    static final String TECH_ID = "divine_oblivion_blade";
    public static void register(){
        TechRegistry.registerTechnique(TECH_ID, chaosHeartbeat);
        TechRegistry.registerTechnique(TECH_ID, permaOblivion);
        TechRegistry.registerTechnique(TECH_ID, geno);
        TechRegistry.registerTechnique(TECH_ID, dt);
        TechRegistry.registerTechnique(TECH_ID, godslayer);
    }



    static Technique chaosHeartbeat = new Technique(
            "oblivion_beat",
            "Oblivion Beat",
            new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(4), List.of("Spawn four pulse waves around you.")),
            TargetSelectors.self(),
            (ctx, token) -> {
                Player player = ctx.caster();
                // Programar 3 oleadas a 1s, 2s y 3s (20, 40, 60 ticks)
                Bukkit.getScheduler().runTaskLater(plugin, () -> spawnWave(player), 20L);
                Bukkit.getScheduler().runTaskLater(plugin, () -> spawnWave(player), 40L);
                Bukkit.getScheduler().runTaskLater(plugin, () -> spawnWave(player), 60L);
                Bukkit.getScheduler().runTaskLater(plugin, () -> spawnWave(player), 80L);
                hotbarMessage.sendHotbarMessage(player, "§c§lYou unleashed the beat of Oblivion!§r");
            }
    );

    private static void spawnWave(Player user){
        if (user == null || !user.isOnline()) return;

        // Aplicar daño/substracción de vida a jugadores cercanos (puede ocurrir una vez por oleada)
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isOnline()) continue;
            if (!p.getWorld().equals(user.getWorld())) continue;
            if (p.equals(user)) continue;
            if (PlayerTechniqueManager.isInmune(p.getUniqueId())) continue; // excluir inmunes
            if (p.getLocation().distance(user.getLocation()) <= 30.0) {
                int rng = new Random().nextInt(10);
                if (rng < 4) OblivionEnchant.oblivion(user, p);
                double newHealth = p.getHealth() - 20;
                if (newHealth <= 0) {
                    // aplicar daño masivo para forzar muerte si corresponde
                    p.damage(300.0, user);
                } else {
                    // setHealth puede lanzar excepción si fuera de rango; usamos try por seguridad
                    try {
                        p.setHealth(newHealth);
                        p.damage(1, user);
                    } catch (Exception ignore) { /* si falla, intentar infligir daño equivalente */
                        p.damage(20, user);
                    }
                }
            }
        }

        // Crear expansión de partículas: crecer de 0 a 30 bloques en ~10 ticks
        final int steps = 10;
        final double maxRadius = 30.0;
        final Location center = user.getLocation().clone().add(0, 1.0, 0); // elevar un poco para visibilidad
        final Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0,0,0), 1.0f);

        for (int step = 1; step <= steps; step++) {
            final int s = step;
            final double radius = (maxRadius / steps) * s;
            // programar cada paso en ticks relativos (1 tick entre pasos -> total ~10 ticks)
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!user.isOnline()) return;
                Location c = center.clone();
                // generar puntos en el perímetro; resolución depende del radio (más radio -> más puntos)
                double circumference = 2 * Math.PI * Math.max(radius, 1.0);
                double spacing = 0.5; // separación aproximada entre partículas
                int points = Math.max(8, (int)(circumference / spacing));
                for (int i = 0; i < points; i++) {
                    double angle = (2 * Math.PI) * i / points;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    Location spawnLoc = c.clone().add(x, 0, z);
                    spawnLoc.getWorld().spawnParticle(Particle.DUST, spawnLoc, 1, 0, 0, 0, 0, dust);
                }
            }, s); // s ticks después de la invocación de la oleada
        }
    }


    static Technique permaOblivion = new Technique(
            "oblivion_roll",
            "I am Oblivion",
            false, cooldownHelper.minutesToMiliseconds(5), List.of("Allows great chance of oblivion for a while"),
            TargetSelectors.self(), (ctx, token) ->{


        Events.isOblivionGenoTechOn.put(ctx.caster(), true);
        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> Events.isOblivionGenoTechOn.put(ctx.caster(), false), 20*90);

    });

    static Technique geno = new Technique(
            "oblivion_smash",
            "Oblivion Smash",
            new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(4), List.of("Teleport behind a target and apply blindness.")),
            TargetSelectors.self(),
            (ctx, token) -> {
                Player player = ctx.caster();
                // 1. Find random target within 200 blocks
                Player target = getClosestPlayer(player.getLocation());
                if(target == null) {
                    player.sendMessage("No target found within 200 blocks.");
                    return;
                }
                if(target.getLocation().distance(player.getLocation()) > 200) {
                    player.sendMessage("No target found within 200 blocks.");
                    return;
                }

                // 2. Apply blindness 255 for 5s, slowness 255 for 1s
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 254, false, false, false));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 2, 254, false, false, false));

                // 3. Teleport user 2 blocks behind target, facing them
                Location behind = target.getLocation().clone();
                behind.setDirection(target.getLocation().getDirection().multiply(-1));
                behind = behind.add(behind.getDirection().normalize().multiply(2));
                behind.setYaw(target.getLocation().getYaw() + 180);
                player.teleport(behind);
                player.teleport(player.getLocation().setDirection(target.getLocation().subtract(player.getLocation()).toVector()));

                Bukkit.getScheduler().runTaskLater(com.delta.plugins.Plugin.getPlugin(com.delta.plugins.Plugin.class), () -> CooldownManager.setCooldown(player, "geno", cooldownHelper.minutesToMiliseconds(5)), 2);
                Bukkit.getScheduler().runTaskLater(com.delta.plugins.Plugin.getPlugin(com.delta.plugins.Plugin.class), () -> Events.isOblivionGenoTechOn.put(player, true), 0);
                Bukkit.getScheduler().runTaskLater(com.delta.plugins.Plugin.getPlugin(com.delta.plugins.Plugin.class), () -> Events.isOblivionGenoTechOn.put(player, false), 20);
            }
    );

    static Technique dt = new Technique(
            "spear",
            "Oblivion Spear",
            new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(3), List.of("Launch determination projectiles.")),
            TargetSelectors.closestPlayer(),
            (ctx, token) -> {
                Player player = ctx.caster();
                LivingEntity targetE = ctx.targets().iterator().next();
                if(!(targetE instanceof Player target)) return;
                Material mat = target.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType();
                boolean air = mat.isAir();
                int projectiles = (!air) ? 20 : 10;
                if(!air) {
                    target.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20*5, 255));
                    target.addPotionEffect(PotionEffectType.SLOWNESS.createEffect(20*5, 255));
                }
                for(int i = 0; i<projectiles; i++){
                    Bukkit.getScheduler().runTaskLater(plugin, ()->{
                        DeterminationProjectile proj = new DeterminationProjectile(player.getLocation(), player);
                        proj.launch();
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if(!proj.isValid()){
                                    this.cancel();
                                    return;
                                }
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    // Excluir activador e jugadores inmunes
                                    if (p == player) continue;
                                    if (PlayerTechniqueManager.isInmune(p.getUniqueId())) continue;
                                    if(proj.getDistance(p.getLocation()) < 2){
                                        p.setNoDamageTicks(5);
                                    }
                                }
                            }
                        }.runTaskTimer(plugin, 2L, 2L);
                    }, i*2L);

                }
                hotbarMessage.sendHotbarMessage(player, "&5&lOblivion Spear Activated!");
            }
    );

    static Technique godslayer = new Technique(
            "supreme:godslayer",
            "Supreme Magic: Slayer of Gods",
            true, cooldownHelper.hour*3, List.of("Sets general cooldown + pops the targets", "Can incvrease pop amount"),
            TargetSelectors.radialPlayers(100), (ctx, token) ->{
            List<String> dialogue = List.of(
                    "I am thou, and thou art I.",
                    "Heed my call, world of nothing",
                    "May the divinity of the forgotten become one with my determination.",
                    "May the souls whose memory none retain join as one in this moment.",
                    "Forgotten World! Heed my call! Let us slay those who dare stand before us!",
                    "In the name of " + ctx.caster().getName() + ", wielder of Oblivion" + ", i cast",
                    ChatColor.BLACK + (ChatColor.BOLD + "Supreme Magic: Slayer of Gods") + ChatColor.RESET + "!"
            );

            for(int i = 0; i < dialogue.size(); i++) {
                int finalI = i;
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()){
                        if(finalI == dialogue.size() - 1){
                            p.sendTitle(dialogue.get(finalI), "", 10, 70, 20);
                        }
                        else {
                            p.sendMessage(dialogue.get(finalI));
                        }
                    }

                }, i*30L); // Delay of 30 ticks (1.5 seconds)
            }
            //get all targets and ensure they are players
            for(LivingEntity e : ctx.targets()){
                if(!(e instanceof Player p)) continue;
                //run after dialogue (obviously)
                Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () ->{
                    //cooldowns: all techs receive their full cooldown (the one set in the technique)
                    for(String id : TechRegistry.getRegisteredFruitIds()){
                        for(Technique t : TechRegistry.getAllTechniques(id)){
                            CooldownManager.setCooldown(p, t.getId(), t.getMeta().getCooldownMillis());
                        }
                        p.sendMessage(ChatColor.BLACK + "The power of " + id + " has been restricted from you");
                    }
                    //starts at 3 pops
                    int pops = 3;
                    //x2 if has soul 100 (Divinity)
                    if(soulEvents.hasSoul(p, 100)) pops*=2;
                    FileConfiguration configuration = com.delta.plugins.Plugin.getPlugin(com.delta.plugins.Plugin.class).getConfig();
                    List<String> ids = configuration.getStringList(p.getName() + ".groupids");
                    for(String id : ids){
                        for(Technique t : TechRegistry.getAllTechniques(id)){
                            if(t.getId().startsWith("supreme:")){
                                pops*=2; //x2 again if has an abyss with a technique whose id starts with "supreme:"
                                break;
                            }
                        }
                    }
                    if(pops > 12) pops = 12;
                    ctx.caster().sendMessage(ChatColor.BLACK + "Thy power will erase " + pops + " lives from the soul of " + p.getDisplayName());
                    for(int i = 0; i<pops; i++){
                        //the funny happens here
                        Bukkit.getScheduler().runTaskLater(Plugin.getPlugin(Plugin.class), () -> {
                            p.setNoDamageTicks(0);
                            p.damage(3000, ctx.caster());
                        }, 4L *i);
                    }
                }, 30*(dialogue.size()+1));
            }
    });
}
