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
public class HomeCommandHandler extends BasicCommandHandler {

    /**
     * Class constructor
     */
    public HomeCommandHandler() {
        super("totalteleportation.command.home");
    }
    
    /**
     * Handle the /home command
     *
     * @param sender
     * @param args
     * @return
     */
    public boolean execute(PluginCommandManager manager, CommandSender sender, String[] args) {

        if (!manager.hasPermission(sender, this.getPermission())) {
            manager.sendMessage(sender, "You do not have permission to use /home.");
            return true;
        }
        
        if (!(sender instanceof Player)) {
            manager.sendMessage(sender, "Only players can use the /home command.");
            return true;
        }
        
        TotalTeleportation plugin = (TotalTeleportation)manager.getPlugin();
        
        Player player = (Player)sender;
        String playerName = player.getName();
        if (args.length == 1 && manager.hasPermission(sender, this.getPermission() + ".other")) {
            playerName = args[0];
        }
        
        Location home = plugin.getHome(playerName);
        if (home == null) {
            manager.sendMessage(player, "No homes found for " + playerName + ".");
            return true;
        }
        
        player.teleport(home);
        manager.sendMessage(player, "You have been teleported to " + playerName + "s home.");
        
        return true;
    }
}
