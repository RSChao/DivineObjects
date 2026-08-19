package com.rschao.plugins.divineObjects.enchant.tech;

import com.rschao.plugins.techniqueAPI.tech.Technique;
import com.rschao.plugins.techniqueAPI.tech.cooldown.CooldownManager;
import com.rschao.plugins.techniqueAPI.tech.cooldown.cooldownHelper;
import com.rschao.plugins.techniqueAPI.tech.register.TechRegistry;
import com.rschao.plugins.techniqueAPI.tech.selectors.TargetSelectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

import static com.rschao.plugins.divineObjects.enchant.tech.PrimalKatana.plugin;

public class AegisCoresTechniques {

    public static void register(){
        TechRegistry.registerTechnique("divine_aegis_cores", logos);
    }

    public static Technique logos = new Technique("supreme:logos_core", "Logos Core", true, cooldownHelper.hour*4, List.of("Full cooldown reset"), TargetSelectors.self(), (ctx, token) ->{
        List<String> dialogue = List.of(
                "I am thou, and thou art I.",
                "Heed my call, world of Showdown",
                "May the power of the Aegis come to my aid.",
                "May the Aegis of Shadow and Stagnation grant me his power.",
                "Void of Aion! Heed my call, and Turn my clock to a better hour!",
                "In the name of " + ctx.caster().getName() + ", wielder of the Shadow Core Crystal" + ", i cast",
                ChatColor.BLACK + (ChatColor.BOLD + "Supreme Magic: Shadow Core") + ChatColor.RESET + "!"
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
            CooldownManager.removeAllCooldowns(ctx.caster());
            ctx.caster().sendMessage(ChatColor.BLACK+ "Your power has restored all cooldown");
        }, 30*(dialogue.size()+1));
    });
}
