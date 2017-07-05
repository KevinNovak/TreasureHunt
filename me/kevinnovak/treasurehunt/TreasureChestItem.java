package me.kevinnovak.treasurehunt;

import org.bukkit.inventory.ItemStack;

public class TreasureChestItem {
	@SuppressWarnings("deprecation")
	ItemStack item = new ItemStack(0);
	int value = 0;
	int weight = 0;
	
	TreasureChestItem(ItemStack item, int value, int weight) {
		this.item = item;
		this.value = value;
		this.weight = weight;
    }
	
	ItemStack getItem() {
		return this.item;
	}
	
	void setItem(ItemStack item) {
		this.item = item;
	}
	
	int getValue() {
		return this.value;
	}
	
	void setValue(int value) {
		this.value = value;
	}
	
	int getWeight() {
		return this.weight;
	}
	
	void setWeight(int weight) {
		this.weight = weight;
	}
}