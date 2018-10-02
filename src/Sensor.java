import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Sensor {
	public Double x;
	public Double y;
	public String id;
	public int DataPointNo = 0;
	public Double average;
	public Double diff;
		
	public static Sensor[] getSensors(String filePath){
		Gson gson = new Gson();
		Sensor[] sensors;
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader reader = new BufferedReader(file);
			String jsonString = reader.lines().collect(Collectors.joining(" "));
			sensors = gson.fromJson(jsonString, Sensor[].class);
			reader.close();
			return sensors;
		}
		catch(IOException e) {
			System.out.println("Reading json Failed");
			e.printStackTrace();
		}
		return null; // Return Null if file reading failed
	}
	
	public void addDataPoint(Double newValue) {
		if(DataPointNo == 0) {
			average = newValue;
			DataPointNo = 1;
		} else {
			DataPointNo++;
			diff = average - (average * (DataPointNo - 1) + newValue)/DataPointNo;
			average = (average * (DataPointNo - 1) + newValue)/DataPointNo;
			//new_average = (old_average * (n-1) + new_value) / n
		}
	}
	
//	public static void average(Double newValue) {
//		
//	}
}
