/*****************************************************************************
 * INSTRUMENTATION
 * 
 * This class creates a "runs" data directory into which debug data can be
 * saved for a particular run.  A new folder is created in the "runs"
 * directory to hold the data for a particular run, with the name of
 * the folder containing the date and time the run began.  The inheriting 
 * subclass responsible for writing a particular kind of debug data has access 
 * to this folder path via the dataDirectoryName method.
 *****************************************************************************/

package instrumentation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

public abstract class Instrumentation {

	private static String  runDataDir = new String();
	private static boolean instrAvailable;
	
	// Constructor
	Instrumentation () {
		
		// Has this run's instrumentation directory already been created?
		instrAvailable = !runDataDir.isEmpty();
		
		// if not
		if (!instrAvailable) {

			// Try to create a "runs" directory file object.
			String mainPath = "C:\\users\\Jon\\Documents";
			
			instrAvailable  = true;

			File fileDir1 = new File(mainPath + "\\runs");

			// if the directory doesn't already exist try to create it
			if (!fileDir1.exists()) 
				instrAvailable = fileDir1.mkdir();

			// if the "runs" directory exists
			if (instrAvailable) {

				// Try to create the unique directory for this run
				SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd hh.mm.ss");
				Date date                   = new Date();
				runDataDir                  = fileDir1.getAbsolutePath() + "\\" + dateFormat.format(date);
				File fileDir2               = new File(runDataDir);

				instrAvailable = fileDir2.mkdir();
			}
		}

	}

	// Returns true if runDataDir was successfully created.
	public boolean instrumentationAvailable () {return instrAvailable;}
	
	// Returns the path to where debug data should be saved.
	String dataDirectoryName () {return runDataDir;}
	
	// Force the inheriting class to provide a loggingAvailable method.
	public abstract boolean loggingAvailable();
	
	

}
