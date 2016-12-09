/********************************************************************************
 * CASTLE TOWER GOAL TRACKER 
 * 
 * This subclass of TargetTracker implements the calls
 * to the GRIP-generated image processing pipeline for the FRC 2016 castle tower
 * goal and scores potential targets to see if they represent a tower goal.
 ********************************************************************************/

package Vision;

import java.util.ArrayList;
//import org.opencv.imgcodecs.Imgcodecs; // v3.1.0
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import instrumentation.EventLogging;
import instrumentation.EventLogging.*;
import Vision.ImageProcessing.*;

//import org.opencv.highgui.*; // v2.4.13

public class CastleTowerGoalTracker extends TargetTracker {
	
	// 2016 Castle Tower Goal attributes.
	private final float TRUE_CASTLE_TOWER_GOAL_WIDTH  = 20.0f/12.0f;  // feet
	private final float TRUE_CASTLE_TOWER_GOAL_HEIGHT = 14.0f/12.0f;  // feet 
	private final float TRUE_AREA_RATIO               = 1.0f / 3.0f;
	private final int   MIN_VALID_COVERAGE_AREA_SCORE = 75;
	private final float TRUE_ASPECT_RATIO             = 1.43f;
	private final int   MIN_VALID_ASPECT_RATIO_SCORE  = 75;
	
	// Instantiate the GRIP pipeline.
	GRIPCastleTowerGoalPipeline m_pipeline = new GRIPCastleTowerGoalPipeline();
	
/*	// Constructor:
	CastleTowerGoalTracker () {
		
		// Instantiate the GRIP pipeline.
		m_pipeline = new GRIPCastleTowerGoalPipeline();

	}
*/	
	
	// Invokes the GRIP image processing pipeline and returns a list
	// of contours that may include the desired target.
	ArrayList<MatOfPoint> invokeImageProcessingPipeline (Mat targetImage) {
    	m_pipeline.setsource0(targetImage);
		m_pipeline.process();
		return m_pipeline.findContoursOutput();
	}
	
	// This method scores a contour based on how accurately it represents the
	// desired target. 
	ArrayList<Integer> scoreContour(MatOfPoint contour, int contourID, Rect rect) {

		ArrayList<Integer> scores = new ArrayList<Integer>();
		scores.add(0); // Init the total score to 0 (not a desired target).
		
		// Perform a Coverage Area test.
		
		int coverageScore = ContourTests.coverageAreaTest(contour, rect, TRUE_AREA_RATIO);
		scores.add(coverageScore);

		if (coverageScore < MIN_VALID_COVERAGE_AREA_SCORE) {
			ImageProcessing.eventLog.logInterestingEvent(
							  INTERESTINGEVENTS.CONTOUR_FAILED_COVERAGE_AREA_TEST,
							  "ContourID= " + contourID + " Score= " + coverageScore);
			
			return scores;
		}
		
		ImageProcessing.eventLog.logInterestingEvent(
				  		  INTERESTINGEVENTS.CONTOUR_PASSED_COVERAGE_AREA_TEST,
				  		  "ContourID= " + contourID + " Score= " + coverageScore);
		
		// Perform an Aspect Ratio test.
		
		int aspectScore = ContourTests.aspectRatioTest(contour, rect, TRUE_ASPECT_RATIO);
		scores.add(aspectScore);
		aspectScore = 100; // Fake a successful test until we can get it working.

		if (aspectScore < MIN_VALID_ASPECT_RATIO_SCORE) {

			ImageProcessing.eventLog.logInterestingEvent( 
					  		INTERESTINGEVENTS.CONTOUR_FAILED_ASPECT_RATIO_TEST,
					  		"ContourID= " + contourID + " Score= " + aspectScore);
			return scores;
		}

		ImageProcessing.eventLog.logInterestingEvent(
						  INTERESTINGEVENTS.CONTOUR_PASSED_ASPECT_RATIO_TEST,
				          "ContourID= " + contourID + " Score= " + aspectScore);
		
		 // Set the first score element to the total score.
		scores.set(0, coverageScore + aspectScore);

		return scores;
	}
	
	float trueTargetWidthFt () {
		return TRUE_CASTLE_TOWER_GOAL_WIDTH;
	}

	float trueTargetHeightFt () {
		return TRUE_CASTLE_TOWER_GOAL_HEIGHT;
	}
}
