package Vision;

import utils.CallAtHzRate;

public class VisionCaller extends CallAtHzRate {

	VisionCaller(int desiredCallRate) {
		super(desiredCallRate);
	}

	public void methodToCall() {
		ImageProcessing.processCameraImages();
	}

}
