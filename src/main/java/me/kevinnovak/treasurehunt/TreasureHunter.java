package me.kevinnovak.treasurehunt;

import java.util.UUID;

public class TreasureHunter {
    private UUID id;
    private int chestsFound = 0;

    TreasureHunter(UUID id) {
        this.id = id;
    }

    TreasureHunter(UUID id, int chestsFound) {
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