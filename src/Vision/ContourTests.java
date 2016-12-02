/**************************************************************************
 * CONTOUR TESTS
 *
 * This class is a repository for tests that could be run on a contour to
 * determine if it represents an object to be tracked.
 **************************************************************************/

package Vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class ContourTests {
	
	/*****************************************************************************
	 * This method scales observedValue to a value between 0 and 100, where
	 * 100 corresponds to bestValue.
	 *****************************************************************************/
	public static int compute0to100Score (float observedValue, float bestValue) {
		
		int score;
		
		// Map 0..bestValue to 0..100
		if (observedValue <= bestValue)
			score = (int) (100.0f / bestValue * observedValue);
		else {
			// Map bestValue+ to 100..0.
			score = (int) (-100.0f / bestValue * observedValue) + 200;
			if (score < 0) score = 0;
		}
		return score;
	}
	
	/******************************************************************************
	 * COVERAGE AREA TEST
	 * The Coverage Area score is calculated by comparing the area of the
	 * contour compared to the area of the bounding box drawn around the
	 * particle. This is compared to the trueAreaRatio, which is computed by
	 * taking the ratio of area of the retro-reflective strips around the target
	 * and the area of the bounding box around the target.
	 *****************************************************************************/	
	public static int coverageAreaTest (MatOfPoint contour, Rect rect, float trueAreaRatio){
		
		float boundingBoxArea = (float)rect.area();
		float contourArea     = (float)Imgproc.contourArea(contour,false);
		float areaRatio       = contourArea / boundingBoxArea;
		int   coverageScore	  = compute0to100Score (areaRatio, trueAreaRatio);
		
		return coverageScore;
	}

	/****************************************************************************
	 * ASPECT RATIO TEST
	 * The aspect ratio score is based on (Particle Width / Particle Height).
	 * The width and height of the particle are determined using something
	 * called the "equivalent rectangle". The equivalent rectangle is the
	 * rectangle with side lengths x and y where 2x+2y equals the particle
	 * perimeter and x*y equals the particle area. The equivalent rectangle is
	 * used for the aspect ratio calculation as it is less affected by skewing
	 * of the rectangle than using the bounding box. When using the bounding box
	 * rectangle for aspect ratio, as the rectangle is skewed the height
	 * increases and the width decreases.
	 * 
	 * The target's trueAspectRatio is determined by divided it's bounding
	 * rectangle width by its height. The detected aspect ratio is compared to
	 * this ideal ratio. The aspect ratio score is normalized to return 100 when
	 * the ratio matches the target ratio and drops linearly as the ratio varies
	 * below or above.
	 ****************************************************************************/
	
	public static int aspectRatioTest (MatOfPoint contour, Rect rect, float trueAspectRatio){
		
		// arcLength requires a MatOfPoint2f argument, so convert
		
		MatOfPoint2f dst       = new MatOfPoint2f();
		contour.convertTo(dst, CvType.CV_32F);
		float contourPerimeter = (float)Imgproc.arcLength(dst, true);
		float contourArea      = (float)Imgproc.contourArea(contour,false);

		// Per above, P (perimeter) and A (area) are related as follows:
		//		P = 2x+2y, A = x*y
		// Substituting what we know, and using the quadratic equation, x = .5 * ((P/2) +- sqrt((p*p/4) - (4*A)))
		float sqrRoot = (float)Math.sqrt((double)((contourPerimeter * contourPerimeter / 4.0f) - (4.0 * contourArea)));
		float equivRectX1 = 0.5f * ((contourPerimeter/2.0f) + sqrRoot);
		float equivRectX2 = 0.5f * ((contourPerimeter/2.0f) - sqrRoot);
		float equivRectX  = Math.max(equivRectX1, equivRectX2);
		float equivRectY  = contourArea / equivRectX;
		
		float aspectRatio = equivRectX/equivRectY;
		return compute0to100Score (aspectRatio, trueAspectRatio);
	}
	
	/* Moment

	The moment measurement calculates the particles moment of inertia about it's center of mass. This 
	measurement provides a representation of the pixel distribution in the particle. The ideal score 
	for this test is ~0.28. */
	
	//Moments moments;
	
	//moments = Imgproc.moments(contour);
	
	
    // Calculate center
    // Moments moments = Imgproc.moments(corners);
    //center = new Point(moments.m10 / moments.m00, moments.m01 / moments.m00);
	static int computeMomentScore () {
		
		// This test is in-work.  Trying to figure out which moment to use for the "-.28"
		// based on computing the moments of the tower goal dimensions.
		MatOfPoint2f contour = new MatOfPoint2f();
		List<Point>  points  = new ArrayList<Point>();
		points.add(new Point(0,0));
		points.add(new Point(4,0));
		points.add(new Point(4,10));
		points.add(new Point(16,10));
		points.add(new Point(16,0));
		points.add(new Point(20,0));
		points.add(new Point(20,14));
		points.add(new Point(0,14));
		points.add(new Point(0,0));
		contour.fromList(points);

		Moments moments = Imgproc.moments(contour);
		//Moments moments2 = Imgproc.HuMoments(m, hu);
		
		return 0;
	}
	/* X/Y Profiles
	
	The edge score describes whether the particle matches the appropriate profile in both the X and Y 
	directions. As shown, it is calculated using the row and column averages across the bounding box 
	extracted from the original image and comparing that to a profile mask. The score ranges from 0 
	to 100 based on the number of values within the row or column averages that are between the upper 
	and lower limit values. 
	
	
	*/
	
}
