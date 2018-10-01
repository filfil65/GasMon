import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class AmazonS3Getter {

	public static void GetSensorData(String bucket, String file) {
		ProfileCredentialsProvider myCredentials = new ProfileCredentialsProvider("aws/credentials.txt", "default");
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(myCredentials).build();
		try {
		    S3Object o = s3.getObject(bucket, file);
		    S3ObjectInputStream s3is = o.getObjectContent();
		    FileOutputStream fos = new FileOutputStream(new File(file));
		    byte[] read_buf = new byte[1024];
		    int read_len = 0;
		    while ((read_len = s3is.read(read_buf)) > 0) {
		        fos.write(read_buf, 0, read_len);
		    }
		    s3is.close();
		    fos.close();
		} catch (AmazonServiceException e) {
		    System.err.println(e.getErrorMessage());
		    System.exit(1);
		} catch (FileNotFoundException e) {
		    System.err.println(e.getMessage());
		    System.exit(1);
		} catch (IOException e) {
		    System.err.println(e.getMessage());
		    System.exit(1);
		}
	}
}
