import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import ch.qos.logback.core.net.SyslogOutputStream;

public class Main {
	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws InterruptedException {
		logger.info("Initialise");

		String bucket = "eventprocessing-rfm-sept-2018-locationss3bucket-186b0uzd6cf01";
		String file = "locations.json";

		// Get Amazon S3 Sensor Data
		AmazonS3Getter.GetSensorData(bucket, file);
		logger.info("Sensor Data fetched successfully from Amazon S3"); 

		// Get Sensor Locations
		Sensor[] sensors = Sensor.getSensors(file);
		logger.info("Sensor Location extracted from json"); 

// @@@@@@@@@@@@@@@@@@@@@@@@@ IN PROGRESS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

		
		List<Message> messageList = new ArrayList<Message>();// = new List<Message>();
		SNSandSQS Queue = new SNSandSQS();
		//ArrayList<DataEntry[]> entryLogger;;
		while(messageList.size() <= 10) {
			messageList.addAll(Queue.getMessages());
			if(messageList.isEmpty()){
				continue;
			}
			else {
			//System.out.println(messageList.get(messageList.size() - 1));
			//Get DataEntries and have a nice list of all of them
			for(Message message : messageList) {
				System.out.println(SensorMessageBody.fromJson(message.getBody()).toString());
				DataEntry.getDataEntry("[" + SensorMessageBody.fromJson(message.getBody()).toString()+"]");
				
				//(DataEntry.getDataEntry("[" + SensorMessageBody.fromJson(message.getBody()).toString()+"]");)
				
				
				//DataEntry[] dataEntry1 = DataEntry.getDataEntry("[" + SensorMessageBody.fromJson(message.getBody()).toString()+"]");

				//System.out.println(DataEntry.getDataEntry("[" + SensorMessageBody.fromJson(message.getBody()).toString()+"]"));

				//messageList.remove(message);
				//dataEntry = DataEntry.getDataEntry("[" + SensorMessageBody.fromJson(message.getBody()).toString()+"]");
				//EntryLogger.add
				
			}
			}
			//messageList.get(0).getBody();
			
		}

//		System.out.println(SensorMessageBody.fromJson(messages.get(0).getBody()));
//		DataEntry[] dataEntry = DataEntry.getDataEntry("[" + SensorMessageBody.fromJson(messages.get(0).getBody()).toString()+"]");
//		System.out.println("dd");
// @@@@@@@@@@@@@@@@@@@@@@@@@ IN PROGRESS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

		//Queue.queueKill();
	}

}
