package kr.ac.snu.ads.facetracking;


import java.awt.event.*;
import java.awt.image.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import javax.swing.*;

public class FaceTrackingMain {
    public static void main(String s[]) throws java.io.IOException
    {
//    	if (s.length > 0) {
//    		groupName = s[0];
//    	}
    	FaceDetectingRunnerInterface counter =  new VieweeCounter(); 
    	FaceTrackingRunnerInterface tracking = new FaceTrackingThread();
    	ExecutorService es = Executors.newCachedThreadPool();
//    	ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    	
		try {
			VieweesRepository vieweesRepo = new VieweesRepository("viewee");
			TraceRepository traceRepo = new TraceRepository("trace");
	    	counter.setRepository(vieweesRepo);
	    	tracking.setRepository(traceRepo);
	    	//MutiThreads executing cameraViewer and faces detection;	    	
    	
	    	es.execute(new CameraViewer());
	    	es.execute(counter); 
//	    	service.scheduleAtFixedRate(tracking, 0, 500, TimeUnit.MILLISECONDS);
	    	System.out.println("Main ends!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.exit(0);	
    } 
} 

