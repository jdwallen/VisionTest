package Vision;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Timer.Interface;
import java.util.Date;

public class TimerImpl implements Timer.StaticInterface {

    public double getFPGATimestamp() { return 0.0;}

    public double getMatchTime() {return 0.0;}

    public void delay(final double seconds) 
    {
    	try {
    		Thread.sleep((long)(seconds*1000.0));
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    public Interface newTimer() {
		return new InterfaceImpl();
	}
    
    private class InterfaceImpl implements Timer.Interface {
    	
    	Date date = new Date();
    	long startTime;
    	long stopTime;
    	boolean running = false;
    	
    	
    	
        /**
         * Get the current time from the timer. If the clock is running it is
         * derived from the current system clock the start time stored in the timer
         * class. If the clock is not running, then return the time when it was last
         * stopped.
         *
         * @return Current time value for this timer in seconds
         */
        public double get() {
        	if (running) return ((double)date.getTime() / 1000.0) - ((double)startTime / 1000.0);
        	else return (double)stopTime / 1000.0;
        }

        /**
         * Reset the timer by setting the time to 0. Make the timer startTime the
         * current time so new requests will be relative now
         */
        public void reset() {
        	startTime = date.getTime();
        }

        /**
         * Start the timer running. Just set the running flag to true indicating
         * that all time requests should be relative to the system clock.
         */
        public void start() {
        	running = true;
        }

        /**
         * Stop the timer. This computes the time as of now and clears the running
         * flag, causing all subsequent time requests to be read from the
         * accumulated time rather than looking at the system clock.
         */
        public void stop() {
        	running = false;
        	stopTime = date.getTime();
        }


        /**
         * Check if the period specified has passed and if it has, advance the start
         * time by that period. This is useful to decide if it's time to do periodic
         * work without drifting later by the time it took to get around to
         * checking.
         *
         * @param period The period to check for (in seconds).
         * @return If the period has passed.
         */
        public boolean hasPeriodPassed(double period) { return false;} // not yet implemented
    }
}
