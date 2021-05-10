package bptree;

public class Record {

	String dateTime;
	int year;
	String month;
	int mdate;
	String day;
	int time;
	int sensorID;
	String sensorName;
	int hourlyCounts;
	
	public Record(int id, String dateTime, int year, String month, int mdate, String day, int time, int sensorID,
			String sensorName, int hourlyCounts) {
		this.id = id;
		this.dateTime = dateTime;
		this.year = year;
		this.month = month;
		this.mdate = mdate;
		this.day = day;
		this.time = time;
		this.sensorID = sensorID;
		this.sensorName = sensorName;
		this.hourlyCounts = hourlyCounts;
	}
	
	int id;
	public int getId() {
		return id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public int getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public int getMdate() {
		return mdate;
	}

	public String getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}

	public int getSensorID() {
		return sensorID;
	}

	public String getSensorName() {
		return sensorName;
	}

	public int getHourlyCounts() {
		return hourlyCounts;
	}

}
