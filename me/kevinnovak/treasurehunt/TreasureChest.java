package me.kevinnovak.treasurehunt;

import org.bukkit.Location;
import org.bukkit.Material;

public class TreasureChest {
	private Location location;
	//private Inventory contents;
	private int timeAlive = 0;
	private int timeSinceOpened = 0;
	private boolean opened = false;
	
	TreasureChest(Location location) {
        this.location = location;
        //this.contents = contents;
    }
	
	Location getLocation() {
		return this.location;
	}
	
	void setLocation(Location location) {
		this.location = location;
	}
	
	int getTimeAlive() {
		return this.timeAlive;
	}
	
	void setTimeAlive(int timeAlive) {
		this.timeAlive = timeAlive;
	}
	
	int getTimeSinceOpened() {
		return this.timeSinceOpened;
	}
	
	void setTimeSinceOpened(int timeSinceOpened) {
		this.timeSinceOpened = timeSinceOpened;
	}
	
	boolean isOpened() {
		return this.opened;
	}
	
	void setOpened(boolean opened) {
		this.opened = opened;
	}
	
	void spawn() {
		this.location.getBlock().setType(Material.CHEST);
	}
	
	void despawn() {
		this.location.getBlock().setType(Material.AIR);
	}
}