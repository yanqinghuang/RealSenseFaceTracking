package kr.ac.snu.ads.facetracking.test;

import static org.junit.Assert.*;

import org.junit.Test;

import kr.ac.snu.ads.facetracking.FaceDetectingDB;
import kr.ac.snu.ads.facetracking.UserPresence;

public class SqliteTest {

	@Test
	public void testCreateTable() {
		try {
			FaceDetectingDB db = new FaceDetectingDB("test");
			assertTrue(db.createTable("test"));			
		
		} catch (Exception ex) {
			fail("Exception is thrown. " + ex.getMessage());
		}
	}

	@Test
	public void testInsert() {
		try {
			FaceDetectingDB db = new FaceDetectingDB("test");
			db.setTable("test");
			UserPresence up = new UserPresence(0);
			up.setLeaved();
			
			assertTrue(db.insert(up));			
		
		} catch (Exception ex) {
			fail("Exception is thrown. " + ex.getMessage());
		}		
	}

}
