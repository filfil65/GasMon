import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class Sensor {
	public Double x;
	public Double y;
	public String id;
	public ArrayList<MinuteRecord> record;
	public Double runningAvg;
	public int noOfMinuteRecords = 0;
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
		} catch (JsonParseException e) {
			System.out.println("Reading json Failed");
			e.printStackTrace();
		}
		return null; // Return Null if file reading failed
	}

	public void deleteOldRecord() {
		// if next record not empty
		if(record.get(0).average != null) {
			valDiff = valOld - record.get(0).average;
			valStatus = valOld > record.get(0).average ? "decreased":"increased" ;
			valOld = record.get(0).average;
			spreadDiff = spreadOld - record.get(0).spread;
			spreadStatus = spreadDiff > 0 ? "decreased" : "increased";
			spreadOld = record.get(0).spread;
			if(noOfMinuteRecords==0) {
				runningAvg = record.get(0).average;
				noOfMinuteRecords++;
			} else {
				runningAvg = (runningAvg * (noOfMinuteRecords - 1) + record.get(0).average)/noOfMinuteRecords;
				noOfMinuteRecords++;
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormat dfSpread = new DecimalFormat("#.####");

		System.out.println("Concentration " + valStatus + " by " + df.format(valDiff) + ". Spread " + spreadStatus
				+ " by " + df.format(spreadDiff) + ".");
		Double average = record.get(0).average != null ? record.get(0).average : 0.0;
		Double spread = record.get(0).spread != null ? record.get(0).spread : 0.0;
		System.out.println("Average Concentration: " + df.format(average) + ". Spread: " + dfSpread.format(spread));
		record.remove(record.get(0));
	}

	public void addToRecord(DataPoint dataPoint) {
		// if new data outside our record
		while (dataPoint.timestamp > record.get(record.size() - 1).endTime) {
			record.add(new MinuteRecord(record.get(record.size() - 1).endTime));
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
