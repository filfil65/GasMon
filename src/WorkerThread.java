import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerThread extends Thread {
	private int id;
	
	final static Logger logger = LoggerFactory.getLogger(Main.class);

	
	public WorkerThread(int id) {
		this.id = id;
	}
	public void run() {
		logger.info(id + " starting!");
		try {
			Thread.sleep(new Random().nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info(id + " ending!");
	}	
}
