/**********************************************************************
 * EVENT LOG
 * This class allows the coder to leave a trail of time-tagged trail
 * of "breadcrumbs" (events) for debug purposes.
 **********************************************************************/

package instrumentation;

import java.util.Date;
import java.util.*;
import java.text.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EventLogging extends Instrumentation {
	
	// NORMAL events document "normal" execution events, e.g, the beginning and end of major functional
	// flows.
	public enum NORMALEVENTS {
						START_INITIALIZE_COMMAND,   // Outer level command events (NORMAL)
						END_INITIALIZE_COMMAND,
						START_EXECUTE_COMMAND,
						END_EXECUTE_COMMAND,
						START_COMMAND_IS_FINNISHED,
						END_COMMAND_IS_FINNISHED,
						START_COMMAND_END,
						END_COMMAND_END,
						START_COMMAND_INTERRUPTED,
						END_COMMAND_INTERRUPTED,
						START_PROCESS_CAMERA_IMAGES, // Vision processing (NORMAL)
						END_PROCESS_CAMERA_IMAGES,
						PROCESSING_CONTOURS

	};
	
	// INTERESTING events document, as an example, the results of lower-level flows of control.
	public enum INTERESTINGEVENTS {
						
						CONTOUR_REJECTED_TOO_FEW_POINTS, // Vision processing (INTERESTING)
						CONTOUR_FAILED_COVERAGE_AREA_TEST,
						CONTOUR_FAILED_ASPECT_RATIO_TEST,
						CONTOUR_PASSED_COVERAGE_AREA_TEST,
						CONTOUR_PASSED_ASPECT_RATIO_TEST,
						CONTOUR_ACCEPTED_AND_IS_NEW_BEST,
						CONTOUR_ACCEPTED_BUT_NOT_BEST
	};
	
	// BAD events should not happen and should be debugged.
	public enum BADEVENTS {
						TBD
	};

	private boolean logAvailable = false;

	public EventLogging () {

		// Create the directory to hold the events file.
		super();
		logAvailable = instrumentationAvailable();

	}

	// Define the event log.  Event are stored locally to save throughput.  The
	// saveEventLog can be invoked (say, after a match is over) to dump the log
	// to the Driver Station or storage device.
	private List<String> eventLog = new ArrayList<String>(10000);

	// Format string for time and date.
	private final SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss.ssssss");		

	boolean loggingAvailable() {
		return logAvailable;
	}

	// This method logs the specified event onto the eventLog.
	private void logEvent (String event) {

		Date date       = new Date();
		String eventStr = dateFormat.format(date) + ' ' + event;
		eventLog.add(eventStr);

		System.out.println(eventStr);  // Parameterize this?

	}

	// This method logs a NORMAL event.
	public void logNormalEvent (NORMALEVENTS event, String auxData) {

		if (loggingAvailable()) logEvent ("NORMAL      " + event.name() + " " + auxData);

	}

	// This method logs an INTERESTING event.
	public void logInterestingEvent (INTERESTINGEVENTS event, String auxData) {

		if (loggingAvailable()) logEvent ("INTERESTING " + event.name() + " " + auxData);

	}

	// This method logs a BAD event.
	public void logBadEvent (BADEVENTS event, String auxData) {

		if (loggingAvailable()) logEvent ("BAD         " + event.name() + " " + auxData);

	}

	// This method dumps the event log to TBD.
	public void saveEventLog () {

		BufferedWriter bw;
		FileWriter     fw;

		if (loggingAvailable()) {

			// Create a File object with the event log file name.
			File logFile = new File(dataDirectoryName() + "\\Events.txt");

			// if it happens to already exist, delete it.
			if (logFile.exists()) logFile.delete();

			try {

				// if we can create the file
				if (logFile.createNewFile()) {

					// Try to dump it to the file.
					fw = new FileWriter(logFile);
					bw = new BufferedWriter(fw);

					// Write the event log someplace.
					for (String element : eventLog) {

						// On the roboRIO, users should store files to /home/lvuser or subfolders created there. Or,
						// write them to a USB thumbstick at /media/sda1 (or reference /U/ and /V/).  USB stick may have
						// to be FAT-formatted and not "too large".
						bw.write(element);
						bw.write("\r\n");
						System.out.println(element); 
					}

					bw.close();
					fw.close();
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

		// Free up memory.
		eventLog.clear();
	}
}

