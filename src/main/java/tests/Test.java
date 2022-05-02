package tests;

import java.time.Instant;
import java.util.ArrayList;

import com.airtraffic.history.automated.*;
import com.airtraffic.history.database.DatabaseConnector;
import com.airtraffic.history.models.Aircraft;
import com.airtraffic.history.models.AircraftDataStorage;
import com.airtraffic.history.models.AreaBounds;
import com.rest.airtraffic.DatabaseToModel;

//TODO: Write some Unit tests
public class Test {

	private static AreaBounds area;
	
	public static void main(String[] args) throws InterruptedException {
		area = new AreaBounds(37, 38.5, -123, -121.5);
	}	

}
