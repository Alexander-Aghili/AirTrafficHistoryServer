package com.rest.airtraffic;

import java.time.Instant;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.airtraffic.history.models.Aircraft;
import com.airtraffic.history.models.AreaBounds;
import com.airtraffic.history.models.AreaOutOfBoundsException;
import com.airtraffic.history.models.ModelListToJson;

//REST API for Air Traffic History
@Path("history")
public class AirTrafficHistoryService 
{
	
	//URL Example
	//http://localhost:8081/AirTrafficHistory/history/getTrafficHistory/37/38.5/-123/-121.5/1651206661/1651206717
	
	@Path("/getTrafficHistory/{lamin}/{lamax}/{lomin}/{lomax}/{firstTimestamp}/{lastTimestamp}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHistory(
			@PathParam("lamin") double lamin,
			@PathParam("lamax") double lamax,
			@PathParam("lomin") double lomin,
			@PathParam("lomax") double lomax,
			@PathParam("firstTimestamp") long firstTimestamp,
			@PathParam("lastTimestamp") long lastTimestamp,
			@PathParam("interval") int interval
		) {
		
		if (firstTimestamp < Instant.now().getEpochSecond()-3600)
			return Response.status(400).entity("More than 1 Hour Old Request").build();

		DatabaseToModel model = new DatabaseToModel();
		AreaBounds areaBounds;
		
		try {
			areaBounds = new AreaBounds(lamin, lamax, lomin, lomax);	
		} catch (AreaOutOfBoundsException e) {
			return Response.status(400).entity("Bound Error").build();
		}
		if (lastTimestamp == 0) lastTimestamp = Instant.now().getEpochSecond() - 5;
		if (firstTimestamp > lastTimestamp) {
			return Response.status(400).entity("Time Error").build();
		}
		
		ArrayList<Aircraft> aircraftList = model.getElapsedAreaTraffic(areaBounds, firstTimestamp, lastTimestamp);
		
		return Response.status(201).entity(ModelListToJson.aircraftListToJson(aircraftList).toString()).build();
	}
	
	@GET
	@Path("/getTrafficHistory")
	public Response test() {
		return Response.status(201).entity("Test Ok").build();
	}
}
