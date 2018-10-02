import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Sensor {
	public Double x;
	public Double y;
	public String id;
	public ArrayList<MinuteRecord> record;
//	public int DataPointNo = 0;
//	public Double average;
//	public Double diff;

	public static Sensor[] getSensors(String filePath) {
		Gson gson = new Gson();
		Sensor[] sensors;
		try {
			FileReader file = new FileReader(filePath);
			BufferedReader reader = new BufferedReader(file);
			String jsonString = reader.lines().collect(Collectors.joining(" "));
			sensors = gson.fromJson(jsonString, Sensor[].class);
			reader.close();
			return sensors;
		} catch (IOException e) {
			System.out.println("Reading json Failed");
			e.printStackTrace();
		}
		return null; // Return Null if file reading failed
	}

	public void addToRecord(DataPoint dataPoint) {
			// if record goes back in time too far then clear old values as we don't need
			if (record.size() > 7) {
				record.remove(record.get(0));
			}
			// if new data outside our record
			while (dataPoint.timestamp > record.get(record.size() - 1).endTime) {
				record.add(new MinuteRecord(record.get(record.size() - 1).endTime));;
			}
			// if record for the minute is available then add
			for (MinuteRecord record : record) {
				if ((dataPoint.timestamp > record.startTime) && (dataPoint.timestamp < record.endTime)) {
					record.addData(dataPoint); // add to that record
					return;
				}
			}
		

		// // If record wasn't added because there isn't such a time span - add a new
		// minute record to the record
		// if(dataPoint.timestamp<)

	}

}
