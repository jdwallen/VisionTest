/**********************************************************************
 * IMAGE LOGGING
 * This class allows camera images to be saved for future debug.
 **********************************************************************/

package instrumentation;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageLogging {

	// Define the list of images to save.  Images are stored locally to save throughput.  The
	// saveImages can be invoked (say, after a match is over) to dump the images
	// to the Driver Station or storage device.
	private static List<Mat> imageList = new ArrayList<Mat>(10000);
	
	// Save an image to the imageList.
	public static void save (Mat image) {
		imageList.add(image);
	}
	
	// This method dumps the imageList to TBD.
	public static void saveImages () {
		
		// Write the event log someplace.
		for (Mat element : imageList) {
		    Imgcodecs.imwrite("TBD", element);  // Save to Networktables?
		}
		
		// Free up memory.
		imageList.clear();

	}
}
