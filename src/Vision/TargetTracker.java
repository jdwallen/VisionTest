/******************************************************************
 * TARGET TRACKER
 * 
 * This class receives image snapshots from a camera and searches the image
 * for a desired "target".  This may be a FRC game piece, obstacle,
 * or goal.  The abstract methods in this class must be implemented by
 * a subclass that is unique to the type of object being tracked.
 * These methods provide information about the object as well as
 * tests to be run that validate that a potential object is indeed
 * such an object.
 *****************************************************************/

package Vision;

import java.util.ArrayList;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import com.atul.JavaOpenCV.Imshow;
import instrumentation.EventLogging;
import instrumentation.EventLogging.*;
import instrumentation.ImageLogging;


public abstract class TargetTracker {

	// Define the results returned by the trackTarget method.
	public static class TargetStatus {
		
		public final boolean dataValid;
		public final float   tgtRange;   // Feet
		public final float   tgtBearing; // Radians
		
		TargetStatus (boolean valid, float rng, float bearing)  {
			this.dataValid  = valid;
			this.tgtRange   = rng;
			this.tgtBearing = bearing;
		}
	}
	public static final TargetStatus NOTARGETSTATUS = new TargetStatus(false, 0.0f, 0.0f);
	
	// Provide a unique contour ID for each contour that is generated to aid in debug.
	// Make it "static" so that a unique contour is generated no matter how many instances
	// of this class exist.
	private static int uniqueContourID = 0;
	
	// This subclass-supplied method invokes the GRIP image pipeline to get image contours.
	abstract ArrayList<MatOfPoint> invokeImageProcessingPipeline (Mat targetImage);
	
	// This subclass-supplied method returns a list of scores that represents
	// how consistent the supplied contour is compared to the true target. The
	// first score in the list is the total score.  Any other list entries represent 
	// the scores of individual tests, and are returned for display/debug purposes.
	// The higher the value of scores the more consistent it is with the true
	// target. If a 0 total score is returned, the contour was not judged to be a
	// potential target.  "rect" is the bounding rectangle for the contour. countourID uniquely
	// identifies the contour for instrumentation purposes.
	abstract ArrayList<Integer> scoreContour(MatOfPoint contour, int contourID, Rect rect);
	
	// This subclass-supplied method returns the width of the actual target being
	// searched for.
	abstract float trueTargetWidthFt ();
	
	// This subclass-supplied method returns the width of the actual target being
	// searched for.
	abstract float trueTargetHeightFt ();
	
