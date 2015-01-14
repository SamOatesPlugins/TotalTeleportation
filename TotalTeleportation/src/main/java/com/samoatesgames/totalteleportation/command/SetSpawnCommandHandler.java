package com.samoatesgames.totalteleportation.command;

import com.samoatesgames.samoatesplugincore.commands.BasicCommandHandler;
import com.samoatesgames.samoatesplugincore.commands.PluginCommandManager;
import com.samoatesgames.totalteleportation.TotalTeleportation;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SetSpawnCommandHandler extends BasicCommandHandler {

    /**
     * Class constructor
     */
    public SetSpawnCommandHandler() {
        super("totalteleportation.command.setspawn");
    }
    
    /**
     * Handle the /setspawn command
     *
     * @param sender
     * @param args
     * @return
     */
    public boolean execute(PluginCommandManager manager, CommandSender sender, String[] args) {

        if (!manager.hasPermission(sender, this.getPermission())) {
            manager.sendMessage(sender, "You do not have permission to use /setspawn.");
            return true;
        }
        
        if (!(sender instanceof Player)) {
            manager.sendMessage(sender, "Only players can use the /setspawn command.");
            return true;
        }
        
        Player player = (Player)sender;
        
        TotalTeleportation plugin = (TotalTeleportation)manager.getPlugin();
        plugin.setSpawn(player.getLocation());
        manager.sendMessage(player, "You have set the spawn of the world to your current location.");
        
        return true;
    }
}
