package kr.ac.snu.ads.facetracking;

/*******************************************************************************
INTEL CORPORATION PROPRIETARY INFORMATION
This software is supplied under the terms of a license agreement or nondisclosure
agreement with Intel Corporation and may not be copied or disclosed except in
accordance with the terms of that agreement
Copyright(c) 2014 Intel Corporation. All Rights Reserved.

*******************************************************************************/
import intel.rssdk.*;
import java.lang.System.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.*;

public class CameraViewer implements Runnable{    
    static int cWidth  = 640;
    static int cHeight = 480;
    static int dWidth, dHeight;
    static boolean exit = false;
    
    private static void PrintConnectedDevices()
    {
        PXCMSession session = PXCMSession.CreateInstance();
        PXCMSession.ImplDesc desc = new PXCMSession.ImplDesc();        
        PXCMSession.ImplDesc outDesc = new PXCMSession.ImplDesc();        
        desc.group = EnumSet.of(PXCMSession.ImplGroup.IMPL_GROUP_SENSOR);
        desc.subgroup = EnumSet.of(PXCMSession.ImplSubgroup.IMPL_SUBGROUP_VIDEO_CAPTURE);
                
        int numDevices = 0;
        for (int i = 0; ;i++)
        {
            if (session.QueryImpl(desc, i, outDesc).isError())
                break;
            
            PXCMCapture capture = new PXCMCapture();
            if (session.CreateImpl(outDesc, capture).isError())
                continue;
            
            for (int j = 0; ;j++)
            {
                PXCMCapture.DeviceInfo info = new PXCMCapture.DeviceInfo();
                if (capture.QueryDeviceInfo(j, info).isError())
                    break;
                
                System.out.println(info.name);
                numDevices++;
            }
        }
        
        System.out.println("Found " + numDevices + " devices");
    }
    
    public void run(){
        PrintConnectedDevices();
        
        PXCMSenseManager senseMgr = PXCMSenseManager.CreateInstance();        
        
        pxcmStatus sts = senseMgr.EnableStream(PXCMCapture.StreamType.STREAM_TYPE_COLOR, cWidth, cHeight);
        sts = senseMgr.EnableStream(PXCMCapture.StreamType.STREAM_TYPE_DEPTH);

        sts = senseMgr.Init();
        
        System.out.println(sts);
        
        PXCMCapture.Device device = senseMgr.QueryCaptureManager().QueryDevice();
        PXCMCapture.Device.StreamProfileSet profiles = new PXCMCapture.Device.StreamProfileSet();
        device.QueryStreamProfileSet(profiles);
        
        Listener listener = new Listener();
        
        CameraViewer c_raw = new CameraViewer(); 
		DrawFrame c_df = new DrawFrame(cWidth, cHeight);
        JFrame cframe= new JFrame("Intel(R) RealSense(TM) SDK - Color Stream");	
        cframe.addWindowListener(listener);
		cframe.setSize(cWidth, cHeight); 
        cframe.add(c_df);
        cframe.setVisible(true); 
        
        if (sts == pxcmStatus.PXCM_STATUS_NO_ERROR)
        {
            while (listener.exit == false)
            {
                sts = senseMgr.AcquireFrame(true);
                
                if (sts == pxcmStatus.PXCM_STATUS_NO_ERROR)
                {
                 	PXCMCapture.Sample sample = senseMgr.QuerySample();
                    
                    if (sample.color != null)
                    {
    	                PXCMImage.ImageData cData = new PXCMImage.ImageData();                
        	            sts = sample.color.AcquireAccess(PXCMImage.Access.ACCESS_READ,PXCMImage.PixelFormat.PIXEL_FORMAT_RGB32, cData);
            	        if (sts.compareTo(pxcmStatus.PXCM_STATUS_NO_ERROR) < 0)
						{
                	        System.out.println ("Failed to AcquireAccess of color image data");
                    	    System.exit(3);
	                    }
                     
	                    int cBuff[] = new int[cData.pitches[0]/4 * cHeight];
                        
		                cData.ToIntArray(0, cBuff);
	    	            c_df.image.setRGB (0, 0, cWidth, cHeight, cBuff, 0, cData.pitches[0]/4);
	        	        c_df.repaint();  
	           	        sts = sample.color.ReleaseAccess(cData);
						
	              	    if (sts.compareTo(pxcmStatus.PXCM_STATUS_NO_ERROR)<0)
						{
	                    	    System.out.println ("Failed to ReleaseAccess of color image data");
	                        	System.exit(3);
	                    }
					}		               
                }
                else
                {
                    System.out.println("Failed to acquire frame");
                }
                
                senseMgr.ReleaseFrame();
            }
            
            senseMgr.Close();
            System.out.println("Done streaming");
        }
        else
        {
            System.out.println("Failed to initialize");
        }
        
        cframe.dispose();
    }
}

class Listener extends WindowAdapter {
    public boolean exit = false;
	@Override public void windowClosing(WindowEvent e) {
		exit=true;
	}
}

class DrawFrame extends Component { 
    public BufferedImage image; 
    private ArrayList<PXCMRectI32> rectList;
    
    public DrawFrame(int width, int height) { 
       image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);      
    } 
  
    public void paint(Graphics g) { 
    	rectList = VieweeCounter.FaceRectList;
    	
    	((Graphics2D)g).drawImage(image,0,0,null); 
    	((Graphics2D)g).setColor(Color.GREEN);
        ((Graphics2D)g).setStroke(new BasicStroke(3.0f));//line thicker
//        System.out.println ("------- rectListSize: " + rectList.size());
        synchronized(rectList){
        	for(PXCMRectI32 rect : rectList){	 
        		g.drawRect(rect.x, rect.y, rect.w, rect.h);
        	}
        }      
        //clear rects of former frame.
        rectList.clear();       
    }
}