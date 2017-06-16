package me.kevinnovak.treasurehunt;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin implements Listener{
    // ======================
    // Enable
    // ======================
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Enabled!");
    }
    
    // ======================
    // Disable
    // ======================
    public void onDisable() {
        Bukkit.getServer().getLogger().info("[TreasureHunt] Plugin Disabled!");
    }
}