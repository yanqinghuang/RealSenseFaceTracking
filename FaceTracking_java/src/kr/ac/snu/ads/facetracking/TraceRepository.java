package kr.ac.snu.ads.facetracking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TraceRepository {
	HashMap<Integer, UserTrace> traces = null;
	FaceTrackingDB db = null;
	private String groupName = null;
	
	public TraceRepository(String groupName) throws Exception {
		this.traces = new HashMap<>();
		this.groupName = groupName;
		this.db = new FaceTrackingDB(groupName);
		this.db.createTable(groupName);
	}

	public void update(HashMap<Integer, int[]> idLocaMap) {
		// create new  object and add to repo if idList have new id
		for (int id : idLocaMap.keySet()) {
			if (this.traces.containsKey(id) == false) {
				UserTrace trace = new UserTrace(id);
				this.traces.put(id, trace);
			}
		}
		
		for (int key : this.traces.keySet()) {
			UserTrace trace = this.traces.get(key);
			if (idLocaMap.keySet().contains(key) == true) {
				// if current id list contain previous id,
				// set its location
				trace.setLocation(idLocaMap.get(key));
			}				
		}
	}
	
	public void printStatus() {
//		System.out.println("=============================================");
//		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//		System.out.println("  Updated at " + timeStamp);
//		System.out.println("=============================================");
//		for (int key : this.traces.keySet()) {
//			UserTrace trace = this.traces.get(key);
//			trace.printStatus();
//		}		
	}
	
	public void save() {
		if (this.db != null) {
			this.db.setTable(this.groupName);
			Iterator<Map.Entry<Integer, UserTrace>> iter = this.traces.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<Integer, UserTrace> entry = iter.next();
			    UserTrace trace = entry.getValue();
			    Date now = new Date();
				this.db.insert(trace);
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + trace.getUserId() + " trace record is saved.");
				iter.remove();


			}						
		}
	}
}
