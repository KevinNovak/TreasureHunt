package me.kevinnovak.treasurehunt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TreasureChest {
	private TreasureChest plugin;
	private Location location;
	private Inventory contents;
	private double startTime;
	
	TreasureChest(TreasureChest plugin, Location location, Inventory contents, double startTime) {
        this.plugin = plugin;
        this.location = location;
        this.contents = contents;
        this.startTime = startTime;
    }
	
	void spawn() {
		this.location.getBlock().setType(Material.CHEST);
	}
}