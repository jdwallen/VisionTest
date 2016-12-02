/**********************************************************************
 * EVENT LOG
 * This class allows the coder to leave a trail of time-tagged trail
 * of "breadcrumbs" (events) for debug purposes.
 **********************************************************************/

package instrumentation;

//import edu.wpi.first.wpilibj.

import java.util.Date;
import java.util.*;
//import java.util.*;
import java.text.*;

public class EventLog {
	
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

	// Define the event log.  Event are stored locally to save throughput.  The
	// saveEventLog can be invoked (say, after a match is over) to dump the log
	// to the Driver Station or storage device.
	private static List<String> eventLog = new ArrayList<String>();
	
	// Format string for time and date.
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss.ssssss");		
	
	// This method logs the specified event onto the eventLog.
	private static void logEvent (String event) {
		  
		Date date       = new Date();
		String eventStr = dateFormat.format(date) + ' ' + event;
		eventLog.add(eventStr);
		
		System.out.println(eventStr);  // Parameterize this?

	}
	
	// This method logs a NORMAL event.
	public static void logNormalEvent (NORMALEVENTS event, String auxData) {

		logEvent ("NORMAL      " + event.name() + " " + auxData);

	}
	
	// This method logs an INTERESTING event.
	public static void logInterestingEvent (INTERESTINGEVENTS event, String auxData) {

		logEvent ("INTERESTING " + event.name() + " " + auxData);

	}
	
	// This method logs a BAD event.
	public static void logBadEvent (BADEVENTS event, String auxData) {

		logEvent ("BAD         " + event.name() + " " + auxData);

	}
	
	// This method dumps the event log to TBD.
	public static void saveEventLog () {
		
		// Write the event log someplace.
		for (String element : eventLog) {
		    System.out.println(element);  // Save to Networktables
		}
		
		// Free up memory.
		eventLog.clear();
	}
}
