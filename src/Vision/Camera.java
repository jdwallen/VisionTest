/****************************************************************
 * CAMERA
 * 
 * This class defines the attributes of the camera being used for
 * vision processing.  It could be modified to support multiple
 * cameras if need be.
 ****************************************************************/

package Vision;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.image.ColorImage;
//import edu.wpi.first.wpilibj.image.CriteriaCollection;
//import edu.wpi.first.wpilibj.image.NIVision;
//import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Camera {
	
	private Timer      timer;
	private AxisCamera camera;
	private boolean    Initialized = false;
	private ColorImage image2;
	private Mat        image;
	private Relay	   light; // = new Relay(5);
	private boolean	   useCamera = true;
    private final Mat  mat = new Mat();
    VideoCapture       capture;
    
	// For Axis M1013
	static final int   HORIZONTAL_IMAGE_WIDTH  = 640;                // Pixels
	static final int   HORIZONTAL_IMAGE_CENTER = 					 // Pixels
							HORIZONTAL_IMAGE_WIDTH / 2;
	static final int   VERTICAL_IMAGE_WIDTH    = 480;                // Pixels
	static final int   VERTICAL_IMAGE_CENTER   =					 // Pixels
							VERTICAL_IMAGE_WIDTH / 2;
	static final float CAMERA_HORIZONTAL_FOV   = 67.0f;				// Degrees
	static final float CAMERA_HALF_HOR_FOV     =
			                CAMERA_HORIZONTAL_FOV / 2.0f;
	
	static final Scalar RED    = new Scalar(0, 0, 255);
	static final Scalar BLUE   = new Scalar(255, 0, 0);
	static final Scalar GREEN  = new Scalar(0, 255, 0);
	static final Scalar ORANGE = new Scalar(0, 128, 255);
	static final Scalar YELLOW = new Scalar(0, 255, 255);
	static final Scalar PINK   = new Scalar(255, 0,255);
	static final Scalar WHITE  = new Scalar(255, 255, 255);
	/*
#include <stdio.h>
#include <opencv2/opencv.hpp>
#include <iostream>

int main(int, char**) {
    cv::VideoCapture vcap;
    cv::Mat image;

    // This works on a D-Link CDS-932L
    const std::string videoStreamAddress = "http://<username:password>@<ip_address>/video.cgi?.mjpg";

    //open the video stream and make sure it's opened
    if(!vcap.open(videoStreamAddress)) {
        std::cout << "Error opening video stream or file" << std::endl;
        return -1;
    }

    for(;;) {
        if(!vcap.read(image)) {
            std::cout << "No frame" << std::endl;
            cv::waitKey();
        }
        cv::imshow("Output Window", image);
        if(cv::waitKey(1) >= 0) break;
    }  
 }
 */
   //  or
/*     
     #include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>

using namespace cv;
using namespace std;

int main(int argc, char* argv[])
{
    VideoCapture cap; //

    cap.open("http://192.168.226.101:8080/video?x.mjpeg");
    if (!cap.isOpened())  // if not success, exit program
    {
        cout << "Cannot open the video cam" << endl;
        return -1;
    }

   double dWidth = cap.get(CV_CAP_PROP_FRAME_WIDTH); //get the width of frames of the video
   double dHeight = cap.get(CV_CAP_PROP_FRAME_HEIGHT); //get the height of frames of the video

    cout << "Frame size : " << dWidth << " x " << dHeight << endl;

    namedWindow("MyVideo",CV_WINDOW_AUTOSIZE); //create a window called "MyVideo"
    namedWindow("MyNegativeVideo",CV_WINDOW_AUTOSIZE);

    while (1)
    {
        Mat frame;
        Mat contours;

        bool bSuccess = cap.read(frame); // read a new frame from video

         if (!bSuccess) //if not success, break loop
        {
             cout << "Cannot read a frame from video stream" << endl;
             break;
        }

        flip(frame, frame, 1);
        imshow("MyVideo", frame); //show the frame in "MyVideo" window

        Canny(frame, contours, 500, 1000, 5, true);
        imshow("MyNegativeVideo", contours);

        if (waitKey(30) == 27) //wait for 'esc' key press for 30ms. If 'esc' key is pressed, break loop
       {
            cout << "esc key is pressed by user" << endl;
            break;
       }
    }
    return 0;
}
*/
    
	//    also
        
 /*       

The following works for an Axis M1004-W connected to my computer via ethernet cable:

    In the browser of your choice (I'm using Chrome), navigate to the camera's IP address. Provide credentials as necessary.
    You should be looking at a live stream from your camera. Right-click on the video stream and select "Inspect Element" (or its equivalent in non-Chrome browsers).
    You should see a variable called src - this is what you can use within OpenCV to access the camera directly. 
    Mine is /mjpg/video.mjpg, and I bet yours will be similar.

The address you give to OpenCV should look like this:

http://<USERNAME>:<PASSWORD>@<IP_ADDRESS>/<the value of src>

This is what mine looks like:

http://uname:login@192.168.0.0/mjpg/video.mjpg

I entered my address into your code and can see the video stream from an OpenCV window.
*/
	//		or
	
