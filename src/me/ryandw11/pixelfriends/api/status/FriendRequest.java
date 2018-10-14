package me.ryandw11.pixelfriends.api.status;

import java.util.UUID;

import me.ryandw11.pixelfriends.PixelFriends;

public class FriendRequest {
	private UUID sender;
	private UUID reciever;
	private PixelFriends plugin;
	public FriendRequest(UUID sender, UUID reciever) {
		this.sender = sender;
		this.reciever = reciever;
		this.plugin = PixelFriends.plugin;
	}
	
	public UUID getSender() {
		return sender;
	}
	
	public UUID getReciever() {
		return reciever;
	}
	
	public RequestStatus getStatus() {
		return RequestStatus.valueOf(plugin.data.getString(sender + ".Requests." + reciever));
	}
}
