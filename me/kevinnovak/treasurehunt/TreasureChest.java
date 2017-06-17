package me.kevinnovak.treasurehunt;

import org.bukkit.Location;
import org.bukkit.Material;

public class TreasureChest {
	private Location location;
	//private Inventory contents;
	//private double startTime;
	
	TreasureChest(Location location) {
        this.location = location;
        //this.contents = contents;
    }
	
	void spawn() {
		this.location.getBlock().setType(Material.CHEST);
	}
	
	void despawn() {
		this.location.getBlock().setType(Material.AIR);
	}
}