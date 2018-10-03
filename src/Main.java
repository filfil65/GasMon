import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sqs.model.Message;

public class Main {
	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("Initialise");
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
		String bucket = "eventprocessing-rfm-sept-2018-locationss3bucket-186b0uzd6cf01";
		String file = "locations-part2.json";
		long startTime = System.currentTimeMillis();
		long processTime = startTime;

		// Get Amazon S3 Sensor Data
		AmazonS3Getter.GetSensorData(bucket, file);
		logger.info("Sensor Data fetched successfully from Amazon S3");

		// Get Sensor Locations & save sensors to a HashMap<locationKey, Sensor>
		Sensor[] sensors = Sensor.getSensors(file);
		HashMap<String, Sensor> sensorLog = new HashMap<String, Sensor>();
		for (Sensor sensor : sensors) {
			// Create a new array with minuteRecords and put it into the sensor
			ArrayList<MinuteRecord> timeArray = new ArrayList<MinuteRecord>();
			for (int i = -5; i <= 0; i++) {
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
		while (i < 10000) { // i < 500
			messageList = Queue.getMessages();
			logger.info("Messages Received: " + messageList.size());
			if (eventLog.size() > 5000) { // Reset the size of log to last 100 if it gets above 1000
				eventLog.reset();
			}
			// Check time and print results
			long now = System.currentTimeMillis();
			if ((now - startTime) > 60000) {
				startTime = startTime + 60000;
				int j = 1;
				for (Sensor sensor : sensorLog.values()) {
					System.out.println("--------------------\n" + formatter.format(sensor.record.get(0).startTime)
							+ " - Sensor: " + j);
					sensor.deleteOldRecord();
					j++;
				}
			}
			if (messageList.isEmpty()) { // If there are no new messages then continue and hope for new entry
				continue;
			} else { // If there are new messages - process each one of them
				for (Message message : messageList) {
					DataPoint newData = DataPoint
							.getDataEntry(SensorMessageBody.fromJson(message.getBody()).toString());
					if (eventLog.addEvent(newData)) { // If new event was added and valid
						if (sensorLog.containsKey(newData.locationId)) { // If we have a sensor at that location,
							sensorLog.get(newData.locationId).addToRecord(newData); // Add new dataPoint to record
						} else {
							logger.info("Location " + newData.locationId + " is unknown.");
						}
					}
				}
				i++;
				System.out.println(i);
			}

			if ((now - processTime) > 10000) { // process data every 10 sec
				processTime = processTime + 10000;
				AnalyzeData.extractData(sensorLog);
			}
		}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ END OF MAIN LOOP ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		Queue.queueKill();

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
