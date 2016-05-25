package kr.ac.snu.ads.facetracking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VieweesRepository {
	HashMap<Integer, UserPresence> viewees = null;
	FaceDetectingDB db = null;
	private String groupName = null;
	
	public VieweesRepository(String groupName) throws Exception {
		this.viewees = new HashMap<>();
		this.groupName = groupName;
		this.db = new FaceDetectingDB(groupName);
		this.db.createTable(groupName);
	}

	public void update(HashMap<Integer, int[]> idLocaMap) {
		
		// create new viewee object and add to repo if idList have new id
		for (int id : idLocaMap.keySet()) {
			if (this.viewees.containsKey(id) == false) {
				UserPresence presence = new UserPresence(id);
				this.viewees.put(id, presence);
			}
		}
		
		for (int key : this.viewees.keySet()) {
			UserPresence presence = this.viewees.get(key);
			if (idLocaMap.keySet().contains(key) == false) {
				// if current id list doesn't contain previous id,
				// set it leaved when presence is joined before.
				if (presence.getState() == PresenceState.JOINED) {
					presence.setLeaved();
				}
				
			} else {
				// if current id list contain previous id,
				// set it joined again
				presence.setJoined();
				presence.setLocation(idLocaMap.get(key));
			}				
		}
	}
	
	public void printStatus() {
		System.out.println("=============================================");
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("+++++ Updated at " + timeStamp);
		System.out.println("=============================================");
		for (int key : this.viewees.keySet()) {
			UserPresence presence = this.viewees.get(key);
			presence.printStatus();
		}		
	}
	
	public void save() {
		if (this.db != null) {
			this.db.setTable(this.groupName);
			Iterator<Map.Entry<Integer, UserPresence>> iter = this.viewees.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<Integer, UserPresence> entry = iter.next();
			    UserPresence presence = entry.getValue();
			    Date now = new Date();
			    int WAITING_TIMESPAN = 10000;
				if (presence.getState() == PresenceState.LEAVED) {
					// if leaved time passes 10 more secs than now, save it into db.
					if (now.getTime() - presence.getLeavedDate().getTime() >= WAITING_TIMESPAN) {
						this.db.insert(presence);
						System.out.println(presence.getUserId() + " record is saved.");
						iter.remove();
					}
				}
			}						
		}
	}
}
