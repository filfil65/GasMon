import com.google.gson.Gson;

import ch.qos.logback.core.net.SyslogOutputStream;
// Just an intermediate step/object needed to extract the message body, before extracting the sensor location
public class SensorMessageBody {
	public String Message;
	
	public static SensorMessageBody fromJson(String jsonMessage) {
		Gson gson = new Gson();
		return gson.fromJson(jsonMessage, SensorMessageBody.class);
	}
	
	public String toString() {
		return Message;
	}
	
}
