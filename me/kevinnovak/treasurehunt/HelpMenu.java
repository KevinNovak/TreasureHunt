package me.kevinnovak.treasurehunt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class HelpMenu {
	private PermissionManager perm;
	private LanguageManager langMan;
	
	public HelpMenu(PermissionManager perm, LanguageManager langMan) {
		this.perm = perm;
		this.langMan = langMan;
	}
	
	public List<String> getAllowedCommandLines(Player player) {
		List<String> commandLines = new ArrayList<String>();
		
		if (player.hasPermission(perm.chests)) {
			commandLines.add(langMan.helpMenuCommandChests);
		}
		
		if (player.hasPermission(perm.top)) {
			commandLines.add(langMan.helpMenuCommandTop);
		}
		
		if (player.hasPermission(perm.start)) {
			commandLines.add(langMan.helpMenuCommandStart);
		}
		
		return commandLines;
	}
	
	public void print(Player player, int pageNum) {
		List<String> commandLines = this.getAllowedCommandLines(player);
    	
		if (pageNum < 1 || pageNum > Math.ceil((double)commandLines.size()/5)) {
			pageNum = 1;
		}
		
    	player.sendMessage(langMan.helpMenuHeader);
    	if (commandLines.size() > 0) {
        	for (int i=5*(pageNum-1); i<commandLines.size() && i<(5*pageNum); i++) {
        		player.sendMessage(commandLines.get(i));
        	}
			if (commandLines.size() > 5*pageNum) {
				int nextPageNum = pageNum + 1;
				player.sendMessage(langMan.helpMenuMorePages.replace("{PAGE}", Integer.toString(nextPageNum)));
			}
    	} else {
    		player.sendMessage(langMan.helpMenuNoCommands);
    	}
    	player.sendMessage(langMan.helpMenuFooter);
	}
}