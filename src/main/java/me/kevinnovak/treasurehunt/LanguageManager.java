package me.kevinnovak.treasurehunt;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class LanguageManager {
    public TreasureHunt plugin;
    private ColorConverter colorConv = new ColorConverter();

    public String consolePrefix = "[TreasureHunt] ";
    public String chestSpawned, announceTime, chestDespawned, chestFound;
    public String closestPlayer, notClosestPlayer, chestSpawnFailed, tooManyChests, alreadyFound, despawnedAllChests, noSpawnedChests, noPermission;
    public String huntItemSendDistance, huntItemAnotherWorld, huntItemNoChests;
    public String commandMenuHeader, commandMenuCommandHelp, commandMenuCommandList, commandMenuCommandTop, commandMenuCommandStart, commandMenuCommandDespawn, commandMenuMorePages, commandMenuNoCommands, commandMenuFooter;
    public String helpHeader, helpMorePages, helpFooter;
    public List<String> helpLines;
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

        this.closestPlayer = colorConv.convert(languageData.getString("closestPlayer"));
        this.notClosestPlayer = colorConv.convert(languageData.getString("notClosestPlayer"));
        this.chestSpawnFailed = colorConv.convert(languageData.getString("chestSpawnFailed"));
        this.tooManyChests = colorConv.convert(languageData.getString("tooManyChests"));
        this.alreadyFound = colorConv.convert(languageData.getString("alreadyFound"));
        this.despawnedAllChests = colorConv.convert(languageData.getString("despawnedAllChests"));
        this.noSpawnedChests = colorConv.convert(languageData.getString("noSpawnedChests"));
        this.noPermission = colorConv.convert(languageData.getString("noPermission"));

        this.huntItemSendDistance = colorConv.convert(languageData.getString("huntItem.sendDistance"));
        this.huntItemAnotherWorld = colorConv.convert(languageData.getString("huntItem.anotherWorld"));
        this.huntItemNoChests = colorConv.convert(languageData.getString("huntItem.noChests"));

        this.commandMenuHeader = colorConv.convert(languageData.getString("commandMenu.header"));
        this.commandMenuCommandHelp = colorConv.convert(languageData.getString("commandMenu.command.help"));
        this.commandMenuCommandList = colorConv.convert(languageData.getString("commandMenu.command.list"));
        this.commandMenuCommandTop = colorConv.convert(languageData.getString("commandMenu.command.top"));
        this.commandMenuCommandStart = colorConv.convert(languageData.getString("commandMenu.command.start"));
        this.commandMenuCommandDespawn = colorConv.convert(languageData.getString("commandMenu.command.despawn"));
        this.commandMenuMorePages = colorConv.convert(languageData.getString("commandMenu.morePages"));
        this.commandMenuNoCommands = colorConv.convert(languageData.getString("commandMenu.noCommands"));
        this.commandMenuFooter = colorConv.convert(languageData.getString("commandMenu.footer"));

        this.helpHeader = colorConv.convert(languageData.getString("help.header"));
        this.helpLines = colorConv.convert(languageData.getStringList("help.lines"));
        this.helpMorePages = colorConv.convert(languageData.getString("help.morePages"));
        this.helpFooter = colorConv.convert(languageData.getString("help.footer"));
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
        this.consoleChestSpawnFailed = colorConv.convert(languageData.getString("console.chestSpawnFailed"));
        this.consoleAnnounceTime = colorConv.convert(languageData.getString("console.announceTime"));
        this.consoleChestDespawned = colorConv.convert(languageData.getString("console.chestDespawned"));
        this.consoleChestFound = colorConv.convert(languageData.getString("console.chestFound"));
    }
}