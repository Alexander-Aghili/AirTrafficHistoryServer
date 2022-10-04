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

/*
 * Author: Alexander Aghili
 * Copyright 2022 Alexander Aghili
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to 
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of 
 * the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH 
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

//REST API for Air Traffic History
@Path("history")
public class AirTrafficHistoryService 
{
	
	//URL Example
	//http://localhost:8081/AirTrafficHistory/history/getTrafficHistory/37/38.5/-123/-121.5/1651206661/0
	
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
		
		
		/*if (firstTimestamp < Instant.now().getEpochSecond()-3600) 
			return Response.status(400).entity("More than 1 Hour Old Request").build();
		 */
		DatabaseToModel model = new DatabaseToModel();
		AreaBounds areaBounds;
		
		try {
			areaBounds = new AreaBounds(lamin, lamax, lomin, lomax);	
		} catch (AreaOutOfBoundsException e) {
			return Response.status(400).entity("Bound Error").build();
		}
		
		if (lastTimestamp == 0) lastTimestamp = Instant.now().getEpochSecond();
		
		if (firstTimestamp > lastTimestamp) {
			return Response.status(400).entity("Time Error").build();
		}
		
		
		ArrayList<Aircraft> aircraftList = model.getElapsedAreaTraffic(areaBounds, firstTimestamp, lastTimestamp);
		
		return Response.status(201).entity(ModelListToJson.aircraftListToJson(aircraftList).toString()).build();
	}
	
	@GET
	@Path("/test")
	public Response test() {
		return Response.status(201).entity("Test Ok").build();
	}
}
