
public class MinuteRecord {
	public Double startTime;
	public Double endTime;
	public int DataPointNo = 0;
	public Double average;
	public Double min;
	public Double max;
	public Double spread;
	
	public MinuteRecord(double startTime) {
		this.startTime = startTime;
		this.endTime = startTime + 60000;
	}

	public void addData(DataPoint dataPoint) {
		if(DataPointNo == 0) { //First Record
			DataPointNo = 1;
			average = dataPoint.value;
			min = dataPoint.value;
			max = dataPoint.value;
			spread = (double) 0;
		} else { // Add Record
			DataPointNo++;
			max = max < dataPoint.value ? dataPoint.value : max;
			min = min > dataPoint.value ? dataPoint.value : min;
			spread = max - min;
			average = (average * (DataPointNo - 1) + dataPoint.value)/DataPointNo;
		}
	}
}
