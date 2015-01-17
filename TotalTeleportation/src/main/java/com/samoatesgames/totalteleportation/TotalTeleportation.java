package com.samoatesgames.totalteleportation;

import com.samoatesgames.samoatesplugincore.plugin.SamOatesPlugin;
import com.samoatesgames.totalteleportation.command.HomeCommandHandler;
import com.samoatesgames.totalteleportation.command.SetHomeCommandHandler;
import com.samoatesgames.totalteleportation.command.SetSpawnCommandHandler;
import com.samoatesgames.totalteleportation.command.SpawnCommandHandler;
import com.samoatesgames.totalteleportation.data.PlayerHome;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager;

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
     * 
     */
    private Map<String, PlayerHome> m_homeLocations = new HashMap<String, PlayerHome>();
    
    /**
     * 
     */
    private BukkitDatabase m_database = null;
    
    /**
     * 
     */
    private DatabaseTable m_homesTable = null;
    
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
        m_commandManager.registerCommandHandler("home", new HomeCommandHandler());        
        m_commandManager.registerCommandHandler("sethome", new SetHomeCommandHandler());
                
        for (World world : this.getServer().getWorlds()) {
            String key = "spawn." + world.getName().toLowerCase();
            Location defaultSpawn = world.getSpawnLocation();
            
            boolean enabled = this.getSetting(key + ".enabled", false);
            if (enabled) {
                Double x = this.getSetting(key + ".x", defaultSpawn.getX());
                Double y = this.getSetting(key + ".y", defaultSpawn.getY());
                Double z = this.getSetting(key + ".z", defaultSpawn.getZ());
                double yaw = this.getSetting(key + ".yaw", (double)defaultSpawn.getYaw());
                double pitch = this.getSetting(key + ".pitch", (double)defaultSpawn.getPitch());

                Location spawn = new Location(world, x, y, z, (float)yaw, (float)pitch);
                m_spawnLocations.put(world.getName().toLowerCase(), spawn);
            }
        }
        
        m_database = bDatabaseManager.createDatabase(this.getSetting("database.database", "my_database"), this, bDatabaseManager.DatabaseType.SQL);
        if (!m_database.login(
            this.getSetting("database.host", "localhost"), 
            this.getSetting("database.username", "user"), 
            this.getSetting("database.password", "password"), 
            this.getSetting("database.port", 3306))) 
        {
            this.logError("Failed to connect to database!");
        }
        else
        {
            m_homesTable = m_database.createTable("TotalTP_Homes", PlayerHome.class);
            ResultSet loadedData = m_homesTable.selectAll();
            try {
                while (loadedData.next()) {                    
                    String worldName = loadedData.getString("WorldName");
                    String userID = loadedData.getString("Owner");
                    double x = loadedData.getDouble("X");
                    double y = loadedData.getDouble("Y");
                    double z = loadedData.getDouble("Z");
                    double yaw = loadedData.getDouble("Yaw");
                    double pitch = loadedData.getDouble("Pitch");
                    
                    World world = this.getServer().getWorld(worldName);
                    if (world != null) {
                        Location location = new Location(world, x, y, z, (float)yaw, (float)pitch);                    
                        PlayerHome home = new PlayerHome(userID, location);
                        m_homeLocations.put(userID, home);
                    }
                }
            } catch (Exception ex) {
                this.logException("Failed to load home data from database", ex);
            }
        }
    }
    
    /**
     * Register all configuration settings
     */
    public void setupConfigurationSettings() {
        
        this.registerSetting("database.host", "localhost");
        this.registerSetting("database.port", 3306);
        this.registerSetting("database.database", "my_database");
        this.registerSetting("database.username", "user");
        this.registerSetting("database.password", "password");
        
        for (World world : this.getServer().getWorlds()) {
            String key = "spawn." + world.getName().toLowerCase();
            Location spawn = world.getSpawnLocation();
            this.registerSetting(key + ".enabled", true);
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
            return null;
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
        this.setSetting(key + ".enabled", true);
        this.setSetting(key + ".x", location.getX());
        this.setSetting(key + ".y", location.getY());
        this.setSetting(key + ".z", location.getZ());
        this.setSetting(key + ".yaw", (double)location.getYaw());
        this.setSetting(key + ".pitch", (double)location.getPitch());
        this.saveSettings();
    }
    
    /**
     * 
     * @param playerName
     * @return 
     */
    public Location getHome(String playerName) {
        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        if (!m_homeLocations.containsKey(uuid.toString())) {
            return null;
        }
        return m_homeLocations.get(uuid.toString()).toLocation();        
    }
    
    /**
     * 
     * @param playerName
     * @param location 
     */
    public void setHome(String playerName, Location location) {
        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();

        if (m_homeLocations.containsKey(uuid.toString())) {
            m_homeLocations.remove(uuid.toString());
        }
        
        PlayerHome home = new PlayerHome(uuid.toString(), location);
        m_homeLocations.put(uuid.toString(), home);
        
        if (m_homesTable == null) {
            this.logError("Database is not setup. Data will not persist.");
        } else {
            m_homesTable.update(home, "`Owner`='" + uuid.toString() + "'", false);
        }
    }
}
