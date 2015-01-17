/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.samoatesgames.totalteleportation.data;

import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Sam
 */
public class PlayerHome {
    
    public String Owner = null;
    public String Name = "default";    
    public double X = 0.0;
    public double Y = 0.0;
    public double Z = 0.0;
    public double Yaw = 0.0f;
    public double Pitch = 0.0f;
    public String WorldName = null;
    
    private World m_world = null;
    
    public PlayerHome(String uuid, Location location) {
        this.X = location.getX();
        this.Y = location.getY();
        this.Z = location.getZ();
        this.Yaw = location.getYaw();
        this.Pitch = location.getPitch();
        this.WorldName = location.getWorld().getName();
        this.Owner = uuid;
        m_world = location.getWorld();
    }
    
    public Location toLocation() {
        return new Location(m_world, this.X, this.Y, this.Z, (float)this.Yaw, (float)this.Pitch);        
    }
    
}
