package me.ryandw11.pixelfriends;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SettingsManager {
	
	private PixelFriends plugin;
	public SettingsManager(PixelFriends plugin) {
		this.plugin = plugin;
	}
	
	public String getDateFormat() {
		return plugin.getConfig().getString("DateFormat");
	}
	
	public String getNameOne() {
		return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("SettingsName1"));
	}
	
	public String getNameTwo() {
		return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("SettingsName2"));
	}
	
	public Object getSetting(Player p, String path) {
		return plugin.data.get(p.getUniqueId() + ".Settings." + path);
	}
	
	public boolean getBoolSetting(Player p, String path) {
		return plugin.data.getBoolean(p.getUniqueId() + ".Settings." + path);
	}
	
	public boolean getBoolSetting(OfflinePlayer p, String path) {
		return plugin.data.getBoolean(p.getUniqueId() + ".Settings." + path);
	}
	
	public void setSetting(Player p, String path, boolean bool) {
		plugin.data.set(p.getUniqueId() + ".Settings." + path, bool);
		plugin.saveFile();
	}
	
	public void setObjectSetting(Player p, String path, Object obj) {
		plugin.data.set(p.getUniqueId() + ".Settings." + path, obj);
		plugin.saveFile();
	}
	
	public void setupSettings(Player p) {
		String s = p.getUniqueId() + ".Settings.";
		if(!plugin.data.contains(s + "teleport"))
			plugin.data.set(s + "teleport", true);
		if(!plugin.data.contains(s + "requests"))
			plugin.data.set(s + "requests", true);
	}
	
	/*
	 * Config file settings.
	 */
	
	public int getMaxFriend() {
		return plugin.getConfig().getInt("MaxFriends");
	}
	
	public int getMaxRequests() {
		return plugin.getConfig().getInt("MaxRequests");
	}

}
