package me.kevinnovak.treasurehunt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class ColorConverter {
	
	public ColorConverter() {
		
	}
	
    String convert(String toConvert) {
        return ChatColor.translateAlternateColorCodes('&', toConvert);
    }
    
    List<String> convert(List<String> toConvert) {
        List<String> translatedColors = new ArrayList<String>();
        for (String stringToTranslate: toConvert){
            translatedColors.add(ChatColor.translateAlternateColorCodes('&',stringToTranslate));
        }
        return translatedColors;
    }
}