package me.kevinnovak.treasurehunt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class TreasureHunt extends JavaPlugin implements Listener{
	// Files
    File chestsFile = new File(getDataFolder() + "/chests.yml");
    FileConfiguration chestsData = YamlConfiguration.loadConfiguration(chestsFile);
	
	// Config
	private World world;
	private int minX, maxX, minY, maxY, minZ, maxZ;
	private int spawnInterval, chestDuration, openedChestDuration, maxSpawnAttempts;
	private int minPlayersOnline, maxChests;
	
	// Plugin
	private List <TreasureChest> chests = new ArrayList<TreasureChest>();
	private int spawnTimer;
	
    // ======================
    // Enable
    // ======================
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        
        loadConfig();
        Bukkit.getServer().getLogger().info("[TreasureHunt] Config loaded.");
        
        Bukkit.getServer().getLogger().info("[TreasureHunt] Loading treasure chests.");
        loadChestsFromFile();
        
        startTimerThread();
        
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Enabled!");
    }
    
    // ======================
    // Disable
    // ======================
    public void onDisable() {
    	Bukkit.getServer().getLogger().info("[TreasureHunt] Saving treasure chests.");
    	saveChestsToFile();
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Disabled!");
    }
    
    void loadConfig() {
    	String worldString = getConfig().getString("huntArea.world");
    	this.world = getServer().getWorld(worldString);
    	
    	this.minX = getConfig().getInt("huntArea.x-Range.min");
    	this.maxX = getConfig().getInt("huntArea.x-Range.max");
    	this.minY = getConfig().getInt("huntArea.y-Range.min");
    	this.maxY = getConfig().getInt("huntArea.y-Range.max");
    	this.minZ = getConfig().getInt("huntArea.z-Range.min");
    	this.maxZ = getConfig().getInt("huntArea.z-Range.max");
    	
    	this.spawnInterval = getConfig().getInt("spawnInterval");
    	this.chestDuration = getConfig().getInt("chestDuration");
    	this.openedChestDuration = getConfig().getInt("openedChestDuration");
    	this.maxSpawnAttempts = getConfig().getInt("maxSpawnAttempts");
    	this.minPlayersOnline = getConfig().getInt("minPlayersOnline");
    	this.maxChests = getConfig().getInt("maxChests");
    }
    
    void saveChestsToFile() {
    	for (TreasureChest chest : chests) {
    		if (!chest.isOpened()) {
    			String id = chest.getID().toString();
        		chestsData.set(id + ".location.world", chest.getLocation().getWorld().getName());
        		chestsData.set(id + ".location.xPos", chest.getLocation().getBlockX());
        		chestsData.set(id + ".location.yPos", chest.getLocation().getBlockY());
        		chestsData.set(id + ".location.zPos", chest.getLocation().getBlockZ());
        		chestsData.set(id + ".timeAlive", chest.getTimeAlive());
    		} else {
    			chest.despawn();
    		}
    	}
        try {
            chestsData.save(chestsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    void loadChestsFromFile() {
        if (chestsFile.exists()) {
        	Set<String> keys = chestsData.getKeys(false);
        	for (String key : keys) {
        		String worldString = chestsData.getString(key + ".location.world");
        		int xPos = chestsData.getInt(key + ".location.xPos");
        		int yPos = chestsData.getInt(key + ".location.yPos");
        		int zPos = chestsData.getInt(key + ".location.zPos");
        		int timeAlive = chestsData.getInt(key + ".timeAlive");
        		UUID id = UUID.fromString(key);
        		World world = getServer().getWorld(worldString);
        		Location location = new Location(world, xPos, yPos, zPos);
        		TreasureChest treasureChest = new TreasureChest(id, location, timeAlive);
        		chests.add(treasureChest);
        		chestsData.set(key, null);
        	}
        }
    }
    
    void startHunt() {
		if (chests.size() < maxChests) {
	    	Location treasureLocation = getTreasureLocation();
	    	if (treasureLocation.getBlockX() == -1 && treasureLocation.getBlockY() == -1 && treasureLocation.getBlockZ() == -1) {
	    		// TO-DO: Annouce to only admins
	    		Bukkit.getServer().getLogger().info("[TreasureHunt] Failed to spawn a treasure chest after max attempts.");
	    	} else {
	    		// TO-DO: get random loot
	    		UUID id = UUID.randomUUID();
	    		TreasureChest treasureChest = new TreasureChest(id, treasureLocation);
	    		treasureChest.spawn();
	    		chests.add(treasureChest);
	    		Bukkit.getServer().getLogger().info("[TreasureHunt] Chest spawned at " + treasureLocation.getBlockX() + ", " + treasureLocation.getBlockY() + ", " + treasureLocation.getBlockZ());
	    	}
		}
    }
    
    Location getTreasureLocation() {
    	Location randLocation;
    	
    	int attempt = 1;
    	while (attempt <= maxSpawnAttempts) {
    		randLocation = getRandomLocation();

    		while (attempt <= maxSpawnAttempts && randLocation.getBlockY() >= minY) {
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
    
    void incrementChestTimes() {
    	List<TreasureChest> toRemove = new ArrayList<TreasureChest>();
    	for (int i=0; i<chests.size(); i++) {
    		TreasureChest chest = chests.get(i);
    		if (chest.isOpened()) {
    			chest.setTimeSinceOpened(chest.getTimeSinceOpened()+1);
    			if (chest.getTimeSinceOpened() > openedChestDuration) {
    				chest.despawn();
    				toRemove.add(chest);
    			}
    		} else {
        		chest.setTimeAlive(chest.getTimeAlive()+1);
        		if (chest.getTimeAlive() > chestDuration) {
        			chest.despawn();
        			toRemove.add(chest);
        		}
    		}
    	}
    	chests.removeAll(toRemove);
    }
    
    public void startTimerThread() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
        	@Override
            public void run() {
                incrementChestTimes();
                spawnTimer++;
                if (spawnTimer > spawnInterval) {
                	if (getServer().getOnlinePlayers().size() >= minPlayersOnline) {
                    	startHunt();
                	}
                	spawnTimer=0;
                }
            }
        }, 0L, 20 * 1);
    }
    
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
    	Player player = (Player) e.getPlayer();
    	if (player != null) {
    		if (e.getInventory().getType() == InventoryType.CHEST) {
    			for (TreasureChest chest : chests) {
    				if (chest.getLocation().equals(e.getInventory().getLocation())) {
    					chest.setOpened(true);
    				}
    			}
    		}
    	}
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