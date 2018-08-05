package me.kevinnovak.treasurehunt;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LootGenerator {
	public static final int CHEST_SIZE = 27;
	
	Random rand = new Random();
	private ColorConverter colorConv = new ColorConverter();
	List<TreasureChestType> treasureChestTypes = new ArrayList<TreasureChestType>();
	int maxFitItemAttempts = 0;
	int defaultItemWeight = 0;
	int totalWeight = 0;
	int[] weights;
	int bufferPercentage = 0;
	
	LootGenerator(File[] files, int maxFitItemAttempts, int defaultItemWeight, int bufferPercentage) {
		this.maxFitItemAttempts = maxFitItemAttempts;
		this.defaultItemWeight = defaultItemWeight;
		this.bufferPercentage = bufferPercentage;
		this.setTreasureChestTypes(files);
		this.setupWeights();
	}
	
	List<TreasureChestType> getTreasureChestTypes() {
		return this.treasureChestTypes;
	}
	
	void setTreasureChestTypes(List<TreasureChestType> treasureChestTypes) {
		this.treasureChestTypes = treasureChestTypes;
	}
	
	@SuppressWarnings("deprecation")
	List<TreasureChestItem> getItemsFromFile(File file) {
		List<TreasureChestItem> chestItems = new ArrayList<TreasureChestItem>();
		if (file.exists()) {
			FileConfiguration data = YamlConfiguration.loadConfiguration(file);
			if (data.isSet("items")) {
				ConfigurationSection itemsData = data.getConfigurationSection("items");
				for (String key : itemsData.getKeys(false)) {
					if (itemsData.isSet(key + ".name") && itemsData.isSet(key + ".value")) {
						// get id and data
						String itemName = itemsData.getString(key + ".name").toUpperCase();
						Material itemMaterial = Material.getMaterial(itemName);
						
						// get value
						int itemValue = itemsData.getInt(key + ".value");
						
						// get amount
						int itemAmount = 1;
						if (itemsData.isSet(key + ".amount")) {
							itemAmount = itemsData.getInt(key + ".amount");
						}
						
						// get weight
						int itemWeight = this.defaultItemWeight;
						if (itemsData.isSet(key + ".weight")) {
							itemWeight = itemsData.getInt(key + ".weight");
						}
						
						ItemStack item = new ItemStack(itemMaterial, itemAmount);
						
						// add enchantments
						if (itemsData.isSet(key + ".enchantments")) {
							ConfigurationSection enchantmentsData = itemsData.getConfigurationSection(key + ".enchantments");
							for (String enchantmentName : enchantmentsData.getKeys(false)) {
								Enchantment enchantment = Enchantment.getByName(enchantmentName);
								item.addUnsafeEnchantment(enchantment, enchantmentsData.getInt(enchantmentName));
							}
						}

						TreasureChestItem chestItem = new TreasureChestItem(item, itemValue, itemWeight);
						chestItems.add(chestItem);
					}
				}
			}
		}
		return chestItems;
	}
	
	void setTreasureChestTypes(File[] files) {
		for (File file : files) {
			if (file.exists()) {
				FileConfiguration data = YamlConfiguration.loadConfiguration(file);
				if (data.isSet("name") && data.isSet("weight") && data.isSet("value") && data.isSet("items")) {
					String name = colorConv.convert(data.getString("name") + "&r");
					int weight = data.getInt("weight");
					int value = data.getInt("value");

					List<TreasureChestItem> chestItems = this.getItemsFromFile(file);
					
					if (data.isSet("inheritance")){
						List<String> inheritance = data.getStringList("inheritance");
						for (String inheritedFileName : inheritance) {
							for (File otherFile : files) {
								if (otherFile.getName().equals(inheritedFileName + ".yml") || otherFile.getName().equals(inheritedFileName)) {
									List<TreasureChestItem> inheritedItems = this.getItemsFromFile(otherFile);
									chestItems.addAll(inheritedItems);
								}
							}
						}
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
		
		int chestValue = chestType.getValue();
		int bufferAmount = (int) ((double) chestValue * (double) this.bufferPercentage / (double) 100);
		
		int minValue = chestValue - bufferAmount;
		int maxValue = chestValue + bufferAmount;
		
		int currentValue = 0;
		int failedAttempts = 0;
		
		while (currentValue < minValue && items.size() < CHEST_SIZE && failedAttempts < maxFitItemAttempts) {
			TreasureChestItem item = chestType.getRandomItem();
			int itemValue = item.getValue();
			if (currentValue + itemValue <= maxValue) {
				failedAttempts = 0;
				currentValue = currentValue + itemValue;
				items.add(item.getItem());
			} else {
				failedAttempts++;
			}
		}
		
		while (items.size() < CHEST_SIZE) {
			items.add(new ItemStack(Material.AIR));
		}
		
		Collections.shuffle(items);
		
		return items;
	}
	
	String getChestTypeName(int i) {
		return this.treasureChestTypes.get(i).getName();
	}
}