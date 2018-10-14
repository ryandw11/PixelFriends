package me.ryandw11.pixelfriends.teleport;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ryandw11.pixelfriends.PixelFriends;
import me.ryandw11.pixelfriends.SettingsManager;
import me.ryandw11.pixelfriends.api.ComponentAddon;
import me.ryandw11.pixelfriends.api.event.PixelClickEvent;
import net.md_5.bungee.api.ChatColor;

public class TeleportComponenet implements ComponentAddon {
	
	private SettingsManager sm;
	public TeleportComponenet() {
		sm = new SettingsManager(PixelFriends.plugin);
	}

	@Override
	public boolean checkConditions(Player active, OfflinePlayer friend) {
		if(!friend.isOnline())
			return false;
		if(!PixelFriends.teleportComponent)
			return false;
		if(!sm.getBoolSetting(Bukkit.getPlayer(friend.getUniqueId()), "teleport"))
			return false;
		return true;
	}

	@Override
	public ItemStack customItem(Player active, OfflinePlayer friend) {
		ItemStack customItem = new ItemStack(Material.EMERALD);
		ItemMeta customMeta = customItem.getItemMeta();
		customMeta.setDisplayName(ChatColor.GREEN + "Teleport to " + friend.getName());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.AQUA + "Click to teleport your friend!");
		customItem.setItemMeta(customMeta);
		return customItem;
	}

	@Override
	public void onClick(PixelClickEvent e) {
		e.getWhoClicked().teleport(Bukkit.getPlayer(e.getFriend().getUniqueId()));
		e.getWhoClicked().sendMessage(ChatColor.GREEN + "Teleported to your friend!");
	}

}
