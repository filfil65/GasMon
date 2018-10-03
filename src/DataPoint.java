import com.google.gson.Gson;

public class DataPoint {
	String locationId;
	String eventId;
	Double value;
	double timestamp;
	
	public static DataPoint getDataEntry(String jsonString){
		Gson gson = new Gson();
		DataPoint dataPoint;
		dataPoint = gson.fromJson(jsonString, DataPoint.class);
		return dataPoint;
	}
}
