package me.kevinnovak.treasurehunt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreasureChestType {
	String name = "None";
	int weight = 0;
	int value = 0;
	int totalItemWeight = 0;
	int[] itemWeights;
	List<TreasureChestItem> chestItems = new ArrayList <TreasureChestItem>();
	Random rand = new Random();
	
	TreasureChestType(String name, int weight, int value, List<TreasureChestItem> chestItems) {
		this.name = name;
		this.weight = weight;
		this.value = value;
		this.chestItems = chestItems;
		this.setupWeights();
    }
	
	void setupWeights() {
		this.itemWeights = new int[chestItems.size() + 1];
		this.itemWeights[0] = 0;
		int totalItemWeight = 0;
		for (int i=0; i<chestItems.size(); i++) {
			int itemWeight = chestItems.get(i).getWeight();
			totalItemWeight = totalItemWeight + itemWeight;
			this.itemWeights[i+1] = itemWeight + itemWeights[i];
		}
		this.totalItemWeight = totalItemWeight;
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
	
	TreasureChestItem getRandomItem() {
		int randWeight = rand.nextInt(totalItemWeight); // min 0, max totalItemWeight-1
		int i = 0;
		while (randWeight >= itemWeights[i] && randWeight < totalItemWeight) {
			i++;
		}
		i = i-1;
		return this.chestItems.get(i);
	}

}