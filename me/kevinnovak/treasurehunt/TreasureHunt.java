package me.kevinnovak.treasurehunt;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin implements Listener{
	private World world;
	private int minX, maxX, minY, maxY, minZ, maxZ;
	private int maxAttempts;

	
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
    	
    	this.maxAttempts = getConfig().getInt("maxAttempts");
    }
    
    void startHunt() {
    	Location treasureLocation = getTreasureLocation();
    	if (treasureLocation.getBlockX() == -1 && treasureLocation.getBlockY() == -1 && treasureLocation.getBlockZ() == -1) {
    		// TO-DO: Annouce to only admins
    		Bukkit.getServer().getLogger().warning("[TreasureHunt] Failed to spawn a treasure chest after max attempts.");
    	} else {
    		// TO-DO: get random loot
    		TreasureChest treasureChest = new TreasureChest(treasureLocation);
    		treasureChest.spawn();
    		Bukkit.getServer().getLogger().warning("[TreasureHunt] Chest spawned at " + treasureLocation.getBlockX() + ", " + treasureLocation.getBlockY() + ", " + treasureLocation.getBlockZ());
    	}
    }
    
    Location getTreasureLocation() {
    	Location randLocation;
    	
    	int attempt = 1;
    	while (attempt <= maxAttempts) {
    		randLocation = getRandomLocation();

    		while (attempt <= maxAttempts && randLocation.getBlockY() >= minY) {
        		int randX = randLocation.getBlockX();
        		int randY = randLocation.getBlockY();
        		int randZ = randLocation.getBlockZ();
        		
        		Location blockAbove = new Location(world, randX, randY+1, randZ);
        		Location blockBelow = new Location(world, randX, randY-1, randZ);
    			
    			if (randLocation.getBlock().getType() == Material.AIR) {
    				if (blockAbove.getBlock().getType() == Material.AIR) {
    					// TO-DO: Or other forbidden blocks
    					if (blockBelow.getBlock().getType() != Material.AIR) {
    						return randLocation;
    					}
    				}
    			}
    			randLocation.subtract(0, 1, 0);
    			attempt++;
    		}
    	}
    	return new Location(world, -1, -1, -1);
    }
    
    Location getRandomLocation() {
    	int randX = ThreadLocalRandom.current().nextInt(this.minX, this.maxX + 1);
    	int randY = ThreadLocalRandom.current().nextInt(this.minY, this.maxY + 1);
    	int randZ = ThreadLocalRandom.current().nextInt(this.minZ, this.maxZ + 1);
  
    	Location randLocation = new Location(this.world, randX, randY, randZ);
    	
    	return randLocation;
    }
    
    // ======================
    // Commands
    // ======================
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // ======================
        // Console
        // ======================
        // if command sender is the console, let them know, cancel command
        if (!(sender instanceof Player)) {
            // TO-DO: send message to console
            return true;
        }
        
        // otherwise the command sender is a player
        //final Player player = (Player) sender;
        //final String playername = player.getName();
        
        // ======================
        // /mt
        // ======================
        if(cmd.getName().equalsIgnoreCase("th")) {
        	startHunt();
            return true;
        }
        
		return false;
    }
}