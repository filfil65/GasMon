import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class SNSandSQS {
	final static Logger logger = LoggerFactory.getLogger(SNSandSQS.class);
	public AmazonSNS sns;
	public AmazonSQS sqs;
	public String myQueueUrl;

	public SNSandSQS(){
		ProfileCredentialsProvider myCredentials = new ProfileCredentialsProvider("aws/credentials.txt", "default");
		AmazonSNS sns = AmazonSNSClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(myCredentials).build();
		AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(myCredentials).build();
		logger.info("SNS and SQS builders created"); 

//		String myTopicArn = "arn:aws:sns:eu-west-1:552908040772:EventProcessing-RFM-Sept-2018-snsTopicSensorDataPart1-PUR0KBORONQF";
		String myTopicArn = "arn:aws:sns:eu-west-1:552908040772:EventProcessing-RFM-Sept-2018-snsTopicSensorDataPart2-Z3K3NB3PRHGH";
		String myQueueUrl = sqs.createQueue(new CreateQueueRequest("FilipG")).getQueueUrl();
		logger.info("Topic ARN and queue URL set/created"); 

		Topics.subscribeQueue(sns, sqs, myTopicArn, myQueueUrl);
		logger.info("My queue subscribed to topic"); 
		
		this.sns = sns;
		this.sqs = sqs;
		this.myQueueUrl = myQueueUrl;
	}
	
	public List<Message> getMessages() throws InterruptedException{
		List<Message> messages = sqs.receiveMessage(new ReceiveMessageRequest(myQueueUrl).withMaxNumberOfMessages(10)).getMessages();
		return messages;
	}
	public void queueKill(){
		sqs.deleteQueue(myQueueUrl);
	}
	
}
