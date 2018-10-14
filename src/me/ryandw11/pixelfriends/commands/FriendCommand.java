package me.ryandw11.pixelfriends.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.pixelfriends.gui.FriendGui;
import net.md_5.bungee.api.ChatColor;

public class FriendCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("This command is for players only!");
			return true;
		}
		
		Player p = (Player) sender;
		if(!p.hasPermission("pixelfriends.friend")) {
			p.sendMessage(ChatColor.RED + "You do not have permission for this command.");
			return true;
		}
		
		FriendGui fgui = new FriendGui();
		fgui.openMGUI(p, 1);
		return false;
	}

}
