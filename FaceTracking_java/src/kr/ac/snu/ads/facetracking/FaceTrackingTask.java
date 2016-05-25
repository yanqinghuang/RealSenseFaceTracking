package kr.ac.snu.ads.facetracking;

/*******************************************************************************

INTEL CORPORATION PROPRIETARY INFORMATION
This software is supplied under the terms of a license agreement or nondisclosure
agreement with Intel Corporation and may not be copied or disclosed except in
accordance with the terms of that agreement
Copyright(c) 2014 Intel Corporation. All Rights Reserved.

*******************************************************************************/

import intel.rssdk.PXCMCapture;
import intel.rssdk.PXCMFaceConfiguration;
import intel.rssdk.PXCMFaceData;
import intel.rssdk.PXCMFaceModule;
import intel.rssdk.PXCMRectI32;
import intel.rssdk.PXCMSenseManager;
import intel.rssdk.pxcmStatus;

public class FaceTrackingTask implements FaceDetectingRunnerInterface {
	
	public void run() {
        PXCMSenseManager senseMgr = PXCMSenseManager.CreateInstance();
        
        senseMgr.EnableFace(null);
        
        PXCMFaceModule faceModule = senseMgr.QueryFace();
                
        // Retrieve the input requirements
        pxcmStatus sts = pxcmStatus.PXCM_STATUS_DATA_UNAVAILABLE; 
        PXCMFaceConfiguration faceConfig = faceModule.CreateActiveConfiguration();
        faceConfig.SetTrackingMode(PXCMFaceConfiguration.TrackingModeType.FACE_MODE_COLOR);
        faceConfig.detection.isEnabled = true; 
//        faceConfig.landmarks.isEnabled = true; 
//        faceConfig.pose.isEnabled = true; 
        faceConfig.ApplyChanges();
        faceConfig.Update();
        
        senseMgr.Init();
                
        PXCMFaceData faceData; // = faceModule.CreateOutput();
        
        for (int nframes=0; nframes<30000; nframes++)
        {
            senseMgr.AcquireFrame(true);
            
            PXCMCapture.Sample sample = senseMgr.QueryFaceSample();
            
            faceData = faceModule.CreateOutput();
            faceData.Update();
            
            // Read and print data 
            for (int fidx=0; ; fidx++) {
                PXCMFaceData.Face face = faceData.QueryFaceByIndex(fidx);
                if (face==null) break;
                PXCMFaceData.DetectionData detectData = face.QueryDetection(); 
              
                if (detectData != null)
                {
                    PXCMRectI32 rect = new PXCMRectI32();
                    boolean ret = detectData.QueryBoundingRect(rect);
                    if (ret) {
                 
                        System.out.println ("Detection Rectangle at frame #" + nframes); 
                        System.out.println ("Top Left corner: (" + rect.x + "," + rect.y + ")" ); 
                        System.out.println ("Height: " + rect.h + " Width: " + rect.w); 
                    }
                } else {
                    break;
                }
                //pose data
//                PXCMFaceData.PoseData poseData = face.QueryPose();
//                if (poseData != null)
//                {
//                    PXCMFaceData.PoseEulerAngles pea = new PXCMFaceData.PoseEulerAngles();
//                    poseData.QueryPoseAngles(pea);
//                    System.out.println ("Pose Data at frame #" + nframes); 
//                    System.out.println ("(Roll, Yaw, Pitch) = (" + pea.roll + "," + pea.yaw + "," + pea.pitch + ")"); 
//                }  
            }  
  
            faceData.close();
            senseMgr.ReleaseFrame();
        }
        
        senseMgr.Close();		
	}

	@Override
	public void setRepository(VieweesRepository repo) {
		// XXX: DO NOTHING
		
	}

}
