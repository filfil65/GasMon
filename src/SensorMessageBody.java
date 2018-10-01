import com.google.gson.Gson;

import ch.qos.logback.core.net.SyslogOutputStream;

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
