package me.ryandw11.pixelfriends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.ryandw11.pixelfriends.api.ComponentAddon;
import me.ryandw11.pixelfriends.commands.FriendCommand;
import me.ryandw11.pixelfriends.gui.ConfirmFriendship;
import me.ryandw11.pixelfriends.gui.ConfirmRequest;
import me.ryandw11.pixelfriends.gui.Friend;
import me.ryandw11.pixelfriends.gui.FriendGui;
import me.ryandw11.pixelfriends.gui.RequestGui;
import me.ryandw11.pixelfriends.gui.SettingsGui;
import me.ryandw11.pixelfriends.listener.OnJoin;

public class PixelFriends extends JavaPlugin {
	
	public File datafile = new File(getDataFolder() + "/data/players.yml");
	public FileConfiguration data = YamlConfiguration.loadConfiguration(datafile);
	
	public static List<ComponentAddon> ca = new ArrayList<>();
	public static boolean teleportComponent = true;
	
	public static PixelFriends plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		registerConfig();
		getCommand("friend").setExecutor(new FriendCommand());
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new FriendGui(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ConfirmRequest(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new RequestGui(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ConfirmFriendship(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Friend(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new SettingsGui(), this);
		loadFile();
	}
	
	@Override
	public void onDisable() {
		saveFile();
	}
	
	private void registerConfig() {
		saveDefaultConfig();
	}
	
	/**
     * Save the data file.
     */
    public void saveFile(){
		
		try{
			data.save(datafile);
		}catch(IOException e){
			e.printStackTrace();
			
		}
		
	}
	/**
	 * load the data file
	 */
	public void loadFile(){
		if(datafile.exists()){
			try {
				data.load(datafile);
				
			} catch (IOException | InvalidConfigurationException e) {

				e.printStackTrace();
			}
		}
		else{
			try {
				data.save(datafile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