/*
			    # Connect to video Source
//    cam = cv2.VideoCapture()

 //   cam.open("http://10.25.86.11/mjpg/video.mjpg")
 //   if cam.isOpened():
//        print("Camera connection established.")
//    else:
//        print("Failed to connect to the camera.")
//        exit(-1)
//    # Grab frames
//    while(True):
//ret, frame = cam.read() 
*/
	
    //       or
/*     
           			std::string videoStreamAddress = "http://" + struct_ptr->CAMERA_IP +"/mjpg/video.mjpg"; 

			std::cout<<"Trying to connect to Camera stream... at: "<<videoStreamAddress<<std::endl;

			int count = 1;

			//open the video stream and make sure it's opened
			//image settings, resolution and fps are set via axis camera webpage
			while (!vcap.open(videoStreamAddress))
			{

				std::cout << "Error connecting to camera stream, retrying " << count<< std::endl;
				count++;
				usleep(1000000);
}

*/

    public void PreInit() {

    	Timer.SetImplementation(new TimerImpl());
    	timer = new Timer();
    	
        // get an instance of the camera
        //System.out.println("....Delay 60 seconds for AxisCamera startup.");
        //Timer.delay(60.0);
    	System.out.println("Establishing communication with camera...");
    	timer.start();
        camera  = new AxisCamera("10.45.79.11");
        
    	//int ipAddress = (int) (10.0*Math.pow(2.0, 24.0) + 45.0*Math.pow(2.0, 16.0) + 79.0*Math.pow(2.0,  8.0) + 11.0) ;
        //capture = new VideoCapture(ipAddress);
        capture = new VideoCapture();
       
        while (!Initialized) {
        	
            Initialized = capture.open("http://root:roboeagles@10.45.59.11/mjpg/video.mjpg");
        
        }
        
        System.out.println("It took " + timer.get() + " seconds to establish communication.");
        timer.stop();
        timer.reset();
    }

    public void Init() {
        timer.start();
        int imageWidth = 0;
   	    boolean gotImage = false;

        System.out.println("Initializing camera");
        //keep trying to get an image from the camera until we get one or
        //10 seconds has passed
        while ((imageWidth == 0) & (timer.get() < 10.0) & (!gotImage)) {
        	 
        	System.out.print(".");
        	gotImage = capture.read(mat);
        	
/*            try {
                image = camera.getImage();
                imageWidth = image.getWidth();
            } catch (NIVisionException error) {
                
            //} catch (AxisCameraException error) {
            //    System.out.print(".");
            }*/
        }
        System.out.println("");
        imageWidth = mat.width();
        if (imageWidth == 0) {
            System.out.println("......Camera did not initialize in 10 secs");
        } else {
            Initialized = true;
            System.out.println("......Camera image in " + timer.get() + " secs.");
        }
        timer.stop();
        SmartDashboard.putBoolean("Camera?", Initialized);
    }

    public void SetCameraForOps() {
		////After Opening Camera we need to configure the returned image setting
    	////all opencv v4l2 camera controls scale from 0.0 - 1.0
		////vcap.set(CV_CAP_PROP_EXPOSURE_AUTO, 1);
		// CV_CAP_PROP_POS_MSEC Current position of the video file in milliseconds.
		// CV_CAP_PROP_POS_FRAMES 0-based index of the frame to be decoded/captured next.
		// CV_CAP_PROP_POS_AVI_RATIO Relative position of the video file: 0 - start of the film, 1 - end of the film.
		// CV_CAP_PROP_FRAME_WIDTH Width of the frames in the video stream.
		// CV_CAP_PROP_FRAME_HEIGHT Height of the frames in the video stream.
		// CV_CAP_PROP_FPS Frame rate.
		// CV_CAP_PROP_FOURCC 4-character code of codec.
		// CV_CAP_PROP_FRAME_COUNT Number of frames in the video file.
		// CV_CAP_PROP_FORMAT Format of the Mat objects returned by retrieve() .
		// CV_CAP_PROP_MODE Backend-specific value indicating the current capture mode.
		// CV_CAP_PROP_BRIGHTNESS Brightness of the image (only for cameras).
		// CV_CAP_PROP_CONTRAST Contrast of the image (only for cameras).
		// CV_CAP_PROP_SATURATION Saturation of the image (only for cameras).
		// CV_CAP_PROP_HUE Hue of the image (only for cameras).
		// CV_CAP_PROP_GAIN Gain of the image (only for cameras).
		// CV_CAP_PROP_EXPOSURE Exposure (only for cameras).
		// CV_CAP_PROP_CONVERT_RGB Boolean flags indicating whether images should be converted to RGB.
		// CV_CAP_PROP_WHITE_BALANCE_U The U value of the whitebalance setting (note: only supported by DC1394 v 2.x backend currently)
		// CV_CAP_PROP_WHITE_BALANCE_V The V value of the whitebalance setting (note: only supported by DC1394 v 2.x backend currently)
		// CV_CAP_PROP_RECTIFICATION Rectification flag for stereo cameras (note: only supported by DC1394 v 2.x backend currently)
		// CV_CAP_PROP_ISO_SPEED The ISO speed of the camera (note: only supported by DC1394 v 2.x backend currently)
		// CV_CAP_PROP_BUFFERSIZE Amount of frames stored in internal buffer memory (note: only supported by DC1394 v 2.x backend currently)

        camera.writeBrightness(50);
        camera.writeColorLevel(50);
        camera.writeWhiteBalance(AxisCamera.WhiteBalance.kAutomatic);
        lightOn();
    }
    
    private static int toCVImageType(int javaImageType) {
        if (javaImageType == BufferedImage.TYPE_3BYTE_BGR) {
            return CvType.CV_8UC3;
        } else if (javaImageType == BufferedImage.TYPE_BYTE_GRAY) {
            return CvType.CV_8UC1;
        } else {
            return CvType.CV_USRTYPE1;
        }
    }

    private Mat toMat(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();
        int type = image.getType();
        // Get Mat type
        int cvType = toCVImageType(type);
        // If the Mat does not match the BufferedImage, create a new one.
        if (width != mat.width() || height != mat.height() || cvType != image.getType()) {
            mat.create(height, width, cvType);
        }
        // Copy BufferedImage data to Mat
        mat.put(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return mat;
    }

//    private Mat toMat(ColorImage image) {
//
//        int width = image.getWidth();
//        int height = image.getHeight();
//        int type = image.
//        // Get Mat type
//        int cvType = toCVImageType(type);
//        // If the Mat does not match the BufferedImage, create a new one.
//        if (width != mat.width() || height != mat.height() || cvType != image.getType()) {
//            mat.create(height, width, cvType);
//        }
//        // Copy BufferedImage data to Mat
//        mat.put(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
//        return mat;
//    }

    // Needs work.  Convert to Mat.  Return status?
    public Mat getImage (String fileName) {
    	Mat mat = null;
    	ColorImage image;
        try {
            image = camera.getImage();
        } catch (NIVisionException error) {

        }
		return mat;
    }

    
    
    public void lightToggle() {
        if (light.get() == Relay.Value.kOff) {
            light.set(Relay.Value.kForward);
            return;
        }
        if (light.get() == Relay.Value.kForward) {
            light.set(Relay.Value.kOff);
        }
    }

    public void lightOn() {
        light.set(Relay.Value.kForward);
    }


    public void lightOff() {
        light.set(Relay.Value.kOff);
    }
    
 /*   public void fromRobotInit(){
    	
    	//The following lines try to initialize the camera if expected.
    	if (useCamera) {
    		int count = 0;
    		PreInit();
    		while (!Initialized && (count < 6)) {
    			Init();   //Ten seconds max per cycle.
    			count += 1;
    			System.out.println("\n....Camera Loop " + count);
    		}
    		System.out.println("....Camera?: " + Initialized);
    		SmartDashboard.putBoolean("Camera?", Initialized);


    		for (int i = 0; i < 3; i++) {
    			lightOn();
    			Timer.delay(0.5);
    			lightOff();
    			Timer.delay(0.5);
    		}
    		SetCameraForOps();
    	}
    }
*/
    
}
