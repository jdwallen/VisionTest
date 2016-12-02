/****************************************************************
 * CAMERA
 * 
 * This class defines the attributes of the camera being used for
 * vision processing.  It could be modified to support multiple
 * cameras if need be.
 ****************************************************************/

package Vision;

import org.opencv.core.Scalar;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Camera {

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
}
