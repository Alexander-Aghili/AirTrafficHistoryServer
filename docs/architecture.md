Describes the current architecure. Right now as I am writing this, the ideas for future architecture ideas are listed in a comment in AirTrafficHistoryService.java.

This is only for the History/Timelapse right now. Future things or other ideas are not listed because they cannot be executed before this foundation is layed so get this working first, 
and solve future things when you have the architecture for the main things down. 

Current Architecture:
 1. Frontend makes a POST request with JSON payload containing AreaBounds(lat/long min and max), firstTimestamp(start time), lastTimestamp(end time), and the interval of requests.
 2. Backend collects data and makes appropriate requests to Opensky for data
 3. Backend then uses this data to format a JSON response with all data for time period requested
 4. Frontend uses data to create whatever scenerio is appropraite
