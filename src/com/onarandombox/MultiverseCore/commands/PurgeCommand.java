package com.onarandombox.MultiverseCore.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.utils.PurgeWorlds;

public class PurgeCommand extends MultiverseCommand {

    public PurgeCommand(MultiverseCore plugin) {
        super(plugin);
        this.commandName = "Purge the world ";
        this.commandDesc = "Removed the specified type of mob from the specified world.";
        this.commandUsage = "/mvpurge" + ChatColor.GOLD + " [WORLD|all] " + ChatColor.GREEN + "{all|animals|monsters|MOBNAME}";
        this.minimumArgLength = 1;
        this.maximumArgLength = 2;
        this.commandKeys.add("mvpurge");
        this.commandKeys.add("mv purge");
        this.permission = "multiverse.world.purge";
        this.opRequired = true;
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (args.size() == 1 && p == null) {
            sender.sendMessage("This command requires a WORLD when being run from the console!");
            sender.sendMessage(this.commandUsage);
            return;
        }
        String worldName = null;
        String deathName = null;
        if (args.size() == 1) {
            worldName = p.getWorld().getName();
            deathName = args.get(0);
        } else {
            worldName = args.get(0);
            deathName = args.get(1);
        }

        if (!worldName.equalsIgnoreCase("all") && !this.plugin.isMVWorld(worldName)) {
            ((MultiverseCore)this.plugin).showNotMVWorldMessage(sender, worldName);
            sender.sendMessage("It cannot be purged.");
            return;
        }

        List<MVWorld> worldsToRemoveEntitiesFrom = new ArrayList<MVWorld>();
        // Handle all case any user who names a world "all" should know better...
        if (worldName.equalsIgnoreCase("all")) {
            worldsToRemoveEntitiesFrom.addAll(this.plugin.getMVWorlds());
        } else {
            worldsToRemoveEntitiesFrom.add(this.plugin.getMVWorld(worldName));
        }

        PurgeWorlds purger = this.plugin.getWorldPurger();
        ArrayList<String> thingsToKill = new ArrayList<String>();
        if (deathName.equalsIgnoreCase("all") || deathName.equalsIgnoreCase("animals") || deathName.equalsIgnoreCase("monsters")) {
            thingsToKill.add(deathName.toUpperCase());
        } else {
            Collections.addAll(thingsToKill, deathName.toUpperCase().split(","));
        }
        for (MVWorld w : worldsToRemoveEntitiesFrom) {
            purger.purgeWorld(sender, w, thingsToKill, false, false);
        }

        return;
    }

}
