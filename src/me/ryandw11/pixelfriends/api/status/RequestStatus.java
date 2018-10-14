package me.ryandw11.pixelfriends.api.status;

public enum RequestStatus {
	Sent,
	Recieved;
	
	public RequestStatus fromString(String s) {
		return s == "Sent" ? RequestStatus.Sent : RequestStatus.Recieved;
	}
}
