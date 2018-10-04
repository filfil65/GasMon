import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.maths.Coord3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.Message;
import com.google.gson.stream.MalformedJsonException;

public class Main {
	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		logger.info("Initialise");
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
		String bucket = "eventprocessing-rfm-sept-2018-locationss3bucket-186b0uzd6cf01";
		String file = "locations-part2.json";
//		String file = "locations.json";
		long startTime = System.currentTimeMillis();
		long processTime = startTime;
		LeakMap leakMap = new LeakMap();

		// Get Amazon S3 Sensor Data
		AmazonS3Getter.GetSensorData(bucket, file);
		logger.info("Sensor Data fetched successfully from Amazon S3");

		// Get Sensor Locations & save sensors to a HashMap<locationKey, Sensor>
		Sensor[] sensors = Sensor.getSensors(file);
		HashMap<String, Sensor> sensorLog = new HashMap<String, Sensor>();
		for (Sensor sensor : sensors) {
			// Create a new array with minuteRecords and put it into the sensor
			ArrayList<MinuteRecord> timeArray = new ArrayList<MinuteRecord>();
			for (int i = - 4; i <= 0; i++) { // was -5
				timeArray.add(new MinuteRecord(startTime + (i * 60000)));
			}
			sensor.record = timeArray;
			sensorLog.put(sensor.id, sensor);
		}
		logger.info("Sensor Location extracted from json");

		// Starting new Threads
		// new Main().testRun();

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  MAIN LOOP ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		List<Message> messageList = new ArrayList<Message>();// = new List<Message>();
		SNSandSQS Queue = new SNSandSQS();
		EventLog eventLog = new EventLog();
		int i = 1;
		while (i <= 7000) { // i < 500
			messageList = Queue.getMessages();
//			logger.info("Loop: " + i +". Messages Received: " + messageList.size());
			if(i%100 == 0) logger.info("Loop: " + i);
			if (eventLog.size() > 5000) eventLog.reset(); // Reset the size of log to last 100 if it gets above 1000
			
			// Check time and delete old results and extract the results into a heat map
			long now = System.currentTimeMillis();
			if ((now - startTime) > 60000) {
				startTime = startTime + 60000;
				int j = 1;
				for (Sensor sensor : sensorLog.values()) {
					System.out.println("----------\n" + formatter.format(sensor.record.get(0).startTime)
							+ " - Sensor: " + j);
					sensor.deleteOldRecord();
					j++;
				}
				System.out.println("\n");
				
				// ~~~~~~~~~~ FROM WIP LOOP BELOW ~~~~~~~~~~~~
				leakMap.extractData(sensorLog);

			}
			
			// @@@@@@@@@@@@@@@@@@@@ WIP @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//			if ((now - processTime) > 30000) { // process data every 10 sec
//				processTime = processTime + 30000;
//				leakMap.extractData(sensorLog);
////				for (Sensor sensor : sensorLog.values()) {
////					System.out.println("------------\n" + sensor.runningAvg);
////					System.out.println(sensor.noOfMinuteRecords);
////				}
//			}
			// @@@@@@@@@@@@@@@@@@@@ WIP @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			
			// ################# MAIN SUB-LOOP #####################
			if (messageList.isEmpty()) { // If there are no new messages then continue and hope for new entry
				i++;
				//System.out.println(i);
				continue;
			} else { // If there are new messages - process each one of them
				for (Message message : messageList) {
					try {
						DataPoint newData = DataPoint
								.getDataEntry(SensorMessageBody.fromJson(message.getBody()).toString());
						if (eventLog.addEvent(newData)) { // If new event was added and valid
							if (sensorLog.containsKey(newData.locationId)) { // If we have a sensor at that location,
								sensorLog.get(newData.locationId).addToRecord(newData); // Add new dataPoint to record
							} else {
								// logger.info("Location " + newData.locationId + " is unknown.");
							}
						}
					} catch (Exception e) {
						System.out.println("Reading json Failed");
						e.printStackTrace();
						continue;
					}
				}
			}
			i++;
		}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END OF MAIN WHILE LOOP ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		Queue.queueKill();
		System.out.println(leakMap.Location().x);
		System.out.println(leakMap.Location().y);
		
		// @@@@@@@@ WIP @@@@@@@@@@' 
		// plotting sensor locations
		Coord3d[] sensorLoc = new Coord3d[sensorLog.size()];
//		for(i = 0; i<sensorLog.size(); i++) {
//			Sensor thisSensor = sensorLog.values().
//			sensorLoc[i] = new Coord3d()
//		}
		AnalysisLauncher.open(new DrawGraph(leakMap.leakMap));


	} // End of method

	private void testRun() {
		int threadCount = 2;
		HashMap<Integer, WorkerThread> threads = new HashMap<Integer, WorkerThread>();
		for (int i = 0; i < threadCount; i++) {
			WorkerThread thread = new WorkerThread(i);
			threads.put(i, thread);
		}
		for (Integer threadId : threads.keySet()) {
			threads.get(threadId).start();
		}
	}
} // End of class
