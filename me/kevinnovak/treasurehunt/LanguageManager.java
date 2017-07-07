package me.kevinnovak.treasurehunt;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {
	public TreasureHunt plugin;
	private ColorConverter colorConv = new ColorConverter();
	
	public String consolePrefix = "[TreasureHunt] ";
	public String chestSpawned, announceTime, chestDespawned, chestFound, alreadyFound;
	public String tooManyChests, despawnedAllChests, noSpawnedChests, noPermission;
	public String huntItemSendDistance, huntItemAnotherWorld, huntItemNoChests;
	public String helpMenuHeader, helpMenuCommandChests, helpMenuCommandTop, helpMenuCommandStart, helpMenuCommandDespawn, helpMenuMorePages, helpMenuNoCommands, helpMenuFooter;
	public String chestListHeader, chestListChestLine, chestListMorePages, chestListNoChests, chestListFooter;
	public String topHuntersHeader, topHuntersHunterLine, topHuntersMorePages, topHuntersNoHunters, topHuntersFooter;
	public String day, days, hour, hours, minute, minutes, second, seconds;
	public String consoleChestSpawned, consoleChestSpawnFailed, consoleAnnounceTime, consoleChestDespawned, consoleChestFound;
	
	public LanguageManager(TreasureHunt plugin) {
		this.plugin = plugin;
	}
	
	void load() {
        File languageFile = new File(plugin.getDataFolder() + "/language.yml");
        if (!languageFile.exists()) {
        	this.plugin.log("Copying default language file.");
        	this.plugin.saveResource("language.yml", false);
        }
		this.plugin.log("Loading language file.");
        YamlConfiguration languageData = YamlConfiguration.loadConfiguration(languageFile);
        
    	this.chestSpawned = colorConv.convert(languageData.getString("chestSpawned"));
    	this.announceTime = colorConv.convert(languageData.getString("announceTime"));
    	this.chestDespawned = colorConv.convert(languageData.getString("chestDespawned"));
    	this.chestFound = colorConv.convert(languageData.getString("chestFound"));
    	this.alreadyFound = colorConv.convert(languageData.getString("alreadyFound"));
    	this.tooManyChests = colorConv.convert(languageData.getString("tooManyChests"));
    	this.despawnedAllChests = colorConv.convert(languageData.getString("despawnedAllChests"));
    	this.noSpawnedChests = colorConv.convert(languageData.getString("noSpawnedChests"));
    	this.noPermission = colorConv.convert(languageData.getString("noPermission"));
    	
    	this.huntItemSendDistance = colorConv.convert(languageData.getString("huntItem.sendDistance"));
    	this.huntItemAnotherWorld = colorConv.convert(languageData.getString("huntItem.anotherWorld"));
    	this.huntItemNoChests = colorConv.convert(languageData.getString("huntItem.noChests"));
   
    	this.helpMenuHeader = colorConv.convert(languageData.getString("helpMenu.header"));
    	this.helpMenuCommandChests = colorConv.convert(languageData.getString("helpMenu.command.chests"));
    	this.helpMenuCommandTop = colorConv.convert(languageData.getString("helpMenu.command.top"));
    	this.helpMenuCommandStart = colorConv.convert(languageData.getString("helpMenu.command.start"));
    	this.helpMenuCommandDespawn = colorConv.convert(languageData.getString("helpMenu.command.despawn"));
    	this.helpMenuMorePages = colorConv.convert(languageData.getString("helpMenu.morePages"));
    	this.helpMenuNoCommands = colorConv.convert(languageData.getString("helpMenu.noCommands"));
    	this.helpMenuFooter = colorConv.convert(languageData.getString("helpMenu.footer"));
    	
    	this.chestListHeader = colorConv.convert(languageData.getString("chestList.header"));
    	this.chestListChestLine = colorConv.convert(languageData.getString("chestList.chestLine"));
    	this.chestListMorePages = colorConv.convert(languageData.getString("chestList.morePages"));
    	this.chestListFooter = colorConv.convert(languageData.getString("chestList.footer"));
    	this.chestListNoChests = colorConv.convert(languageData.getString("chestList.noChests"));
    	
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
    	
    	this.consoleChestSpawned = colorConv.convert(languageData.getString("console.chestSpawned"));
    	this.consoleChestSpawnFailed= colorConv.convert(languageData.getString("console.chestSpawnFailed"));
    	this.consoleAnnounceTime = colorConv.convert(languageData.getString("console.announceTime"));
    	this.consoleChestDespawned = colorConv.convert(languageData.getString("console.chestDespawned"));
    	this.consoleChestFound = colorConv.convert(languageData.getString("console.chestFound"));
	}
}