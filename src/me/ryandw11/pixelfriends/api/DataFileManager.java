package me.ryandw11.pixelfriends.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.ryandw11.pixelfriends.PixelFriends;
import me.ryandw11.pixelfriends.api.status.FriendRequest;
import me.ryandw11.pixelfriends.api.status.RequestStatus;

public class DataFileManager {
	private PixelFriends plugin;
	
	public DataFileManager(PixelFriends plugin) {
		this.plugin = plugin;
	}
	
	public boolean playerExists(Player p) {
		if(plugin.data.contains(p.getUniqueId().toString()))
			return true;
		return false;
	}
	
	public void setupPlayer(UUID ud) {
		plugin.data.set(ud + ".Friends", new ArrayList<String>());
		plugin.saveFile();
	}
	
	/**
	 * Check if that request exists.
	 * @param p The player.
	 * @param uuid The request.
	 */
	public boolean hasRequest(UUID p, UUID uuid, RequestStatus status) {
		if(!plugin.data.contains(p + ".Requests." + uuid))
			return false;
		if(plugin.data.getString(p + ".Requests." + uuid).equals(status.toString()))
			return true;
		return false;
	}
	/**
	 * Get all of the requests.
	 * @param p The player that holds the requests
	 * @return A list of said requests.
	 */
	public List<FriendRequest> getRequests(Player p) {
		List<FriendRequest> ls = new ArrayList<>();
		if(!plugin.data.contains(p.getUniqueId() + ".Requests")) {
			return ls;
		}
		for(String sel : plugin.data.getConfigurationSection(p.getUniqueId() + ".Requests").getKeys(false)) {
			if(plugin.data.getString(p.getUniqueId() + ".Requests." + sel).equals(RequestStatus.Sent.toString())) {
				ls.add(new FriendRequest(p.getUniqueId(), UUID.fromString(sel)));
			}else {
				ls.add(new FriendRequest(UUID.fromString(sel), p.getUniqueId()));
			}
		}
		return ls;
	}
	/**
	 * Get all of the request a player has recieved
	 * @param p
	 * @return
	 */
	public List<FriendRequest> getRecievedRequests(Player p){
		List<FriendRequest> ls = new ArrayList<>();
		
		if(!plugin.data.contains(p.getUniqueId() + ".Requests")) {
			return ls;
		}
		
		for(String sel : plugin.data.getConfigurationSection(p.getUniqueId() + ".Requests").getKeys(false)) {
			if(plugin.data.getString(p.getUniqueId() + ".Requests." + sel).equals(RequestStatus.Recieved.toString())) {
				ls.add(new FriendRequest(UUID.fromString(sel), p.getUniqueId()));
			}
		}
		return ls;
	}
	
	/**
	 * Send a request.
	 * @param sender The person sending a request.
	 * @param reciever The person recieving the request.
	 */
	public void sendRequest(Player sender, UUID reciever) {
		plugin.data.set(sender.getUniqueId() + ".Requests." + reciever, RequestStatus.Sent.toString());
		plugin.data.set(reciever + ".Requests." + sender.getUniqueId(), RequestStatus.Recieved.toString());
		plugin.saveFile();
	}
	
	/**
	 * Remove a request
	 * @param sender The person who sent the request.
	 * @param reciever The person who recieved the request.
	 */
	public void removeRequest(UUID sender, UUID reciever) {
		plugin.data.set(sender + ".Requests." + reciever, null);
		plugin.data.set(reciever + ".Requests." + sender, null);
		plugin.saveFile();
	}
	
	
	/**
	 * 
	 * @param sender The player who is getting the friend.
	 * @param reciever The player who has the friend.
	 */
	public void addFriend(UUID sender, UUID reciever) {
		List<String> friends = plugin.data.getStringList(sender + ".Friends");
		if(friends == null)
			friends = new ArrayList<>();
		friends.add(reciever.toString());
		plugin.data.set(sender + ".Friends", friends);
		
		friends = plugin.data.getStringList(reciever + ".Friends");
		if(friends == null)
			friends = new ArrayList<>();
		friends.add(sender.toString());
		plugin.data.set(reciever + ".Friends", friends);
		plugin.saveFile();
	}
	/**
	 * 
	 * @param sender
	 * @param reciever
	 */
	public void removeFriend(UUID sender, UUID reciever) {
		List<String> friends = plugin.data.getStringList(sender + ".Friends");
		if(friends == null)
			friends = new ArrayList<>();
		friends.remove(reciever.toString());
		plugin.data.set(sender + ".Friends", friends);
		
		friends = plugin.data.getStringList(reciever + ".Friends");
		if(friends == null)
			friends = new ArrayList<>();
		friends.remove(sender.toString());
		plugin.data.set(reciever + ".Friends", friends);
		plugin.saveFile();
	}
	
	/**
	 * 
	 * @param person
	 * @param friend
	 * @return
	 */
	public boolean hasFriend(UUID person, UUID friend) {
		if(plugin.data.getStringList(person + ".Friends").contains(friend.toString()))
			return true;
		return false;
	}
}
