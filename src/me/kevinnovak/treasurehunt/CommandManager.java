package me.kevinnovak.treasurehunt;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class CommandManager {
	public TreasureHunt plugin;
	
	public List<String> helpCommands, listCommands, topCommands, startCommands, despawnCommands;
	
	public CommandManager(TreasureHunt plugin) {
		this.plugin = plugin;
	}
	
	void load() {
        File commandsFile = new File(plugin.getDataFolder() + "/commands.yml");
        if (!commandsFile.exists()) {
        	this.plugin.log("Copying default commands file.");
        	this.plugin.saveResource("commands.yml", false);
        }
		this.plugin.log("Loading commands file.");
        YamlConfiguration commandsData = YamlConfiguration.loadConfiguration(commandsFile);
        
    	this.helpCommands = commandsData.getStringList("help");
    	this.listCommands = commandsData.getStringList("list");
    	this.topCommands = commandsData.getStringList("top");
    	this.startCommands = commandsData.getStringList("start");
    	this.despawnCommands = commandsData.getStringList("despawn");
	}
}