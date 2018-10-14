package me.ryandw11.pixelfriends.api.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PixelClickEvent {
	private Player clicker;
	private OfflinePlayer friend;
	public PixelClickEvent(Player clicker, OfflinePlayer friend) {
		this.clicker = clicker;
		this.friend = friend;
	}
	
	public Player getWhoClicked() {
		return clicker;
	}
	
	public OfflinePlayer getFriend() {
		return friend;
	}

}
