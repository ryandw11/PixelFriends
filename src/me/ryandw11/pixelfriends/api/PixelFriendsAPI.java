package me.ryandw11.pixelfriends.api;

import me.ryandw11.pixelfriends.PixelFriends;

public class PixelFriendsAPI {
	
	private PixelFriends plugin;
	public PixelFriendsAPI() {
		this.plugin = PixelFriends.plugin;
	}
	
	/**
	 * Add a componenet to the plugin.
	 * @param ca A reference to your class
	 */
	public void declareAddon(ComponentAddon ca) {
		PixelFriends.ca.add(ca);
	}
	
	/**
	 * Disable the teleport function.
	 * @param bool true or false
	 */
	public void disableTeleport(boolean bool) {
		PixelFriends.teleportComponent = bool;
	}

}
