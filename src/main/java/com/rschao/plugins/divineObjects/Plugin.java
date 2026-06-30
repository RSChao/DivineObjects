package com.rschao.plugins.divineObjects;

import com.rschao.plugins.divineObjects.command.GiveDivineObject;
import com.rschao.plugins.divineObjects.enchant.DivineBlessing;
import com.rschao.plugins.divineObjects.enchant.DivineForgery;
import com.rschao.plugins.divineObjects.enchant.GodlyEmblem;
import com.rschao.plugins.divineObjects.enchant.PrimalOblivion;
import com.rschao.plugins.divineObjects.event.Events;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        GiveDivineObject.getCommand().register(this);
        initEnchants();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    void initEnchants(){
        List<EasyEnchant> enchants = List.of(
                new DivineBlessing(),
                new DivineForgery(),
                new PrimalOblivion(),
                new DivineBlessing(),
                new GodlyEmblem()
        );
        for(EasyEnchant enchant : enchants){
            Bukkit.getPluginManager().registerEvents(enchant, this);
        }
    }
}
