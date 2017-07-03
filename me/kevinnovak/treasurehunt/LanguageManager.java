package me.kevinnovak.treasurehunt;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {
	public TreasureHunt plugin;
	private ColorConverter colorConv = new ColorConverter();
	
	public String chestSpawned, chestDespawned, chestFound, alreadyFound, tooManyChests, noPermission;
	public String huntItemSendDistance, huntItemAnotherWorld, huntItemNoChests;
	public String spawnedChestsHeader, spawnedChestsChestLine, spawnedChestsMorePages, spawnedChestsNoChests, spawnedChestsFooter;
	public String topHuntersHeader, topHuntersHunterLine, topHuntersMorePages, topHuntersNoHunters, topHuntersFooter;
	public String day, days, hour, hours, minute, minutes, second, seconds;
	
	public LanguageManager(TreasureHunt plugin) {
		this.plugin = plugin;
        File languageFile = new File(plugin.getDataFolder() + "/language.yml");
        if (!languageFile.exists()) {
        	plugin.saveResource("language.yml", false);
        }
        YamlConfiguration languageData = YamlConfiguration.loadConfiguration(languageFile);
        
    	this.chestSpawned = colorConv.convert(languageData.getString("chestSpawned"));
    	this.chestDespawned = colorConv.convert(languageData.getString("chestDespawned"));
    	this.chestFound = colorConv.convert(languageData.getString("chestFound"));
    	this.alreadyFound = colorConv.convert(languageData.getString("alreadyFound"));
    	this.tooManyChests = colorConv.convert(languageData.getString("tooManyChests"));
    	this.noPermission = colorConv.convert(languageData.getString("noPermission"));
    	
    	this.huntItemSendDistance = colorConv.convert(languageData.getString("huntItem.sendDistance"));
    	this.huntItemAnotherWorld = colorConv.convert(languageData.getString("huntItem.anotherWorld"));
    	this.huntItemNoChests = colorConv.convert(languageData.getString("huntItem.noChests"));
    	
    	this.spawnedChestsHeader = colorConv.convert(languageData.getString("spawnedChests.header"));
    	this.spawnedChestsChestLine = colorConv.convert(languageData.getString("spawnedChests.chestLine"));
    	this.spawnedChestsMorePages = colorConv.convert(languageData.getString("spawnedChests.morePages"));
    	this.spawnedChestsFooter = colorConv.convert(languageData.getString("spawnedChests.footer"));
    	this.spawnedChestsNoChests = colorConv.convert(languageData.getString("spawnedChests.noChests"));
    	
    	this.topHuntersHeader = colorConv.convert(languageData.getString("topHunters.header"));
    	this.topHuntersHunterLine = colorConv.convert(languageData.getString("topHunters.hunterLine"));
    	this.topHuntersMorePages = colorConv.convert(languageData.getString("topHunters.morePages"));
    	this.topHuntersFooter = colorConv.convert(languageData.getString("topHunters.footer"));
    	this.topHuntersNoHunters = colorConv.convert(languageData.getString("topHunters.noHunters"));
    	
    	this.day = languageData.getString("time.day");
    	this.days = languageData.getString("time.days");
    	this.hour = languageData.getString("time.hour");
    	this.hours = languageData.getString("time.hours");
    	this.minute = languageData.getString("time.minute");
    	this.minutes = languageData.getString("time.minutes");
    	this.second = languageData.getString("time.second");
    	this.seconds = languageData.getString("time.seconds");
	}
}