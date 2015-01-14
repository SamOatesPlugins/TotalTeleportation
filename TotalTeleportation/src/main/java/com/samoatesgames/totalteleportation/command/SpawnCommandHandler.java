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
public class SpawnCommandHandler extends BasicCommandHandler {

    /**
     * Class constructor
     */
    public SpawnCommandHandler() {
        super("totalteleportation.command.spawn");
    }
    
    /**
     * Handle the /spawn command
     *
     * @param sender
     * @param args
     * @return
     */
    public boolean execute(PluginCommandManager manager, CommandSender sender, String[] args) {

        if (!manager.hasPermission(sender, this.getPermission())) {
            manager.sendMessage(sender, "You do not have permission to use /spawn.");
            return true;
        }
        
        if (!(sender instanceof Player)) {
            manager.sendMessage(sender, "Only players can use the /spawn command.");
            return true;
        }
        
        Player player = (Player)sender;
        
        TotalTeleportation plugin = (TotalTeleportation)manager.getPlugin();
        Location spawn = plugin.getSpawn(player);
        player.teleport(spawn);
        
        manager.sendMessage(player, "You have been teleported to the spawn of the world.");
        
        return true;
    }
}
