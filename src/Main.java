import java.util.List;

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

public class Main {
	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		logger.info("Initialise");

		String bucket = "eventprocessing-rfm-sept-2018-locationss3bucket-186b0uzd6cf01";
		String file = "locations.json";

		// Get Amazon S3 Sensor Data
		//AmazonS3Getter.GetSensorData(bucket, file);
		logger.info("Sensor Data fetched successfully from Amazon S3"); 

		// Get Sensor Locations
		Sensor sensor = new Sensor();
		Sensor[] sensors = sensor.getSensors(file);
		logger.info("Sensor Location extracted from json"); 

// @@@@@@@@@@@@@@@@@@@@@@@@@ IN PROGRESS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		ProfileCredentialsProvider myCredentials = new ProfileCredentialsProvider("aws/credentials.txt", "default");
		AmazonSNS sns = AmazonSNSClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(myCredentials).build();
		AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(myCredentials).build();
		logger.info("SNS and SQS builders created"); 

		
		String myTopicArn = "arn:aws:sns:eu-west-1:552908040772:EventProcessing-RFM-Sept-2018-snsTopicSensorDataPart1-PUR0KBORONQF";
		String myQueueUrl = sqs.createQueue(new CreateQueueRequest("queueName")).getQueueUrl();
		logger.info("Topic ARN and queue URL set/created"); 

		
		Topics.subscribeQueue(sns, sqs, myTopicArn, myQueueUrl);
		logger.info("My queue subscribed to topic"); 

		List<Message> messages = sqs.receiveMessage(new ReceiveMessageRequest(myQueueUrl)).getMessages();
		
		if (messages.size() > 0) {
//		    byte[] decodedBytes = Base64.decodeBase64((messages.get(0)).getBody().getBytes());
//		    System.out.println("Message: " +  new String(decodedBytes));
		    System.out.println(messages.get(0).getBody());
		}
		System.out.println();
// @@@@@@@@@@@@@@@@@@@@@@@@@ IN PROGRESS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	}

}
