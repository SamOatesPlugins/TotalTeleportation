package com.samoatesgames.totalteleportation.command;

import com.samoatesgames.samoatesplugincore.commands.BasicCommandHandler;
import com.samoatesgames.samoatesplugincore.commands.PluginCommandManager;
import com.samoatesgames.totalteleportation.TotalTeleportation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SetHomeCommandHandler extends BasicCommandHandler {

    /**
     * Class constructor
     */
    public SetHomeCommandHandler() {
        super("totalteleportation.command.sethome");
    }
    
    /**
     * Handle the /sethome command
     *
     * @param sender
     * @param args
     * @return
     */
    public boolean execute(PluginCommandManager manager, CommandSender sender, String[] args) {

        if (!manager.hasPermission(sender, this.getPermission())) {
            manager.sendMessage(sender, "You do not have permission to use /sethome.");
            return true;
        }
        
        if (!(sender instanceof Player)) {
            manager.sendMessage(sender, "Only players can use the /sethome command.");
            return true;
        }
        
        Player player = (Player)sender;        
        TotalTeleportation plugin = (TotalTeleportation)manager.getPlugin();

        String playerName = player.getName();
        if (args.length == 1 && manager.hasPermission(sender, this.getPermission() + ".other")) {
            playerName = args[0];
        }
        
        plugin.setHome(playerName, player.getLocation());
        manager.sendMessage(player, "You have set the home location for " + playerName + ".");
        
        return true;
    }
}
