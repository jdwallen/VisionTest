/*******************************************************************
 * IMAGE PROCESSING
 *
 * This is the top level class for image processing.  It reads camera
 * images and invokes one or more target trackers to find objects
 * in the images.  
 * 
 * MUCH OF THIS CLASS IS TBD PENDING INCORPORATION
 * INTO REAL ROBOT CODE.
 *******************************************************************/

package Vision;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs; //v 3.1.0
//import org.opencv.highgui.*; // v 2.4.13

import instrumentation.EventLog;
import instrumentation.EventLog.*;

public class ImageProcessing {

	// Load the OpenCV static library.
	static
	{
		System.out.println(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	}
	
	// Declare a target processor for each target.
	private static CastleTowerGoalTracker castleTowerTgt = 
			new CastleTowerGoalTracker();
	
	public static void processCameraImages () {
		
		Mat cameraImage; // An image from the camera
		
		EventLog.logNormalEvent(NORMALEVENTS.START_PROCESS_CAMERA_IMAGES, "" );
		//int i = ContourTests.computeMomentScore();
		// Get the image from the camera.
		// For now, read a test image.
		// v 2.4.13 cameraImage = Highgui.imread("c:/Users/Jon/Desktop/FRCFieldPhoto.png",Highgui.CV_LOAD_IMAGE_COLOR);

		cameraImage = Imgcodecs.imread("../VisionTest/src/Vision/Images/real_field/0.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);
		
		// Process each type of target.
        TargetTracker.TargetStatus validCastleGoalTgt = castleTowerTgt.trackTarget(cameraImage);

        cameraImage = Imgcodecs.imread("../VisionTest/src/Vision/Images/real_field/205.jpg",Imgcodecs.CV_LOAD_IMAGE_COLOR);
        validCastleGoalTgt = castleTowerTgt.trackTarget(cameraImage);
	}

		//System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		//System.out.println("Build path: " + System.getProperty("java.library.path"));




}
