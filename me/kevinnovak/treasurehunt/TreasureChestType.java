package me.kevinnovak.treasurehunt;

import java.util.ArrayList;
import java.util.List;

public class TreasureChestType {
	String name = "None";
	int weight = 0;
	int value = 0;
	List<TreasureChestItem> chestItems = new ArrayList <TreasureChestItem>();
	
	TreasureChestType(String name, int weight, int value, List<TreasureChestItem> chestItems) {
		this.name = name;
		this.weight = weight;
		this.value = value;
		this.chestItems = chestItems;
    }
	
	String getName() {
		return this.name;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	int getWeight() {
		return this.weight;
	}
	
	void setWeight(int weight) {
		this.weight = weight;
	}
	
	int getValue() {
		return this.value;
	}
	
	void setValue(int value) {
		this.value = value;
	}
	
	List<TreasureChestItem> getChestItems() {
		return this.chestItems;
	}
	
	void setChestItems(List<TreasureChestItem> chestItems) {
		this.chestItems = chestItems;
	}

}