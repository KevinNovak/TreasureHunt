package me.kevinnovak.treasurehunt;

public class PermissionManager {
    public String base = "treasurehunt.";
    public String command = "command.";

    public String item = base + "item";
    public String help = base + command + "help";
    public String list = base + command + "list";
    public String top = base + command + "top";
    public String start = base + command + "start";
    public String despawn = base + command + "despawn";

    public PermissionManager() {

    }
}