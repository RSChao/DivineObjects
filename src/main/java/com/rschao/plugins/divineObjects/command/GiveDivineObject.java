package com.rschao.plugins.divineObjects.command;

import com.rschao.plugins.divineObjects.item.DivineItems;
import com.rschao.plugins.techniqueAPI.tech.feedback.hotbarMessage;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;

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
                    case "emblem":
                        player.getInventory().addItem(DivineItems.divineEmblem());
                        hotbarMessage.sendHotbarMessage(player, "You have been given the Divine Emblem!");
                        break;
                    // Add more cases for other divine objects
                    default:
                        hotbarMessage.sendHotbarMessage(player, "Unknown divine object: " + objectName);
                        break;
                }

            });

    public static CommandAPICommand getCommand() {
        return command;
    }
}
