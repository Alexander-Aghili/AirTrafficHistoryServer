package com.rest.airtraffic;

import java.time.Instant;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.airtraffic.history.GetTraffic;
import com.airtraffic.history.models.Aircraft;
import com.airtraffic.history.models.AreaBounds;
import com.airtraffic.history.models.ModelListToJson;

//REST API for Air Traffic History
@Path("history")
public class AirTrafficHistoryService 
{
	
	/*
	 * The current structure of the API is that the frontend makes a request for all the data within
	 * a certain timeframe and then the API will collect all the data from the opensky network ASAP
	 * and then return that. This will be slow. Additionally the data is structured by aircraft and
	 * not timestamps. This means that it could be hard to go through the JSON on the frontend for
	 * proper use since it has to go through each aircraft and update it. There are issues with this.
	 * Consider that Aircraft A has a larger timestamp jump(eg. 2 seconds) than Aircraft B(eg 1 second). 
	 * If the frontend just goes by the next timestamp data(without checking timestamp) 
	 * then Aircraft A will be positioned at 1 second in front of Aircraft B, but be displayed at the same time
	 * for the user. Obviously to avoid this, we will just check the timestamp for each aircraft, but this is
	 * very costly. Doing a check for each aircraft's timestamp, especially on the frontend, might end up just being bad.
	 * That is the current plan but I have other implementation ideas that I will explain here.
	 * 
	 * Sending the data based on timestamps. Consider that the interval requested is 1 second. I will request
	 * data for each second. However, Opensky might not give me a second update for each aircraft. Instead, what I
	 * can do is group the AircraftData updates based on timestamp. Instead of sending Aircraft with AircraftData in it,
	 * I could send to the frontend timestamps with updates for each Aircraft. This means the frontend will just have to
	 * go through each timestamp, and for loop through each update for that timestamp. However, this means a lot of
	 * dechipering work for the backend, properly assigning the updates based on timestamps.
	 * 
	 * A further optimization to avoid making unnecessary requests is best explained with an example followed by
	 * the abstract. Lets say I make a request for time=1234. If I get a response for time=1239, then I shall not make
	 * another request for that aircraft until time=1240. This avoids unneccessary requests to opensky. In the abstract, 
	 * if I make a request for a certain time and I get a time that is farther in the future, I know then that I will not
	 * get any new information up to the next interval after that timestamp. This will avoid unneccessary requests.
	 * 
	 * A further optimization to offload the work on the backend is by utilizing streams. Instead of collecting
	 * all of the data and sending it back later, it might be better to send packets of data
	 * (via AKKA or HTTP Long Poll: https://www.youtube.com/watch?v=yqc3PPmHvrA&t=2528s)
	 * or have the frontend request packets of data based on a streamID of some sorts. 
	 * Not only would this reduce load on Opensky servers(and potentially saved me from getting banned)
	 * but also reduce wait time for users. 
	 * (It would act more like a Youtube video where content is sent via multiple HTTP requests)
	 * 
	 * For now I am going to see if my first idea works but if its too slow on the frontend or I find that
	 * I want to attempt a different method or improve it the ideas for optimizations are above.
	 */
	
	
	/*
	 * JSON with Area Bounds and first timestamp, interval , last timestamp (if 0, that is now)
	 * {"bounds": {"lamin":double,"lamax":double,"lomin":double,"lomax":double},
	 *  "firstTimestamp: int, "lastTimestamp": int, "interval": int} 
	 */
	//TODO Finish
	@Path("/GetHistory")
	@POST	
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHistory(String jsonString) {
		JSONObject json = new JSONObject(jsonString);
		
		if (json.getDouble("firstTimestamp") < Instant.now().getEpochSecond()-3600)
			return Response.status(400).entity("More than 1 Hour Old Request").build();
		
		AreaBounds areaBounds = new AreaBounds(json.getJSONObject("bounds"));
		int firstTimestamp = json.getInt("firstTimestamp");
		
		int elapsedTime;
		if (json.getInt("lastTimestamp") == 0) 
			elapsedTime = (int) Instant.now().getEpochSecond() - firstTimestamp;
		else
			elapsedTime = json.getInt("lastTimestamp") - firstTimestamp; 
		
		ArrayList<Aircraft> aircraftList = GetTraffic.getElapsedAreaData(areaBounds, firstTimestamp, json.getInt("interval"), elapsedTime);
		
		
		return Response.status(201).entity(ModelListToJson.aircraftListToJson(aircraftList).toString()).build();
	}
}
