package kr.ac.snu.ads.facetracking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import intel.rssdk.PXCMFaceConfiguration;
import intel.rssdk.PXCMFaceData;
import intel.rssdk.PXCMFaceData.Face;
import intel.rssdk.PXCMFaceData.RecognitionData;
import intel.rssdk.PXCMFaceModule;
import intel.rssdk.PXCMRectI32;
import intel.rssdk.PXCMSenseManager;
import intel.rssdk.pxcmStatus;

public class FaceTrackingThread implements FaceTrackingRunnerInterface {
	private int nFaces = 0; 
	private TraceRepository repo = null;
	
	public void setRepository (TraceRepository repo) {
		this.repo = repo;
	}
	
	public void run() {
        PXCMSenseManager senseMgr = PXCMSenseManager.CreateInstance();
        
        senseMgr.EnableFace(null); // XXX:I couldn't know what is exact argument.
        
        PXCMFaceModule face1 = senseMgr.QueryFace();
                
        // Retrieve the input requirements
        //pxcmStatus sts = pxcmStatus.PXCM_STATUS_DATA_UNAVAILABLE; 
        PXCMFaceConfiguration faceConfig = face1.CreateActiveConfiguration();
        faceConfig.SetTrackingMode(PXCMFaceConfiguration.TrackingModeType.FACE_MODE_COLOR_PLUS_DEPTH); //PXCMFaceConfiguration.TrackingModeType.FACE_MODE_COLOR
        faceConfig.detection.isEnabled = true;
        //faceConfig.landmarks.isEnabled = true; 
        //faceConfig.pose.isEnabled = true;
                
        faceConfig.ApplyChanges();
        faceConfig.Update();
        
        senseMgr.Init();
        
        PXCMFaceData fdata = face1.CreateOutput();
        while (senseMgr.AcquireFrame(true) == pxcmStatus.PXCM_STATUS_NO_ERROR) {
        	
        	// retrieve the face tracking results
        	fdata.Update();
        	
        	int nfaces = fdata.QueryNumberOfDetectedFaces();
        	        	
        	if (nfaces != this.nFaces) {
        
        		HashMap<Integer, int[]> idLocaMapTmp = getIdLocaMap(fdata);
        		
        		if (repo != null) {
            		repo.update(idLocaMapTmp);
            		repo.printStatus();
            		repo.save();
        		} else {
//            		String timeStamp = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(new Date());
//                	System.out.println(nfaces + " faces detected at " + timeStamp);   			
        		}
            	
        		this.nFaces = nfaces;		
        	}     
        
        	// resume next frame processing
        	senseMgr.ReleaseFrame();
        }
        fdata.close();
        
	}


	private HashMap<Integer, int[]> getIdLocaMap(PXCMFaceData fdata) {
		HashMap<Integer, int[]> idLocalMap = new HashMap<Integer, int[]>();
		
		int nfaces = fdata.QueryNumberOfDetectedFaces();       		        		
		for (int i = 0; i < nfaces; i++) {
			Face face = fdata.QueryFaceByIndex(i);
			//recognize the current face?
			int userId = face.QueryUserID();
	
			if(userId >= 0){
				//Do something with the recognized user.
				PXCMRectI32 userRect = getFaceRect(face);
				int[] rectAxis = new int[2];
				rectAxis[0] = userRect.x;
				rectAxis[1] = userRect.y;
				idLocalMap.put(userId, rectAxis);
//				System.out.println ("Top Left corner: (" + rectAxis[0] + "," + rectAxis[1] + ")" ); 
//				userIdList.add(userId);  
			}			
		}
		return idLocalMap;
	}
	
	private PXCMRectI32 getFaceRect(PXCMFaceData.Face face){
        PXCMFaceData.DetectionData detectData = face.QueryDetection(); 
        
        if (detectData != null)
        {
    		PXCMRectI32 rect = new PXCMRectI32();
            boolean ret = detectData.QueryBoundingRect(rect);
            if(ret) return rect;
        }
            
        return null;
	}
} 
