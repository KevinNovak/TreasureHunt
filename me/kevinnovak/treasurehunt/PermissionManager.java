package me.kevinnovak.treasurehunt;

public class PermissionManager {
	public String base = "treasurehunt.";
	public String command = "command.";
	
	public String item = base + "item";
	public String start = base + command + "start";
	public String top = base + command + "top";
	public String chests = base + command + "chests";
	public String despawn = base + command + "despawn";
	
	public PermissionManager() {
		
	}
}