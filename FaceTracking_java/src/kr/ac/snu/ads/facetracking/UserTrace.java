package kr.ac.snu.ads.facetracking;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserTrace {
	private Date snapDate = null;
	private int userId = -1;
	//face location on scream
	private int x = -1;
	private int y = -1;
	
	public UserTrace(int id) {
		this.userId = id;
		this.snapDate = new Date();			
	}
	
	public UserTrace(int id, Date snapDate, int x, int y) {
		this.userId = id;
		this.snapDate = new Date();
		this.x = x;
		this.y = y;
	}

	//set and get face location.
	public int[] getLocation(){
		int[] axis = new int[2];
		axis[0] = this.x;
		axis[1] = this.y;
		return axis;		
	}
	
	public void setLocation(int[] loca){
		this.x = loca[0];
		this.y = loca[1];	
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public Date getSnapDate() {
		return this.snapDate;		
	}
	
	public void setSanpDate(Date snapDate) {
		this.snapDate = snapDate;		
	}
	
	private String toSimpleTimestamp(Date time) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(time);
		return timeStamp;
	}
	
	public void printStatus() {
		String status = "";
		status = "User " + this.userId + " snapped at " + this.toSimpleTimestamp(this.snapDate);
//			if (state == PresenceState.LEAVED) {
//				status += ", leaved at " + this.toSimpleTimestamp(this.dateLeaved);
//			}
//			System.out.println(status);
	}

	

}
