package com.rschao.plugins.divineObjects.enchant;

import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.CustomEnchantment;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.definition.EasyEnchant;
import com.rschao.plugins.showdowncore.showdownCore.api.enchantment.util.ColorCodes;

public class DivineBlessing extends EasyEnchant {
    public DivineBlessing(){
        super("divine_blessing", "showdowncore", ColorCodes.YELLOW.getCode() + ColorCodes.BOLD.getCode() + "Divine Blessing");
        CustomEnchantment enchantment = getCustomEnchantment();
        enchantment.setMaxLevel(3);
        this.saveBukkitEnchantment(enchantment);
    }
}
