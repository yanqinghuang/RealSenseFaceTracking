package kr.ac.snu.ads.facetracking;

import java.text.SimpleDateFormat;
import java.util.Date;

enum PresenceState {UNKNOWN, JOINED, LEAVED};  // 
public class UserPresence {
	private Date dateJoined = null;
	private Date dateLeaved = null;
	private int userId = -1;
	private PresenceState state = PresenceState.UNKNOWN;
	//face location on screan
	private int x_axis = -1;
	private int y_axis = -1;
	
	public UserPresence(int id) {
		this.userId = id;
		this.dateJoined = new Date();
		this.state = PresenceState.JOINED;
		
	}
//	public UserPresence(int id, int x, int y) {
//		this.userId = id;
//		this.dateJoined = new Date();
//		this.state = PresenceState.JOINED;
//		this.x_axis = x;
//		this.y_axis = y;
//	}
	
//	public UserPresence(int id, Date dateJoined) {
//		this.userId = id;
//		this.dateJoined = dateJoined;
//		this.state = PresenceState.JOINED;
//		
//	}
	public UserPresence(int id, Date dateJoined, int x, int y) {
		this.userId = id;
		this.dateJoined = dateJoined;
		this.state = PresenceState.JOINED;
		this.x_axis = x;
		this.y_axis = y;
	}
	//set and get face location.
	public int[] getLocation(){
		int[] axis = new int[2];
		axis[0] = this.x_axis;
		axis[1] = this.y_axis;
		return axis;		
	}
	
	public void setLocation(int[] loca){
		this.x_axis = loca[0];
		this.y_axis = loca[1];	
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public PresenceState getState() {
		return this.state;
	}
	
	public void setLeaved() {
		this.dateLeaved = new Date();
		this.state = PresenceState.LEAVED;
	}
	
	public void setJoined() {
		this.state = PresenceState.JOINED;
	}
	
	public Date getJoinedDate() {
		return this.dateJoined;		
	}
	
	public Date getLeavedDate() {
		
		return this.dateLeaved;		
	}
	
	private String toSimpleTimestamp(Date time) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(time);
		return timeStamp;
	}
	
	public void printStatus() {
		String status = "";
		status = "User " + this.userId + " joined at " + this.toSimpleTimestamp(this.dateJoined);
		if (state == PresenceState.LEAVED) {
			status += ", leaved at " + this.toSimpleTimestamp(this.dateLeaved);
		}
		System.out.println(status);
	}

}
