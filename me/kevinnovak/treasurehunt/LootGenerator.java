package me.kevinnovak.treasurehunt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LootGenerator {
	List<TreasureChestType> treasureChestTypes = new ArrayList<TreasureChestType>();
	
	LootGenerator(File[] files) {
		this.setTreasureChestTypes(files);
	}
	
	List<TreasureChestType> getTreasureChestTypes() {
		return this.treasureChestTypes;
	}
	
	void setTreasureChestTypes(List<TreasureChestType> treasureChestTypes) {
		this.treasureChestTypes = treasureChestTypes;
	}
	
	void setTreasureChestTypes(File[] files) {
		
	}
}