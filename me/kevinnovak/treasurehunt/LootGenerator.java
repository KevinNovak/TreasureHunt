package me.kevinnovak.treasurehunt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class LootGenerator {
	Random rand = new Random();
	List<TreasureChestType> treasureChestTypes = new ArrayList<TreasureChestType>();
	int totalWeight = 0;
	int[] weights;
	
	LootGenerator(File[] files) {
		this.setTreasureChestTypes(files);
		this.setupWeights();
	}
	
	List<TreasureChestType> getTreasureChestTypes() {
		return this.treasureChestTypes;
	}
	
	void setTreasureChestTypes(List<TreasureChestType> treasureChestTypes) {
		this.treasureChestTypes = treasureChestTypes;
	}
	
	void setTreasureChestTypes(File[] files) {
		for (File file : files) {
			if (file.exists()) {
				FileConfiguration data = YamlConfiguration.loadConfiguration(file);
				if (data.isSet("name") && data.isSet("weight") && data.isSet("value") && data.isSet("items")) {
					String name = data.getString("name");
					int weight = data.getInt("weight");
					int value = data.getInt("value");
					
					// Add itemstacks to list of items
					List<TreasureChestItem> chestItems = new ArrayList<TreasureChestItem>();
					ConfigurationSection itemsData = data.getConfigurationSection("items");
					for (String key : itemsData.getKeys(false)) {
						@SuppressWarnings("deprecation")
						ItemStack item = new ItemStack(itemsData.getInt(key + ".id"), itemsData.getInt(key + ".amount"));
						TreasureChestItem chestItem = new TreasureChestItem(item, itemsData.getInt(key + ".value"));
						// TO-DO: value, item data, enchantments, etc
						chestItems.add(chestItem);
					}
					
					// Add treasure chest type to list of treasure chest types
					TreasureChestType treasureChestType = new TreasureChestType(name, weight, value, chestItems);
					treasureChestTypes.add(treasureChestType);
				}
			}
		}
	}
	
	void setupWeights() {
		this.weights = new int[treasureChestTypes.size() + 1];
		this.weights[0] = 0;
		int totalWeight = 0;
		for (int i=0; i<treasureChestTypes.size(); i++) {
			int weight = treasureChestTypes.get(i).getWeight();
			totalWeight = totalWeight + weight;
			this.weights[i+1] = weight + weights[i];
		}
		this.totalWeight = totalWeight;
	}
	
	int selectChestType() {
		int randWeight = rand.nextInt(totalWeight); // min 0, max totalWeight-1
		int i = 0;
		while (randWeight >= weights[i] && randWeight < totalWeight) {
			i++;
		}
		i = i-1;
		return i;
	}
	
	List<ItemStack> generateRandomItems(int i) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		TreasureChestType chestType = this.treasureChestTypes.get(i);
		List<TreasureChestItem> chestItems = chestType.getChestItems();
		int remainingValue = chestType.getValue();
		
		int failedAttempts = 0;
		while (remainingValue > 0 && items.size() < 27 && failedAttempts < 3000) {
			int randInt = rand.nextInt(chestItems.size());
			TreasureChestItem item = chestItems.get(randInt);
			int itemValue = item.getValue();
			if (itemValue <= remainingValue) {
				failedAttempts = 0;
				remainingValue = remainingValue - itemValue;
				items.add(item.getItem());
			} else {
				failedAttempts++;
			}
		}
		
		return items;
	}
	
	String getChestTypeName(int i) {
		return this.treasureChestTypes.get(i).getName();
	}
}