package me.kevinnovak.treasurehunt;

import org.bukkit.Location;
import org.bukkit.Material;

public class TreasureChest {
	private Location location;
	//private Inventory contents;
	private int timeAlive = 0;
	private boolean isSpawned = false;
	
	TreasureChest(Location location) {
        this.location = location;
        //this.contents = contents;
    }
	
	int getTimeAlive() {
		return this.timeAlive;
	}
	
	void setTimeAlive(int timeAlive) {
		this.timeAlive = timeAlive;
	}
	
	void spawn() {
		this.location.getBlock().setType(Material.CHEST);
		this.isSpawned = true;
	}
	
	void despawn() {
		this.location.getBlock().setType(Material.AIR);
		this.isSpawned = false;
	}
}