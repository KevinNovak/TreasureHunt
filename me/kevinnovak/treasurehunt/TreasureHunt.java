package me.kevinnovak.treasurehunt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class TreasureHunt extends JavaPlugin implements Listener{
	// Files
    File chestsFile = new File(getDataFolder() + "/data/chests.yml");
    FileConfiguration chestsData = YamlConfiguration.loadConfiguration(chestsFile);
    File huntersFile = new File(getDataFolder() + "/data/hunters.yml");
    FileConfiguration huntersData = YamlConfiguration.loadConfiguration(huntersFile);
    File languageFile, treasureFile;
    FileConfiguration languageData, treasureData;
	
	// Config
    private ItemStack huntItem;
	private World world;
	private int minX, maxX, minY, maxY, minZ, maxZ;
	private int spawnInterval, chestDuration, openedChestDuration, maxSpawnAttempts, maxFitItemAttempts;
	private int minPlayersOnline, maxChests;
	private List<Integer> dontSpawnOn;
	private boolean protectAgainstBreak, protectAgainstBurn, protectAgainstExplode;
	
	// Plugin
	private List <TreasureChest> chests = new ArrayList<TreasureChest>();
	private List <TreasureHunter> hunters = new ArrayList<TreasureHunter>();
	private int spawnTimer;
	private LanguageManager langMan = new LanguageManager(this);
	private TimeConverter timeConv = new TimeConverter(langMan.day, langMan.days, langMan.hour, langMan.hours, langMan.minute, langMan.minutes, langMan.second, langMan.seconds);;
	private LootGenerator lootGen;
	private PermissionManager perm = new PermissionManager();
	
    // ======================
    // Enable
    // ======================
    public void onEnable() {
        saveDefaultConfig();
        
        // get language file
        languageFile = new File(getDataFolder() + "/language.yml");
        if (!languageFile.exists()) {
            saveResource("language.yml", false);
        }
        languageData = YamlConfiguration.loadConfiguration(languageFile);
        
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        
        loadConfig();
        Bukkit.getServer().getLogger().info("[TreasureHunt] Config loaded.");
        
        // get treasure files
        copyTreasureFiles();
        
        Bukkit.getServer().getLogger().info("[TreasureHunt] Loading treasure chests.");
        loadChestsFromFile();
        
        Bukkit.getServer().getLogger().info("[TreasureHunt] Loading treasure hunters.");
        loadHuntersFromFile();
        
        startTimerThread();
        
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Enabled!");
    }
    
    // ======================
    // Disable
    // ======================
    public void onDisable() {
    	Bukkit.getServer().getLogger().info("[TreasureHunt] Saving treasure chests.");
    	saveChestsToFile();
    	Bukkit.getServer().getLogger().info("[TreasureHunt] Saving treasure hunters.");
    	saveHuntersToFile();
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Disabled!");
    }
    
    @SuppressWarnings("deprecation")
	void loadConfig() {
    	this.huntItem = new ItemStack(getConfig().getInt("huntItem"));
    	
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
    	this.maxFitItemAttempts = getConfig().getInt("maxFitItemAttempts");
    	this.minPlayersOnline = getConfig().getInt("minPlayersOnline");
    	this.maxChests = getConfig().getInt("maxChests");
    	
    	this.dontSpawnOn = getConfig().getIntegerList("dontSpawnOn");
    	
    	this.protectAgainstBreak = getConfig().getBoolean("protectAgainst.break");
    	this.protectAgainstBurn = getConfig().getBoolean("protectAgainst.burn");
    	this.protectAgainstExplode = getConfig().getBoolean("protectAgainst.explode");
    }
    
    void copyTreasureFiles() {
    	File treasureDir = new File(getDataFolder() + "/treasure");
    	if (!treasureDir.isDirectory() || !(treasureDir.list().length > 0)) {
			saveResource("treasure/common.yml", false);
			saveResource("treasure/epic.yml", false);
			saveResource("treasure/legendary.yml", false);
			saveResource("treasure/rare.yml", false);
			saveResource("treasure/uncommon.yml", false);
    	}
    	lootGen = new LootGenerator(treasureDir.listFiles(), this.maxFitItemAttempts);
    }
    
    void saveHuntersFile() {
        try {
            huntersData.save(this.huntersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    void saveChestsFile() {
        try {
            chestsData.save(this.chestsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    void saveHuntersToFile() {
    	for (TreasureHunter hunter : this.hunters) {
    		String id = hunter.getID().toString();
    		huntersData.set(id + ".chestsFound", hunter.getChestsFound());
    	}
    	saveHuntersFile();
    }
    
    void loadHuntersFromFile() {
    	if (huntersFile.exists()) {
    		Set<String> keys = huntersData.getKeys(false);
    		for (String key : keys) {
    			UUID id = UUID.fromString(key);
    			int chestsFound = huntersData.getInt(key + ".chestsFound");
    			TreasureHunter hunter = new TreasureHunter(id, chestsFound);
    			hunters.add(hunter);
    		}
    	}
    }
    
    void saveChestsToFile() {
    	for (TreasureChest chest : chests) {
    		if (!chest.isOpened()) {
    			String id = chest.getID().toString();
        		chestsData.set(id + ".type", chest.getType());
        		chestsData.set(id + ".location.world", chest.getLocation().getWorld().getName());
        		chestsData.set(id + ".location.xPos", chest.getLocation().getBlockX());
        		chestsData.set(id + ".location.yPos", chest.getLocation().getBlockY());
        		chestsData.set(id + ".location.zPos", chest.getLocation().getBlockZ());
        		chestsData.set(id + ".timeAlive", chest.getTimeAlive());
    		} else {
    			chest.despawn();
    		}
    	}
    	saveChestsFile();
    }
    
    void loadChestsFromFile() {
        if (chestsFile.exists()) {
        	Set<String> keys = chestsData.getKeys(false);
        	for (String key : keys) {
        		String type = chestsData.getString(key + ".type");
        		String worldString = chestsData.getString(key + ".location.world");
        		int xPos = chestsData.getInt(key + ".location.xPos");
        		int yPos = chestsData.getInt(key + ".location.yPos");
        		int zPos = chestsData.getInt(key + ".location.zPos");
        		int timeAlive = chestsData.getInt(key + ".timeAlive");
        		UUID id = UUID.fromString(key);
        		World world = getServer().getWorld(worldString);
        		Location location = new Location(world, xPos, yPos, zPos);
        		TreasureChest treasureChest = new TreasureChest(id, location, type, timeAlive);
        		chests.add(treasureChest);
        		chestsData.set(key, null);
        	}
        	saveChestsFile();
        }
    }
    
    List<TreasureChest> getAvailableChests() {
    	List<TreasureChest> availableChests = new ArrayList<TreasureChest>();
    	for (TreasureChest chest : this.chests) {
    		if (!chest.isOpened()) {
    			availableChests.add(chest);
    		}
    	}
    	if (availableChests.size() > 1) {
        	Collections.sort(availableChests, new Comparator<TreasureChest>() {
        	    public int compare(TreasureChest left, TreasureChest right)  {
        	        return right.getTimeAlive() - left.getTimeAlive(); // The order depends on the direction of sorting.
        	    }
        	});
    	}
    	return availableChests;
    }
    
    int getRemainingTime(TreasureChest chest) {
    	return this.chestDuration - chest.getTimeAlive();
    }
    
    void sortHunters() {
    	Collections.sort(hunters, new Comparator<TreasureHunter>() {
    	    public int compare(TreasureHunter left, TreasureHunter right)  {
    	        return right.getChestsFound() - left.getChestsFound(); // The order depends on the direction of sorting.
    	    }
    	});
    }
    
    void startHunt() {
		if (this.getAvailableChests().size() < maxChests) {
	    	Location treasureLocation = getTreasureLocation();
	    	if (treasureLocation.getBlockX() == -1 && treasureLocation.getBlockY() == -1 && treasureLocation.getBlockZ() == -1) {
	    		// TO-DO: Annouce to only admins
	    		Bukkit.getServer().getLogger().info("[TreasureHunt] Failed to spawn a treasure chest after max attempts.");
	    	} else {
	    		// TO-DO: get random loot
	    		UUID id = UUID.randomUUID();
	    		
        		int chestTypeNum = lootGen.selectChestType();
        		String type = lootGen.getChestTypeName(chestTypeNum);
        		List<ItemStack> items = lootGen.generateRandomItems(chestTypeNum);
        		ItemStack[] itemsArray = new ItemStack[items.size()];
        		itemsArray = items.toArray(itemsArray);
	    		TreasureChest treasureChest = new TreasureChest(id, treasureLocation, type);
	    		treasureChest.spawn(itemsArray);
	    		
	    		chests.add(treasureChest);
	    		for (Player player : Bukkit.getOnlinePlayers()) {
	    			player.sendMessage(this.langMan.chestSpawned.replace("{RARITY}", type));
	    		}
	    		Bukkit.getServer().getLogger().info("[TreasureHunt] Chest spawned at " + treasureLocation.getBlockX() + ", " + treasureLocation.getBlockY() + ", " + treasureLocation.getBlockZ());
	    	}
		}
    }
    
    @SuppressWarnings("deprecation")
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
    					Material blockBelowMaterial = blockBelow.getBlock().getType();
    					if (blockBelowMaterial != Material.AIR) {
    						boolean forbidden = false;
    						for (Integer itemID : dontSpawnOn) {
    							if (blockBelowMaterial == Material.getMaterial(itemID)) {
    								forbidden = true;
    							}
    						}
    						if (!forbidden) {
        						return randLocation;
    						}
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
    
    void printHelp(Player player) {
    	player.sendMessage("Treasure Hunt Help");
    }
    
    void printChests(Player player, int pageNum) {
		List<TreasureChest> availableChests = getAvailableChests();
    	
		if (pageNum < 1 || pageNum > Math.ceil((double)availableChests.size()/5)) {
			pageNum = 1;
		}
		
    	player.sendMessage(langMan.spawnedChestsHeader);
    	if (availableChests.size() > 0) {
        	for (int i=5*(pageNum-1); i<availableChests.size() && i<(5*pageNum); i++) {
        		String time = timeConv.friendlyTime(getRemainingTime(availableChests.get(i)));
        		player.sendMessage(langMan.spawnedChestsChestLine.replace("{RARITY}", availableChests.get(i).getType()).replace("{RANK}", Integer.toString(i+1)).replace("{TIME}", time));
        	}
			if (availableChests.size() > 5*pageNum) {
				int nextPageNum = pageNum + 1;
				player.sendMessage(langMan.spawnedChestsMorePages.replace("{PAGE}", Integer.toString(nextPageNum)));
			}
    	} else {
    		player.sendMessage(langMan.spawnedChestsNoChests);
    	}
    	player.sendMessage(langMan.spawnedChestsFooter);
    }
    
    void printTopHunters(Player player, int pageNum) {
		if (pageNum < 1 || pageNum > Math.ceil((double)hunters.size()/5)) {
			pageNum = 1;
		}
    	
    	player.sendMessage(langMan.topHuntersHeader);
    	if (hunters.size() > 0) {
        	sortHunters();
        	for (int i=5*(pageNum-1); i<hunters.size() && i<(5*pageNum); i++) {
        		String name = "Unknown";
        		if (getServer().getOfflinePlayer(hunters.get(i).getID()).getName() != null) {
            		name = getServer().getOfflinePlayer(hunters.get(i).getID()).getName();
        		}
        		String chestsFound = String.valueOf(hunters.get(i).getChestsFound());
        		player.sendMessage(langMan.topHuntersHunterLine.replace("{RANK}", Integer.toString(i+1)).replace("{PLAYER}", name).replace("{CHESTS}", chestsFound));
        	}
			if (hunters.size() > 5*pageNum) {
				int nextPageNum = pageNum + 1;
				player.sendMessage(langMan.topHuntersMorePages.replace("{PAGE}", Integer.toString(nextPageNum)));
			}
    	} else {
    		player.sendMessage(langMan.topHuntersNoHunters);
    	}
    	player.sendMessage(langMan.topHuntersFooter);
    }
    
    int getHunterPos(UUID id) {
    	for (int i=0; i<this.hunters.size(); i++) {
    		if (this.hunters.get(i).getID().equals(id)) {
    			return i;
    		}
    	}
		return -1;
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
        			for (Player player : Bukkit.getOnlinePlayers()) {
        				player.sendMessage(langMan.chestDespawned.replace("{RARITY}", chest.getType()));
        			}
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
                		if (getAvailableChests().size() < maxChests) {
                			startHunt();
                		}
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
    					if (!chest.isOpened()) {
        					chest.setOpened(true);
        					UUID id = player.getUniqueId();
        					int hunterPos = getHunterPos(id);
        					if (hunterPos != -1) {
        						this.hunters.get(hunterPos).foundAChest();
        					} else {
        						TreasureHunter hunter = new TreasureHunter(id);
        						hunter.foundAChest();
        						this.hunters.add(hunter);
        					}
        					chest.setFoundBy(player.getName());
        					for (Player p : Bukkit.getOnlinePlayers()) {
        						p.sendMessage(langMan.chestFound.replace("{PLAYER}", player.getName()).replace("{RARITY}", chest.getType()));
        					}
    					} else {
    						String foundBy = chest.getFoundBy();
    						if (player.getName() != foundBy) {
    							e.setCancelled(true);
    							player.sendMessage(langMan.alreadyFound.replace("{PLAYER}", foundBy));
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
    	if (this.protectAgainstBreak) {
        	if (e.getBlock().getType() == Material.CHEST) {
        		for (TreasureChest chest : chests) {
        			if (chest.getLocation().equals(e.getBlock().getLocation())) {
        				e.setCancelled(true);
        			}
        		}
        	}
    	}
    }
    
    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
    	if (this.protectAgainstBurn) {
	    	if (e.getBlock().getType() == Material.CHEST) {
	    		for (TreasureChest chest : chests) {
	    			if (chest.getLocation().equals(e.getBlock().getLocation())) {
	    				e.setCancelled(true);
	    			}
	    		}
	    	}
    	}
    }
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
    	if (this.protectAgainstBurn) {
	    	if (e.getBlock().getType() == Material.CHEST) {
	    		for (TreasureChest chest : chests) {
	    			if (chest.getLocation().equals(e.getBlock().getLocation())) {
	    				e.setCancelled(true);
	    			}
	    		}
	    	}
    	}
    }
    
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
    	if (this.protectAgainstExplode) {
	    	for (Block block : e.blockList()) {
	        	if (block.getType() == Material.CHEST) {
	        		for (TreasureChest chest : chests) {
	        			if (chest.getLocation().equals(block.getLocation())) {
	        				e.setCancelled(true);
	        			}
	        		}
	        	}
	    	}
    	}
    }
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
    	if (this.protectAgainstExplode) {
        	for (Block block : e.blockList()) {
            	if (block.getType() == Material.CHEST) {
            		for (TreasureChest chest : chests) {
            			if (chest.getLocation().equals(block.getLocation())) {
            				e.setCancelled(true);
            			}
            		}
            	}
        	}
    	}
    }
    
    int distanceBetween(Location here, Location there) {
    	int x1 = here.getBlockX();
    	int x2 = there.getBlockX();
    	int z1 = here.getBlockZ();
    	int z2 = there.getBlockZ();
    	int distance = (int) Math.sqrt(((x1 - x2)*(x1 - x2)) + ((z1 - z2)*(z1 - z2)));
    	return distance;
    }
    
    void sendDistance(Player player) {
    	List<TreasureChest> availableChests = this.getAvailableChests();
    	if (availableChests.size() > 0) {
    		Location playerLoc = player.getLocation();
			if (playerLoc.getWorld().equals(world)) {
	    		int closestDistance = Integer.MAX_VALUE;
				for (TreasureChest chest : availableChests) {
					int distance = distanceBetween(playerLoc, chest.getLocation());
	    			if (distance < closestDistance) {
	    				closestDistance = distance;
	    			}
	    		}
				player.sendMessage(langMan.huntItemSendDistance.replace("{DISTANCE}", Integer.toString(closestDistance)));
			} else {
				player.sendMessage(langMan.huntItemAnotherWorld);
			}
    	} else {
    		player.sendMessage(langMan.huntItemNoChests);
    	}
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    // when a player clicks
    public void interact(PlayerInteractEvent event) {
        // get the players name and type of click
        Player player = event.getPlayer();
        Action action = event.getAction();
        
        // if player is left clicking with the hunt item
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (player.getItemInHand().getType() == huntItem.getType()) {
            	if (player.hasPermission(perm.item)) {
                	this.sendDistance(player);
            	} else {
            		player.sendMessage(langMan.noPermission);
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
        Player player = (Player) sender;
        
        // otherwise the command sender is a player
        //final Player player = (Player) sender;
        //final String playername = player.getName();
        
        // ======================
        // /mt
        // ======================
        if(cmd.getName().equalsIgnoreCase("th")) {
            // th
        	if (args.length == 0) {
            	printHelp(player);
            	return true;
            } else if (args.length > 0) {
        		// th start
        		if (args[0].equalsIgnoreCase("start")) {
        			if (player.hasPermission(perm.start)) {
                    	if (this.getAvailableChests().size() < maxChests) {
                        	startHunt();
                        	return true;
                    	} else {
                    		player.sendMessage(langMan.tooManyChests);
                        	return true;
                    	}
        			} else {
        				player.sendMessage(langMan.noPermission);
        				return true;
        			}
            	}
        		// th top
        		else if (args[0].equalsIgnoreCase("top")) {
            		int pageNum = 1;
            		if (args.length >= 2) {
                		if (tryParse(args[1]) != null) {
                			pageNum = tryParse(args[1]);
                		}
            		}
            		printTopHunters(player, pageNum);
            		return true;
            	} else if (args[0].equalsIgnoreCase("chests")) {
            		int pageNum = 1;
            		if (args.length >= 2) {
                		if (tryParse(args[1]) != null) {
                			pageNum = tryParse(args[1]);
                		}
            		}
            		printChests(player, pageNum);
            		return true;
            	} else {
            		printHelp(player);
            		return true;
            	}
            }
        }
        
		return false;
    }
    
    public static Integer tryParse(String text) {
    	try {
    		return Integer.parseInt(text);
    	} catch (NumberFormatException e) {
    		return null;
    	}
    }
}