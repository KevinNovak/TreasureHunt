package me.kevinnovak.treasurehunt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class TreasureChestType {
	String name = "None";
	int weight = 0;
	int value = 0;
	List<ItemStack> items = new ArrayList <ItemStack>();
	
	TreasureChestType(String name, int weight, int value, List<ItemStack> items) {
		this.name = name;
		this.weight = weight;
		this.value = value;
		this.items = items;
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
	
	List<ItemStack> getItems() {
		return this.items;
	}

}