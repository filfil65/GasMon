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
		// if record goes back in time too far then clear old values as we don't need
		if (valOld == 0.0) { //If there was no record then do
			valOld = record.get(0).average != null ? record.get(0).average : 0.0;
			spreadOld = record.get(0).spread != null ? record.get(0).spread : 0.0;
		} else {
			valDiff = record.get(0).average != null ? valOld - record.get(0).average : 0.01;
			valStatus = valDiff > 0 ? "increased" : "decreased";
			valOld = record.get(0).average;
			spreadDiff = record.get(0).spread != null ? spreadOld - record.get(0).spread : 0.01;
			spreadStatus = spreadDiff > 0 ? "increased" : "decreased";
			spreadOld = record.get(0).spread;
			runningAvg = (runningAvg * (noOfMinuteRecords - 1) + record.get(0).average)/noOfMinuteRecords;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Spread " + spreadStatus + " by " + df.format(spreadDiff) + ". Concentration " + valStatus
				+ " by " + df.format(valDiff) + ".");
		Double average = record.get(0).average != null ? record.get(0).average : 0.0;
		Double spread = record.get(0).spread != null ? record.get(0).spread : 0.0;
		System.out.println("Average Concentration: " + df.format(average) + ". Spread: " + df.format(spread));
		record.remove(record.get(0));
	}

	public void addToRecord(DataPoint dataPoint) {
		// if new data outside our record
		while (dataPoint.timestamp > record.get(record.size() - 1).endTime) {
			record.add(new MinuteRecord(record.get(record.size() - 1).endTime));
			;
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
