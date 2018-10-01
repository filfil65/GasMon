import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Sensor {
	Double x;
	Double y;
	String id;
	
	public Sensor[] getSensors(String filePath){
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
}
