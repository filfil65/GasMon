import com.google.gson.Gson;

public class DataEntry {
	String locationId;
	String eventId;
	Double value;
	Double timestamp;
	
	public static DataEntry[] getDataEntry(String jsonString){
		Gson gson = new Gson();
		DataEntry[] dataEntry;
		dataEntry = gson.fromJson(jsonString, DataEntry[].class);
		return dataEntry;
	}
}
