package me.kevinnovak.treasurehunt;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin implements Listener{
	private Integer minX, maxX, minY, maxY, minZ, maxZ;
	private World world;
	
    // ======================
    // Enable
    // ======================
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        
        loadConfig();
        Bukkit.getServer().getLogger().info("[TreasureHunt] Config loaded.");
        
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Enabled!");
    }
    
    // ======================
    // Disable
    // ======================
    public void onDisable() {
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Disabled!");
    }
    
    void loadConfig() {
    	this.minX = getConfig().getInt("minX");
    	this.maxX = getConfig().getInt("maxX");
    	this.minY = getConfig().getInt("minY");
    	this.maxY = getConfig().getInt("maxY");
    	this.minZ = getConfig().getInt("minZ");
    	this.maxZ = getConfig().getInt("maxZ");
    	
    	String worldString = getConfig().getString("world");
    	this.world = getServer().getWorld(worldString);
    }
    
    Location getRandomLocation() {
    	int randX = ThreadLocalRandom.current().nextInt(this.minX, this.maxX + 1);
    	int randY = ThreadLocalRandom.current().nextInt(this.minY, this.maxY + 1);
    	int randZ = ThreadLocalRandom.current().nextInt(this.minZ, this.maxZ + 1);
  
    	Location randLocation = new Location(this.world, randX, randY, randZ);
    	
    	return randLocation;
    }
}