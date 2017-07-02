package me.kevinnovak.treasurehunt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class LootGenerator {
	List<TreasureChestType> treasureChestTypes = new ArrayList<TreasureChestType>();
	int totalWeight = 0;
	
	LootGenerator(File[] files) {
		this.setTreasureChestTypes(files);
		this.calculateTotalWeight();
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
	
	void calculateTotalWeight() {
		int totalWeight = 0;
		for (TreasureChestType treasureChestType : treasureChestTypes) {
			totalWeight = totalWeight + treasureChestType.getWeight();
		}
		this.totalWeight = totalWeight;
	}
	
	List<ItemStack> generateRandomItems() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		
		
		return items;
	}
}