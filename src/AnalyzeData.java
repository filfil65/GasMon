import java.util.ArrayList;
import java.util.HashMap;

public class AnalyzeData {
	//private GraphPoint[] dataMap ;

	public static void extractData(HashMap<String, Sensor> SensorLog){
		int size = 1000; //Size is 1000*1000
		GraphPoint[] point = new GraphPoint[size^2];

		for(Sensor sensor : SensorLog.values()) {
			for(Double row = 0.0 ; row<=size ; row++) { // Go from left to right
				for(Double col = 0.0 ; col<=size ; col++) {
					
				}
				// To do
				// point[0] = new GraphPoint(x,y,z);
			}
		}
	}
}
