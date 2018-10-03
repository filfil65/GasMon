import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Sensor {
	public Double x;
	public Double y;
	public String id;
	public ArrayList<MinuteRecord> record;
	public Double valOld = 0.0;
	public Double valDiff = 0.0;
	public String valStatus = "~";
	public Double spreadOld = 0.0;
	public Double spreadDiff = 0.0;
	public String spreadStatus = "~";


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

	public void deleteOldRecord() {
		// if record goes back in time too far then clear old values as we don't need
		if(valOld == 0.0) {
			valOld = record.get(0).average != null ? record.get(0).average : 1 ;
			spreadOld = record.get(0).spread != null ? record.get(0).spread : 1;
		} else {
			valDiff = valOld - record.get(0).average;
			valStatus = valDiff >0 ? "increased": "decreased";
			valOld = record.get(0).average;
			spreadDiff = spreadOld - record.get(0).spread;
			spreadStatus = spreadDiff >0 ? "increased": "decreased";
			spreadOld = record.get(0).spread;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Spread " + spreadStatus + " by " + df.format(spreadDiff) + ". Concentration " + valStatus + " by " + df.format(valDiff) + ".");
		System.out.println("Average Concentration: " + df.format(record.get(0).average) + ". Spread: " + df.format(record.get(0).spread));
		record.remove(record.get(0));
	}
	
	public void addToRecord(DataPoint dataPoint) {
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
	}

}
