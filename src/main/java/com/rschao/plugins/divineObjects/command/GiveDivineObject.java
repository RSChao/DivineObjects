package com.rschao.plugins.divineObjects.command;

import com.rschao.plugins.divineObjects.item.DivineItems;
import com.rschao.plugins.divineObjects.item.TempleTeleportItem;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

public class GiveDivineObject {
    static CommandAPICommand command = new CommandAPICommand("givedivineobject")
            .withPermission("divineobjects.give")
            .withArguments(new StringArgument("object"))
            .executesPlayer((player, args) -> {
                String objectName = (String) args.get(0);
                // Here you would add logic to give the player the specified divine object based on objectName
                // For example:
                switch (objectName.toLowerCase()) {
                    case "katana":
                        player.getInventory().addItem(DivineItems.primalKatana(player));
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Primal Oblivion Katana!");
                        break;
                    case "katana_buff":
                        player.getInventory().addItem(DivineItems.primalKatanaAwakened(player));
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Primal Oblivion Katana!");
                        break;
                    case "emblem":
                        player.getInventory().addItem(DivineItems.divineEmblem());
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Divine Emblem!");
                        break;
                    case "master_sword":
                        player.getInventory().addItem(DivineItems.MasterSword());
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Master Sword!");
                        break;
                    case "aegis_sword":
                        player.getInventory().addItem(DivineItems.AegisSword());
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Aegis's Blade!");
                        break;
                    case "origin":
                        player.getInventory().addItem(DivineItems.oblivionSword());
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Oblivion King's Blade!");
                        break;
                    case "cores":
                        player.getInventory().addItem(DivineItems.AishiaAegisCore());
                        player.getInventory().addItem(DivineItems.LogosAegisCore());
                        player.getInventory().addItem(DivineItems.RedAegisCore());
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Aegis Cores!");
                        break;
                    // Add more cases for other divine objects
                    default:
                        hotbarMessage.sendHotbarMessage(player, "Unknown divine object: " + objectName);
                        break;
                }

            });


    public static CommandAPICommand commandItem = new CommandAPICommand("givedivinetp")
            .withPermission("divineobjects.give")
            .withArguments(new StringArgument("object"), new LocationArgument("templeLoc"))
            .executesPlayer((player, args) -> {
                String objectName = (String) args.get(0);
                Location loc = (Location) args.get(1);

                Item i = player.getWorld().dropItemNaturally(player.getLocation(), TempleTeleportItem.createTempleTeleporter(loc, objectName));
                i.setVelocity(new Vector(0, 3, 0));
                i.setPickupDelay(0);

            });

    public static CommandAPICommand getCommand() {
        return command;
    }
}
