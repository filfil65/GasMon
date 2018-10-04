//import java.util.ArrayList;
//import java.util.Collections;
import java.util.HashMap;

import org.jzy3d.maths.Coord3d;

public class LeakMap {
	// private int size = 1000; //Size is 1000*1000
	public int size; // Size is 1000*1000
	public Double maxLength;
	public Coord3d[] leakMap;

	public LeakMap() {
		this.size = 1000;
		this.leakMap = new Coord3d[(size * size)];
		this.maxLength = Math.sqrt(Math.pow(size,  2)*2);
	}

	public Coord3d[] extractData(HashMap<String, Sensor> SensorLog) {
		for (Sensor sensor : SensorLog.values()) {
			int i = 0;
			Double k = maxLength;
			
			if(sensor.runningAvg!=null) {
				for (Double row = 0.0; row < size; row++) { // Go from left to right
					for (Double col = 0.0; col < size; col++) {
						Double x = Math.abs(sensor.x - col); // Sensor - square location
						Double y = Math.abs(sensor.y - row);
						Double sensorDistance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
//						Double multiplier = (maxLength - sensorDistance) / maxLength;
//						multiplier = 1 - multiplier;
						
						Double multiplier = 1 + (sensorDistance / maxLength);
						Double sensorInfluence = multiplier * sensor.runningAvg;
						Double influenceRadius = k/sensor.runningAvg;
						
						if (leakMap[i] == null) {
							if(sensorDistance <= influenceRadius) {
								leakMap[i] = new Coord3d(row, col, sensorInfluence);
							} else {
								leakMap[i] = new Coord3d(row, col, 0.0);
							}
						} else if(leakMap[i] != null && sensorDistance <= sensorInfluence) { // not a new value so add them together
							leakMap[i] = new Coord3d(row, col, leakMap[i].z+sensorInfluence);
						} // else if not a new number and not in the sphere of influence
						
//						if(sensorDistance <= sensorInfluence) {
//							if (leakMap[i] == null) {
//								leakMap[i] = new Coord3d(row, col, sensorInfluence);
//							} else {
//								leakMap[i] = new Coord3d(row, col, leakMap[i].z+sensorInfluence);
////										leakMap[i].plus(sensorInfluence);
//							}
//						}
						i++;
					}
				}
			}
		}
		return leakMap;
	}
	
	public Coord3d Location() {
		Coord3d out = leakMap[0];
		for(Coord3d point : leakMap) {
			out = point.z > out.z ? point : out ;
		}
		return out;
	}
}
