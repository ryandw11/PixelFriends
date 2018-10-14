package me.ryandw11.pixelfriends.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.ryandw11.pixelfriends.api.event.PixelClickEvent;

public interface ComponentAddon {
	public boolean checkConditions(Player active, OfflinePlayer friend);
	
	public ItemStack customItem(Player active, OfflinePlayer friend);
	
	public void onClick(PixelClickEvent e);
}
