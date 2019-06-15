package me.kevinnovak.treasurehunt;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandMenu {
    private PermissionManager perm;
    private LanguageManager langMan;

    public CommandMenu(PermissionManager perm, LanguageManager langMan) {
        this.perm = perm;
        this.langMan = langMan;
    }

    public List<String> getAllowedCommandLines(Player player) {
        List<String> commandLines = new ArrayList<String>();

        if (player.hasPermission(perm.help)) {
            commandLines.add(langMan.commandMenuCommandHelp);
        }

        if (player.hasPermission(perm.list)) {
            commandLines.add(langMan.commandMenuCommandList);
        }

        if (player.hasPermission(perm.top)) {
            commandLines.add(langMan.commandMenuCommandTop);
        }

        if (player.hasPermission(perm.start)) {
            commandLines.add(langMan.commandMenuCommandStart);
        }

        if (player.hasPermission(perm.despawn)) {
            commandLines.add(langMan.commandMenuCommandDespawn);
        }

        return commandLines;
    }

    public void print(Player player, int pageNum) {
        List<String> commandLines = this.getAllowedCommandLines(player);

        if (pageNum < 1 || pageNum > Math.ceil((double) commandLines.size() / 5)) {
            pageNum = 1;
        }

        player.sendMessage(langMan.commandMenuHeader);
        if (commandLines.size() > 0) {
            for (int i = 5 * (pageNum - 1); i < commandLines.size() && i < (5 * pageNum); i++) {
                player.sendMessage(commandLines.get(i));
            }
            if (commandLines.size() > 5 * pageNum) {
                int nextPageNum = pageNum + 1;
                player.sendMessage(langMan.commandMenuMorePages.replace("{PAGE}", Integer.toString(nextPageNum)));
            }
        } else {
            player.sendMessage(langMan.commandMenuNoCommands);
        }
        player.sendMessage(langMan.commandMenuFooter);
    }
}