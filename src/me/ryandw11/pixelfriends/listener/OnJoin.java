package me.ryandw11.pixelfriends.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ryandw11.pixelfriends.PixelFriends;
import me.ryandw11.pixelfriends.SettingsManager;
import me.ryandw11.pixelfriends.api.DataFileManager;

public class OnJoin implements Listener {
	private PixelFriends plugin;
	private DataFileManager dm;
	private SettingsManager sm;
	public OnJoin() {
		this.plugin = PixelFriends.plugin;
		dm = new DataFileManager(plugin);
		sm = new SettingsManager(plugin);
	}
	@EventHandler
	public void joinEvent(PlayerJoinEvent e) {
		if(!dm.playerExists(e.getPlayer())) {
			dm.setupPlayer(e.getPlayer().getUniqueId());
		}
		sm.setupSettings(e.getPlayer());
	}

}