	// This method searches the targetSearchImage for a target that satisfies the
	// criteria as derived from the child class.  If such a target is found, its
	// range and bearing are returned.
	public TargetStatus trackTarget (Mat targetSearchImage) {
		
		final float tanHalfFOV = (float)Math.tan(Math.toRadians(Camera.CAMERA_HALF_HOR_FOV));
		
		// Declare a list of potential target contours.
		ArrayList<MatOfPoint> potTargetContours = new ArrayList<MatOfPoint>();
		
	    // Declare a list of scores.  The first entry is the total score of all tests
		// run on a contour.  Subsequent entries are individual test scores.
		ArrayList<Integer>    scores            = new ArrayList<Integer>();

		Imshow       im                 = new Imshow("Target Contours");
		int	         bestScore          = 0;
		int          bestContourIndex   = 0;
		int			 bestContourID      = 0;
		Rect         bestRect           = new Rect();
		TargetStatus targetStatus;

		// Get a list of contours from the provided image.
		potTargetContours = invokeImageProcessingPipeline(targetSearchImage);

		// Display all the contours.
    	Imgproc.drawContours(targetSearchImage, potTargetContours, -1, Camera.WHITE);

		// For each possible contour, save the index of the contour with the best score (it
    	// most likely matches the desired target).
    	ImageProcessing.eventLog.logNormalEvent(NORMALEVENTS.PROCESSING_CONTOURS, 
    					  		"Num Contours: " + potTargetContours.size());
    	
    	// for each contour
    	for (int i = 0; i < potTargetContours.size(); i++) {
    		
    		// Generate a unique ID for this contour for instrumentation purposes.
    		uniqueContourID++;

    		// Get a contour
    		MatOfPoint contouri = potTargetContours.get(i);

    		// Don't bother with small contours
    		if (contouri.rows() > 50) {

    			// Get the bounding rectangle and score the contour.
    			Rect rect = Imgproc.boundingRect(contouri);
    			
    			// Clear out scores of any previous contour.
    			scores.clear();
    			
    			// Run the tests for a valid contour and get the scores.
    			scores    = scoreContour(contouri, uniqueContourID, rect);
    			
    			// The first entry is the total score.
    			int totalScore = scores.get(0);
    			
				// Draw the rectangle on the debug image.
		    	Point upperLeft  = new Point((double)rect.x, (double)(rect.y));
		    	Point lowerRight = new Point((double)(rect.x + rect.width), (double)(rect.y + rect.height));
		    	Imgproc.rectangle(targetSearchImage, upperLeft, lowerRight, Camera.RED);
		    	
		    	// Annotate the contourID.
		    	Imgproc.putText(targetSearchImage, 
		    					Integer.toString(uniqueContourID), 
		    					new Point(upperLeft.x, upperLeft.y - 5), 
		    					Core.FONT_HERSHEY_SIMPLEX, 
		    					0.5, 
		    					Camera.RED);

		    	// Draw the individual scores down the right edge of the bounding rectangle.
		    	for (int j=1; j < scores.size(); j++)

			    	Imgproc.putText(targetSearchImage, 
			    					Integer.toString(scores.get(j)), 
			    					new Point(lowerRight.x + 5, (upperLeft.y + (j*20))), 
			    					Core.FONT_HERSHEY_SIMPLEX, 
			    					0.5, 
			    					Camera.ORANGE);

    			// If it is a target candidate
    			if (totalScore > 0) {
    				
    				// If it is the best match so far, save it for future processing.
    				if (totalScore > bestScore) {
    					
    					bestRect         = rect.clone();
    					bestScore        = scores.get(0);
    					bestContourIndex = i;
    					bestContourID    = uniqueContourID;
    					
    					ImageProcessing.eventLog.logInterestingEvent
			   				(INTERESTINGEVENTS.CONTOUR_ACCEPTED_AND_IS_NEW_BEST, 
			   				 "Contour ID = " + uniqueContourID + "  Score= " + totalScore);    				}
    				else
    					ImageProcessing.eventLog.logInterestingEvent
			   				(INTERESTINGEVENTS.CONTOUR_ACCEPTED_BUT_NOT_BEST, 
			   				 "Contour ID = " + uniqueContourID + "  Score= " + totalScore);
			

    			}
    		} else
    			ImageProcessing.eventLog.logInterestingEvent
    			   (INTERESTINGEVENTS.CONTOUR_REJECTED_TOO_FEW_POINTS, 
    				"Contour ID = " + uniqueContourID + "  Num Points: " + contouri.rows());
    	}

		
		// if we think we've found the target
		if (bestScore > 0) {

			//
			// Compute range/bearing.  
			//
			
			// First, get the width of the contour via a bounding rectangle.
			float tgtWidth = bestRect.width;
			
			// horizontal dist from center of image to center of target = TrueTgtWidth * TgtHorOffsetFromCenter / TgtPixelWidth 
			int rectCenterX = bestRect.x + (bestRect.width  / 2);
			int rectCenterY = bestRect.y + (bestRect.height / 2);
			float TgtHorDistFromImageCntrFt = 
					trueTargetWidthFt() * (float)(rectCenterX - Camera.HORIZONTAL_IMAGE_CENTER) / tgtWidth;

			// HalfFOVft = trueTgtWidthFt * HalfFOVPixels / tgtWidthPixels
			float halfFOVft = trueTargetWidthFt() * (float)Camera.HORIZONTAL_IMAGE_CENTER / tgtWidth;
			
			// Apparent distance to center of image = halfFOVft / tan(halfFOV)
			float d = halfFOVft / tanHalfFOV;
			
			float tgtRange = (float)Math.sqrt((halfFOVft * halfFOVft) + (d * d));
			
			// bearing = arctan(horizonatal_dist_from_center_of_image / distance)
			
			float tgtBearing = 
					(float)Math.toDegrees(Math.atan((double)(TgtHorDistFromImageCntrFt) / (double)tgtRange));
			
			//
			// Annotate the input image with what we've learned.
			//
			
	    	// Display the best rectangle as GREEN.
	    	Point upperLeft  = new Point((double)bestRect.x, (double)(bestRect.y));
	    	Point lowerRight = new Point((double)(bestRect.x + bestRect.width), (double)(bestRect.y + bestRect.height));
	    	Imgproc.rectangle(targetSearchImage, upperLeft, lowerRight, Camera.GREEN);
	    	
	    	// Put a cross in the center of the best rect.
	    	Imgproc.drawMarker(targetSearchImage, new Point(rectCenterX,rectCenterY), Camera.GREEN);
	    	
	    	// Note the range and bearing
	    	Imgproc.putText(targetSearchImage, 
					"Range  : " + Integer.toString((int)tgtRange) + " ft", 
					new Point(0, 20), 
					Core.FONT_HERSHEY_SIMPLEX, 
					0.5, 
					Camera.GREEN);
	    	
	    	Imgproc.putText(targetSearchImage, 
					"Bearing: " + Integer.toString((int)tgtBearing) + " deg", 
					new Point(0, 40), 
					Core.FONT_HERSHEY_SIMPLEX, 
					0.5, 
					Camera.GREEN);
	    	
	    	// Mark the center of the image.
	    	Imgproc.drawMarker(targetSearchImage, 
	    					   new Point(Camera.HORIZONTAL_IMAGE_CENTER,
	    					   Camera.VERTICAL_IMAGE_CENTER), 
	    					   Camera.ORANGE);
	        
			targetStatus = new TargetStatus(true, tgtRange, tgtBearing);
			
		}
		else 
			targetStatus = NOTARGETSTATUS;
		
		// Display the debug image (or send to driver station?)
		im.showImage(targetSearchImage);
		
		// Save the image for post-run analysis.
		ImageProcessing.imageLog.save (targetSearchImage, bestContourID);

		return targetStatus;
	}

}
