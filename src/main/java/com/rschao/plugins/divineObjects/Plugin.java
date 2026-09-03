package com.rschao.plugins.divineObjects;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.util.CoordAdapter;
import com.rschao.plugins.divineObjects.advs.AdvancementTabNamespaces;
import com.rschao.plugins.divineObjects.advs.divine_objects.*;
import com.rschao.plugins.divineObjects.command.GiveDivineObject;
import com.rschao.plugins.divineObjects.command.PortalAnimCommand;
import com.rschao.plugins.divineObjects.enchant.*;
import com.rschao.plugins.divineObjects.event.Events;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.EasyEnchantManager;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;

public final class Plugin extends JavaPlugin {
    public static UltimateAdvancementAPI api;

    public AdvancementTab divine_objects;


    @Override
    public void onEnable() {
        // Plugin startup logic
        GiveDivineObject.getCommand().register(this);
        GiveDivineObject.commandItem.register();
        PortalAnimCommand.cmd().register();
        initEnchants();
        initializeTabs();
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    void initEnchants(){
        Bukkit.getLogger().severe("Registering enchantments for DivineObjects");
        List<EasyEnchant> enchants = List.of(
                new DivineBlessing(),
                new DivineForgery(),
                new PrimalOblivion(),
                new BladeOfTheEnd(),
                new DivineBlessing(),
                new GodlyEmblem(),
                new OblivionKing(),
                new AegisBlade(),
                new TriforceSword(),
                new AegisBlessingEnchant()
        );
        for(EasyEnchant enchant : enchants){
            EasyEnchantManager.addEasyEnchant(enchant);
        }
    }



    public void initializeTabs() {
        api = UltimateAdvancementAPI.getInstance(this);


        divine_objects = api.createAdvancementTab(AdvancementTabNamespaces.divine_objects_NAMESPACE);
        HashSet<BaseAdvancement> divine_objectsSet = new HashSet<>();



        CoordAdapter adapterdivine_objects = CoordAdapter.builder().add(Divine_emblem.KEY, 0f, 0f).add(Primal_katana.KEY, 1f, 0f).add(Creator_emblem.KEY, 1f, -1f).add(Master_sword.KEY, 1f, 1f).add(Dimentio_mask.KEY, -1f, 0f).add(Aegis_blade.KEY, -2f, 0f).build();

        Divine_emblem divine_emblem = new Divine_emblem(divine_objects, adapterdivine_objects.getX(Divine_emblem.KEY), adapterdivine_objects.getY(Divine_emblem.KEY));
        Primal_katana primal_katana = new Primal_katana(divine_emblem, adapterdivine_objects.getX(Primal_katana.KEY), adapterdivine_objects.getY(Primal_katana.KEY));
        Creator_emblem creator_emblem = new Creator_emblem(divine_emblem, adapterdivine_objects.getX(Creator_emblem.KEY), adapterdivine_objects.getY(Creator_emblem.KEY));
        Master_sword master_sword = new Master_sword(divine_emblem, adapterdivine_objects.getX(Master_sword.KEY), adapterdivine_objects.getY(Master_sword.KEY));
        Dimentio_mask dimentio_mask = new Dimentio_mask(divine_emblem, adapterdivine_objects.getX(Dimentio_mask.KEY), adapterdivine_objects.getY(Dimentio_mask.KEY));
        Aegis_blade aegis_blade = new Aegis_blade(dimentio_mask, adapterdivine_objects.getX(Aegis_blade.KEY), adapterdivine_objects.getY(Aegis_blade.KEY));

        divine_objectsSet.add(primal_katana);
        divine_objectsSet.add(creator_emblem);
        divine_objectsSet.add(master_sword);
        divine_objectsSet.add(dimentio_mask);
        divine_objectsSet.add(aegis_blade);

        divine_objects.registerAdvancements(divine_emblem , divine_objectsSet);
        divine_objects.automaticallyShowToPlayers();

    }
}
