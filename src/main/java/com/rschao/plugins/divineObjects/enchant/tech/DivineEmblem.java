package com.rschao.plugins.divineObjects.enchant.tech;

import com.delta.plugins.Plugin;
import com.rschao.events.soulEvents;
import com.rschao.items.Items;
import com.rschao.items.weapons;
import com.rschao.plugins.divineObjects.enchant.GodlyEmblem;
import com.rschao.plugins.divineObjects.item.DivineItems;
import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.TechniqueMeta;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class DivineEmblem {


    static final String TECH_ID = "divine_emblem";
    static final Plugin plugin = Plugin.getPlugin(Plugin.class);
    public static void register() {
        TechRegistry.registerTechnique(TECH_ID, soulChange);
        TechRegistry.registerTechnique(TECH_ID, fake_regen_kills);
        TechRegistry.registerTechnique(TECH_ID, regenerateEmblem_DT);
    }

    public static Technique soulChange = new Technique("soul_change", "Power Exchange", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(3), List.of("Allows the user to exchange between purity heart and chaos heart.", "Works as long as they have at least one of them in their inventory and the other equipped.")), TargetSelectors.self(), (techniqueContext, cancellationToken) ->{
        ItemStack purityHeart = Items.PurityHeart;
        ItemStack chaosHeart = Items.ChaosHeart;
        boolean hasPurity = techniqueContext.caster().getInventory().containsAtLeast(purityHeart, 1);
        boolean hasChaos = techniqueContext.caster().getInventory().containsAtLeast(chaosHeart, 1);

        boolean hasChaosEquipped = soulEvents.hasSoul(techniqueContext.caster(), 66);
        boolean hasPurityEquipped = soulEvents.hasSoul(techniqueContext.caster(), 30);

        if(!hasPurity && !hasChaos) {
            hotbarMessage.sendHotbarMessage(techniqueContext.caster(), "You need to have either the Purity Heart or the Chaos Heart in your inventory to use this technique.");
            return;
        }
        if(!hasChaosEquipped && !hasPurityEquipped) {
            hotbarMessage.sendHotbarMessage(techniqueContext.caster(), "You need to have either the Purity Heart or the Chaos Heart equipped to use this technique.");
            return;
        }

        if(hasChaosEquipped && hasPurity) {
            int soulPos = getSoulPosition(66, techniqueContext.caster());
            if(soulPos == 0) soulEvents.setSouls(techniqueContext.caster(), 30, soulEvents.GetSecondSoulN(techniqueContext.caster()));
            else soulEvents.setSouls(techniqueContext.caster(), soulEvents.GetSoulN(techniqueContext.caster()), 30);
            techniqueContext.caster().getInventory().removeItem(purityHeart);
            techniqueContext.caster().getInventory().addItem(chaosHeart);
            hotbarMessage.sendHotbarMessage(techniqueContext.caster(), "You have exchanged your Chaos Heart for a Purity Heart.");
        } else if(hasPurityEquipped && hasChaos) {
            int soulPos = getSoulPosition(30, techniqueContext.caster());
            if(soulPos == 0) soulEvents.setSouls(techniqueContext.caster(), 66, soulEvents.GetSecondSoulN(techniqueContext.caster()));
            else soulEvents.setSouls(techniqueContext.caster(), soulEvents.GetSoulN(techniqueContext.caster()), 66);
            techniqueContext.caster().getInventory().removeItem(chaosHeart);
            techniqueContext.caster().getInventory().addItem(purityHeart);
            hotbarMessage.sendHotbarMessage(techniqueContext.caster(), "You have exchanged your Purity Heart for a Chaos Heart.");
        } else {
            hotbarMessage.sendHotbarMessage(techniqueContext.caster(), "You need to have the opposite heart in your inventory to exchange it.");
        }

    });


    static int getSoulPosition(int soulId, Player player) {
        int soul1 = soulEvents.GetSoulN(player);
        int soul2 = soulEvents.GetSecondSoulN(player);
        if(soul1 == soulId) return 0;
        if(soul2 == soulId) return 1;
        return -1;
    }

    public static Technique regenerateEmblem_kills = new Technique("regenerate_emblem_kills", "Regenerate Emblem", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(1), List.of("Regenerates the emblem.", "Triggered on kill if the killer has the emblem")), TargetSelectors.self(), (techniqueContext, cancellationToken) ->{
        ItemStack emblem = DivineItems.divineEmblem();
        Player p = techniqueContext.caster();
        ItemStack offHand = p.getInventory().getItemInOffHand();
        if(offHand.hasItemMeta() && offHand.getItemMeta().hasEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment())) {
            // Do nothing, item has the enchantment
        } else {
            offHand = p.getInventory().getItemInMainHand();
            if(offHand.hasItemMeta() && offHand.getItemMeta().hasEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment())) {
                // Do nothing, item has the enchantment
            } else {
                hotbarMessage.sendHotbarMessage(p, "You need to have the emblem equipped to use this technique.");
                return;
            }
        }
        if(offHand.getItemMeta() == null) return;
        int uses = offHand.getItemMeta().getPersistentDataContainer().getOrDefault(weapons.CHKey, PersistentDataType.INTEGER, 0);
        if(uses <= 0) return;
        uses -= 1;
        List<String> lore = emblem.getItemMeta().getLore();
        if(uses > 0){
            lore = new ArrayList<>();
            lore.add("Times used:");
            lore.add(String.valueOf(uses));
        }
        ItemMeta meta = offHand.getItemMeta();
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(weapons.CHKey, PersistentDataType.INTEGER, uses);
        offHand.setItemMeta(meta);
        hotbarMessage.sendHotbarMessage(p, "Your emblem has obtained the power of the fallen soul. Uses left: " + (30-uses));
    });

    public static Technique regenerateEmblem_DT = new Technique("regenerate_emblem_dt", "Regenerate Emblem (using DT)", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(5), List.of("Regenerates the emblem.", "Consumes Determination")), TargetSelectors.self(), (techniqueContext, cancellationToken) ->{
        ItemStack emblem = DivineItems.divineEmblem();
        Player p = techniqueContext.caster();
        ItemStack offHand = p.getInventory().getItemInOffHand();
        if(offHand.hasItemMeta() && offHand.getItemMeta().hasEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment())) {
            // Do nothing, item has the enchantment
        } else {
            offHand = p.getInventory().getItemInMainHand();
            if(offHand.hasItemMeta() && offHand.getItemMeta().hasEnchant((new GodlyEmblem()).getCustomEnchantment().toBukkitEnchantment())) {
                // Do nothing, item has the enchantment
            } else {
                hotbarMessage.sendHotbarMessage(p, "You need to have the emblem equipped to use this technique.");
                return;
            }
        }
        p.sendMessage("Item detected: " + offHand.getItemMeta().getDisplayName());
        if(offHand.getItemMeta() == null) return;
        int uses = offHand.getItemMeta().getPersistentDataContainer().getOrDefault(weapons.CHKey, PersistentDataType.INTEGER, 0);
        if(uses <= 0) return;
        p.sendMessage("Current uses: " + uses);
        int regen = 0;
        ItemStack dt = Items.DeterminationEssence;
        //get how many determination essences the player has
        int dtCount = 0;
        for(ItemStack i : p.getInventory().getContents()){
            if(i != null && i.isSimilar(dt)){
                dtCount += i.getAmount();
            }
        }
        p.sendMessage("Determination Essence count: " + dtCount);
        if(dtCount <= 0) {
            hotbarMessage.sendHotbarMessage(p, "You need to have at least 16 Determination Essence in your inventory to use this technique.");
            return;
        }
        regen = (int) Math.floor(dtCount/16);
        int startingUses = uses;
        uses = Math.max(0, uses - regen);
        p.sendMessage("Regenerating " + regen + " uses. New uses: " + uses);

        //get how many uses were actually regenerated
        int actualRegen = startingUses - uses;
        int toRemove = actualRegen*16;
        for(ItemStack i : p.getInventory().getContents()){
            if(i != null && i.isSimilar(dt)){
                if(i.getAmount() > toRemove){
                    i.setAmount(i.getAmount() - toRemove);
                    break;
                } else {
                    toRemove -= i.getAmount();
                    i.setAmount(0);
                }
            }
        }
        List<String> lore = emblem.getItemMeta().getLore();
        if(uses > 0){
            lore = new ArrayList<>();
            lore.add("Times used:");
            lore.add(String.valueOf(uses));
        }
        ItemMeta meta = offHand.getItemMeta();
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(weapons.CHKey, PersistentDataType.INTEGER, uses);
        offHand.setItemMeta(meta);
        hotbarMessage.sendHotbarMessage(p, "You have regenerated the emblem. Uses left: " + (30-uses));
    });
    //same data as regenerateEmblem_kills but with empty action
    public static Technique fake_regen_kills = new Technique("fake_regen_kills", "Regenerate Emblem (Soul Absorption)", new TechniqueMeta(false, cooldownHelper.minutesToMiliseconds(1), List.of("Regenerates the emblem.", "Triggered on kill if the killer has the emblem")), TargetSelectors.self(), (techniqueContext, cancellationToken) ->{
        hotbarMessage.sendHotbarMessage(techniqueContext.caster(), "This technique is passive. It'll work on its own.");
    });
}
