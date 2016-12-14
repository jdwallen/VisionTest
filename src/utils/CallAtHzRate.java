/******************************************************************************
 * CallAtHzRate
 * 
 * This class allows a method to be called at a lower rate than the the thread
 * that calls it is being executed.  The subclass that implements this class
 * instantiates CallHzRate with a desiredCallRate in Hz, and provides a
 * methodToCall method that calls the desired method.
 *****************************************************************************/

package utils;

public abstract class CallAtHzRate {

	static final int MAIN_THREAD_EXECUTION_RATE_IN_HZ = 50;
	
	private int     m_callsPerInvocation;
	private int     m_callCount   = -1;
	
	// Constructor
	public CallAtHzRate(int desiredCallRate) {
		assert (desiredCallRate < MAIN_THREAD_EXECUTION_RATE_IN_HZ) : "CallAtHzRate: desiredCallRate too high.";
		m_callsPerInvocation = MAIN_THREAD_EXECUTION_RATE_IN_HZ / desiredCallRate;
	}
	
	// This method checks to see if it is time to invoke MethodToCall, and if so,
	// invokes it.
	public void invokeMethod () {
		m_callCount = ++m_callCount % m_callsPerInvocation;
		if (m_callCount == 0) {
			methodToCall();
		}
	}

	// This method causes invokeMethod to immediately invoke methodToCall the
	// next time invokeMethod is called.
	public void reset() {
		m_callCount = -1;
	}
	
	// This subclass-provided method invokes the desired method to be called.
	public abstract void methodToCall();

}
