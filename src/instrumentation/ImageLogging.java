/**********************************************************************
 * IMAGE LOGGING
 * This class allows camera images to be saved for future debug.
 **********************************************************************/

package instrumentation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageLogging extends Instrumentation {

	private boolean logAvailable = false;
	private final int MAXLISTENTRIES = 10000;
	
	public ImageLogging (){
		super();
	}
	
	// Define the list of images to save.  Images are stored locally to save throughput.  The
	// saveImages can be invoked (say, after a match is over) to dump the images
	// to a storage device.
	private List<Mat>     imageList     = new ArrayList<Mat>(MAXLISTENTRIES);
	
	// Define a list of times at which the image was created.  These are used in the creation
	// of the file name when the image is written.  This time can assist in correlating the
	// image to events in the event log.
	private List<String>  timeList      = new ArrayList<String>(MAXLISTENTRIES);
	
	// Define a list of contour IDs.  These are used in the creation
	// of the file name when the image is written.  This contour ID can assist in correlating the
	// image to events in the event log.
	private List<Integer> contourIDList = new ArrayList<Integer>(MAXLISTENTRIES);
	
	boolean loggingAvailable() {
		return logAvailable;
	}
	
	// Save an image to the imageList.
	public void save (Mat image, int contourID) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat ("hh.mm.ssssss");
		Date date       = new Date();
		imageList.add(image);
		contourIDList.add(contourID);
		timeList.add(dateFormat.format(date));
	}
	
	// This method dumps the imageList to TBD.
	public void saveImageLog () {
		
		// On the roboRIO, users should store files to /home/lvuser or subfolders created there. Or,
		// write them to a USB thumbstick at /media/sda1 (or reference /U/ and /V/).  USB stick may have
		// to be FAT-formatted and not "too large".

		if (loggingAvailable()) {

			// Write the event log someplace.
			for (int i = 0; i < imageList.size(); i++) {
				Imgcodecs.imwrite(dataDirectoryName() + "\\" + i + " " +timeList.get(i) + " " + "C" + contourIDList.get(i) + ".jpg",
						imageList.get(i));
			}

			// Free up memory.
			imageList.clear();
			contourIDList.clear();
			timeList.clear();

		}

	}
	
//	File input = new File("C:\\File\\1.tif");
//
//	BufferedImage image = ImageIO.read(input);
//	// Here we convert into *supported* format
//	BufferedImage imageCopy =
//	    new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//	imageCopy.getGraphics().drawImage(image, 0, 0, null);
//
//	byte[] data = ((DataBufferByte) imageCopy.getRaster().getDataBuffer()).getData();  
//	Mat img = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
//	img.put(0, 0, data);           
//	Imgcodecs.imwrite("C:\\File\\input.jpg", img);
}
