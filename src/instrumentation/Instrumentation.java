package instrumentation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

public abstract class Instrumentation {

	private static String  runDataDir = new String();
	private static boolean instrAvailable;
	
	Instrumentation () {
		
		// Has this run's instrumentation directory already been created?
		instrAvailable = !runDataDir.isEmpty();
		
		// if not
		if (!instrAvailable) {

			String mainPath = "C:\\users\\Jon\\Documents";
			
			SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd hh.mm.ss");
			Date date       = new Date();
			instrAvailable  = true;

			// Create a "runs" directory file object.
			File fileDir1 = new File(mainPath + "\\runs");

			// if the directory doesn't already exist try to create it
			if (!fileDir1.exists()) 
				instrAvailable = fileDir1.mkdir();

			// if the "runs" directory exists
			if (instrAvailable) {

				// Try to create the unique directory for this run
				runDataDir = fileDir1.getAbsolutePath() + "\\" + dateFormat.format(date);

				File fileDir2 = new File(runDataDir);

				instrAvailable = fileDir2.mkdir();


			}
		}

	}

	boolean instrumentationAvailable () {return instrAvailable;}
	
	String dataDirectoryName () {return runDataDir;}
	
	String fileNameNow () {
		SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd hh.mm.ss.ss");
		Date date                   = new Date();
		return dateFormat.format(date);
	}
	
	abstract boolean loggingAvailable();
	
	

}
