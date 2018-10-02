import java.util.HashSet;

public class EventLog extends HashSet<String> {

	// Adds new events to the log. If event already here, it returns false.
	public boolean addEvent(DataPoint dataPoint) {
		if(contains(dataPoint.eventId)) {
			return false;
		} else {
			add(dataPoint.eventId);
			return true;
		}
	}
	
	// Resets the size of the Event log to the last 100 values
	public void reset() {
		String[] tempLog = toArray(new String[size()]);
		clear();
		for(int i = 100; i>0; i--) {
			add(tempLog[tempLog.length -1 - i]);
		}
	}
}
