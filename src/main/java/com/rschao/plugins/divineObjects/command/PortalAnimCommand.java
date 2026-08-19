package com.rschao.plugins.divineObjects.command;

import com.rschao.plugins.divineObjects.Plugin;
import com.rschao.plugins.divineObjects.animation.PortalAnim;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class PortalAnimCommand {

    public static CommandAPICommand cmd() {
        return new CommandAPICommand("portalanimation")
                .withArguments(new StringArgument("mode"), new LocationArgument("loc"), new IntegerArgument("radius").setOptional(true), new IntegerArgument("duration").setOptional(true))
                .executesPlayer((player, args) ->{
                    String mode = (String) args.get(0);
                    Location loc = (Location) args.getOrDefault("loc", player.getEyeLocation().clone().add(0, 0.5, 0));
                    int radius = (int) args.getOrDefault("radius", 2);
                    int duration = (int) args.getOrDefault("duration", 3);
                    loc = loc.clone();
                    Color c1;
                    Color c2;
                    if(mode.equalsIgnoreCase("void")){
                        c1 = Color.BLACK;
                        c2 = Color.PURPLE;
                    }
                    else{

                        c1 = Color.AQUA;
                        c2 = Color.PURPLE;
                    }
                    Location finalLoc = loc;
                    new BukkitRunnable(){
                        int ticks = 0;
                        @Override
                        public void run() {
                            PortalAnim.anim(player.getEyeLocation().getDirection(), finalLoc, radius, new Particle.DustOptions(c1, 1), new Particle.DustOptions(c2, 0.6f));
                            ticks++;
                            if(ticks >=20*duration) this.cancel();
                        }
                    }.runTaskTimer(Plugin.getPlugin(Plugin.class), 0, 1);
                });
    }
}
