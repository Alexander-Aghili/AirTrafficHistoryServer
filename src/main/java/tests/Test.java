package tests;

import java.time.Instant;
import java.util.ArrayList;

import com.airtraffic.history.GetTraffic;
import com.airtraffic.history.models.Aircraft;
import com.airtraffic.history.models.AreaBounds;

public class Test {

	private static AreaBounds area;
	
	public static void main(String[] args) {
		area = new AreaBounds(41.948265, 42.003904, -87.942113, -87.868632);
		
		//testTimeDifferenceOfLargerDensityRequests();
		makeRequestAndDisplayTimeAsync(30);
	}

	
	private static void testTimeDifferenceOfLargerDensityRequests() {
		int firstTimestamp = (int) Instant.now().getEpochSecond() - 600;
		long time = System.currentTimeMillis();
		for (int i = 1; i < 6; i++) {
			area.changeLengthByFactor(i);
			System.out.println(area.getArea());
			GetTraffic.getElapsedAreaData(area, firstTimestamp, 3, 60); //20 Requests
			System.out.println("Total Time:" + (System.currentTimeMillis() - time));
			time = System.currentTimeMillis();
		}
	}
	
	private static void makeRequestAndDisplayTime(int seconds) {
		int firstTimestamp = (int) Instant.now().getEpochSecond() - seconds;
		long time = System.currentTimeMillis();
		GetTraffic.getElapsedAreaData(area, firstTimestamp, 1, seconds);
		System.out.println(System.currentTimeMillis() - time);
	}
	
	private static void makeRequestAndDisplayTimeAsync(int seconds) {
		int firstTimestamp = (int) Instant.now().getEpochSecond() - seconds;
		long time = System.currentTimeMillis();
		ArrayList<Aircraft> aircraftList=GetTraffic.getElapsedAreaDataAsyncIO(area, firstTimestamp, 1, seconds);
		System.out.println(aircraftList.size());
		System.out.println(System.currentTimeMillis() - time);
		System.exit(0);

	}

}
