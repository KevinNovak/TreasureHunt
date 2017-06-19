package me.kevinnovak.treasurehunt;

import java.util.UUID;

public class Players {
	private UUID id;
	private int chestsFound = 0;
	
	Players(UUID id) {
        this.id = id;
    }
	
	Players(UUID id, int chestsFound) {
		this.id = id;
		this.chestsFound = chestsFound;
    }
	
	UUID getID() {
		return this.id;
	}
	
	void setID(UUID id) {
		this.id = id;
	}
	
	int getChestsFound() {
		return this.chestsFound;
	}
	
	void setChestsFound(int chestsFound) {
		this.chestsFound = chestsFound;
	}
	
	void foundAChest() {
		this.chestsFound++;
	}
}