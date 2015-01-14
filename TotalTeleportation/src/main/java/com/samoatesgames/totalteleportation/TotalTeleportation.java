package com.samoatesgames.totalteleportation;

import com.samoatesgames.samoatesplugincore.plugin.SamOatesPlugin;
import com.samoatesgames.totalteleportation.command.SetSpawnCommandHandler;
import com.samoatesgames.totalteleportation.command.SpawnCommandHandler;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * The main plugin class
 * @author Sam Oates <sam@samoatesgames.com>
 */
public final class TotalTeleportation extends SamOatesPlugin {
    
    /**
     * 
     */
    private Map<String, Location> m_spawnLocations = new HashMap<String, Location>();
    
    /**
     * Class constructor
     */
    public TotalTeleportation() {
        super("TotalTeleportation", "TotalTP", ChatColor.AQUA);
    }
    
    /**
     * Called when the plugin is enabled
     */
    @Override
    public void onEnable() {        
        super.onEnable();
        m_commandManager.registerCommandHandler("spawn", new SpawnCommandHandler());        
        m_commandManager.registerCommandHandler("setspawn", new SetSpawnCommandHandler());
        
        for (World world : this.getServer().getWorlds()) {
            String key = "spawn." + world.getName().toLowerCase();
            Location defaultSpawn = world.getSpawnLocation();
            Double x = this.getSetting(key + ".x", defaultSpawn.getX());
            Double y = this.getSetting(key + ".y", defaultSpawn.getY());
            Double z = this.getSetting(key + ".z", defaultSpawn.getZ());
            double yaw = this.getSetting(key + ".yaw", (double)defaultSpawn.getYaw());
            double pitch = this.getSetting(key + ".pitch", (double)defaultSpawn.getPitch());
            
            Location spawn = new Location(world, x, y, z, (float)yaw, (float)pitch);
            m_spawnLocations.put(world.getName().toLowerCase(), spawn);
        }
    }
    
    /**
     * Register all configuration settings
     */
    public void setupConfigurationSettings() {
        
        for (World world : this.getServer().getWorlds()) {
            String key = "spawn." + world.getName().toLowerCase();
            Location spawn = world.getSpawnLocation();
            this.registerSetting(key + ".x", spawn.getX());
            this.registerSetting(key + ".y", spawn.getY());
            this.registerSetting(key + ".z", spawn.getZ());
            this.registerSetting(key + ".yaw", (double)spawn.getYaw());
            this.registerSetting(key + ".pitch", (double)spawn.getPitch());
        }
        
    }
    
    /***
     * 
     * @param player
     * @return 
     */
    public Location getSpawn(Player player) {
        World world = player.getWorld();
        String worldName = world.getName().toLowerCase();
        if (!m_spawnLocations.containsKey(worldName)) {        
            return world.getSpawnLocation();
        }
        return m_spawnLocations.get(worldName);
    }
    
    /**
     * 
     * @param location 
     */
    public void setSpawn(Location location) {
        
        String worldName = location.getWorld().getName().toLowerCase();
        m_spawnLocations.put(worldName, location);
        
        String key = "spawn." + worldName;
        this.setSetting(key + ".x", location.getX());
        this.setSetting(key + ".y", location.getY());
        this.setSetting(key + ".z", location.getZ());
        this.setSetting(key + ".yaw", (double)location.getYaw());
        this.setSetting(key + ".pitch", (double)location.getPitch());
        this.saveSettings();
    }
}
