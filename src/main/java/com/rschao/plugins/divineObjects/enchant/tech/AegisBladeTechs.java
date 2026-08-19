package com.rschao.plugins.divineObjects.enchant.tech;

import com.rschao.enchants.OblivionEnchant;
import com.rschao.events.soulEvents;
import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.divineObjects.event.Events;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.cooldown.CooldownManager;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.rschao.plugins.divineObjects.enchant.tech.PrimalKatana.plugin;

public class AegisBladeTechs {
    static final String ID = "divine_aegis_blade";
    static final Plugin plugin = Plugin.getPlugin(Plugin.class);

    public static void register(){
        TechRegistry.registerTechnique(ID, fuckEinstein);
        TechRegistry.registerTechnique(ID, aegisBuff);
        TechRegistry.registerTechnique(ID, thisMFerFooledMe);
        TechRegistry.registerTechnique(ID, chronos_flow);
        TechRegistry.registerTechnique(ID, world_of_memories);
    }

    static Technique fuckEinstein = new Technique("physical_rewrite", "Physyical Rewrite",
            false, cooldownHelper.minutesToMiliseconds(2), List.of("Rewrites physical laws", "Grants movement and attack speed"),
            TargetSelectors.self(), (ctx, cancellationToken) -> {

        Player player = ctx.caster();
        Bukkit.getScheduler().runTaskLater(com.delta.plugins.Plugin.getPlugin(com.delta.plugins.Plugin.class), () -> {
            player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(999);
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(90*20, 2));
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4);
                player.sendMessage(ChatColor.GREEN + "The laws of physics have been restored!");
            }, 90 * 20);
        }, 2);
        player.sendMessage(ChatColor.GREEN + "The laws of physics have been rewritten!");

    });
    static Technique aegisBuff = new Technique("aegis_buff", "Aegis Power-up!",
            false, cooldownHelper.minutesToMiliseconds(3), List.of("Strengthens your body and maximizes your strength"),
            TargetSelectors.self(), (ctx, token) ->{
        Player player = ctx.caster();
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(90*20, 2));
        player.addPotionEffect(PotionEffectType.STRENGTH.createEffect(90*20, 4));
        player.addPotionEffect(PotionEffectType.RESISTANCE.createEffect(90*20, 2));
        player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(90*20, 2));

    });
    static Technique thisMFerFooledMe = new Technique("supreme_pause", "Aegis's Supreme Pause",
            false, cooldownHelper.minutesToMiliseconds(8), List.of("Blocks all supreme magic from your closest opponent", "There are spells that bypass this"),
            TargetSelectors.closestPlayer(100), (ctx, cancellationToken) -> {
        for(LivingEntity e : ctx.targets()){
            if(!(e instanceof Player target)) continue;
            for(String id : TechRegistry.getRegisteredFruitIds()){
                if(id.contains("divine_aegis")) continue;
                for (Technique t : TechRegistry.getAllTechniques(id)){
                    if(!id.startsWith("supreme:")) continue;
                    CooldownManager.setCooldown(target, t.getId(), cooldownHelper.minutesToMiliseconds(5));
                }
            }
            target.sendMessage(ChatColor.GREEN + "Your supreme powers have been temporarily restrained");
            ctx.caster().sendMessage(ChatColor.GREEN + "Your opponent " + target.getName() +"' supreme powers have been temporarily restrained");
        }
    });
    static Technique chronos_flow = new Technique("supreme:chronos_flow", "Supreme Magic: Chronos Flow",
            true, cooldownHelper.hour, List.of("Restores that which has been erased from you by oblivion"),
            TargetSelectors.self(), (ctx, token) ->{
        List<String> dialogue = List.of(
                "I am thou, and thou art I.",
                "Heed my call, world of Showdown.",
                "May the Seal of Oblivion come to an end.",
                "May the lost memories flow back into this soul.",
                "Aegis of Memory! Heed my call, and return what was stolen!",
                "In the name of " + ctx.caster().getName() + ", wielder of the Blade of the Aegis" + ", i cast",
                ChatColor.WHITE + (ChatColor.BOLD + "Supreme Magic: Chronos Flow") + ChatColor.RESET + "!"
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

        Bukkit.getScheduler().runTaskLater(plugin, () ->{
            Player p = ctx.caster();
            List<ItemStack> items = Events.forgottenItems.getOrDefault(p, new ArrayList<>());
            int emptySlots = 0;
            for(ItemStack i : p.getInventory().getContents()){
                if(i == null){
                    emptySlots++;
                    break;
                }
                if(i.getType().isAir()){
                    emptySlots++;
                    break;
                }
                if(!i.hasItemMeta()){
                    emptySlots++;
                    break;
                }
            }
            if(emptySlots <= items.size()){
                ItemStack sb = new ItemStack(Material.SHULKER_BOX);
                BlockStateMeta meta = (BlockStateMeta) sb.getItemMeta();
                if (meta != null) {
                    org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) meta.getBlockState();
                    Inventory boxInv = box.getInventory();

                    for (ItemStack item : items) {
                        if (item != null && !item.getType().equals(Material.AIR)) {
                            boxInv.addItem(item.clone());
                        }
                    }

                    meta.setBlockState(box);
                    sb.setItemMeta(meta);
                }
                Item i = p.getWorld().dropItemNaturally(p.getLocation(), sb);
                i.setPickupDelay(0);
                i.setVelocity(new Vector(0, 0, 0));
            }
            else {
                p.getInventory().addItem(items.toArray(new ItemStack[0]));
            }
            Events.forgottenItems.put(p, new ArrayList<>());
            p.sendMessage(ChatColor.GREEN+ "The items from the Seal of Oblivion have returned");
        }, 30*(dialogue.size()+1));
    });
    static Technique world_of_memories = new Technique("supreme:world_of_memories", "Supreme Magic: World of Memories",
            true, cooldownHelper.hour*3, List.of("Grants you every forgotten memory", "Can deal massive damage after that"),
            TargetSelectors.radialPlayers(100), (ctx, token) ->{
        List<String> dialogue = List.of(
                "I am thou, and thou art I.",
                "Heed my call, Spirit of Showdown",
                "May the divinity of the Aegis become one with my will.",
                "May the gates of memory, forever closed, open this present day.",
                "World of Showdown! Heed my call! Grant me the power of a myriad memories",
                "In the name of " + ctx.caster().getName() + ", wielder of the Blade of the Aegis" + ", i cast",
                ChatColor.GREEN + (ChatColor.BOLD + "Supreme Magic: World of Memories") + ChatColor.RESET + "!"
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


        Bukkit.getScheduler().runTaskLater(plugin, () ->{
            Player p = ctx.caster();
            List<ItemStack> items = Events.forgottenItems.getOrDefault(p, new ArrayList<>());
            for(OfflinePlayer op : Bukkit.getOfflinePlayers()){
                List<ItemStack> moreItems = Events.forgottenItems.getOrDefault(op.getPlayer(), new ArrayList<>());
                if(moreItems.isEmpty()) continue;
                items.addAll(moreItems);
                Events.forgottenItems.put(op.getPlayer(), new ArrayList<>());
            }
            int emptySlots = 0;
            for(ItemStack i : p.getInventory().getContents()){
                if(i == null){
                    emptySlots++;
                    break;
                }
                if(i.getType().isAir()){
                    emptySlots++;
                    break;
                }
                if(!i.hasItemMeta()){
                    emptySlots++;
                    break;
                }
            }
            if(emptySlots <= items.size()){
                ItemStack sb = new ItemStack(Material.SHULKER_BOX);
                BlockStateMeta meta = (BlockStateMeta) sb.getItemMeta();
                if (meta != null) {
                    org.bukkit.block.ShulkerBox box = (org.bukkit.block.ShulkerBox) meta.getBlockState();
                    Inventory boxInv = box.getInventory();

                    for (ItemStack item : items) {
                        if (item != null && !item.getType().equals(Material.AIR)) {
                            boxInv.addItem(item.clone());
                        }
                    }

                    meta.setBlockState(box);
                    sb.setItemMeta(meta);
                }
                Item i = p.getWorld().dropItemNaturally(p.getLocation(), sb);
                i.setPickupDelay(0);
                i.setVelocity(new Vector(0, 0, 0));
            }
            else {
                p.getInventory().addItem(items.toArray(new ItemStack[0]));
            }
            Events.forgottenItems.put(p, new ArrayList<>());
            p.sendMessage(ChatColor.GREEN+ "The items from the Seal of Oblivion have returned");
        }, 30*(dialogue.size()+1));
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
                if(soulEvents.hasSoul(p, 100) || soulEvents.hasSoul(p, 19)) pops*=2;
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
                if(pops > 8) pops = 8;
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
