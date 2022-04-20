Describes the current architecure. Right now as I am writing this, the ideas for future architecture ideas are listed in a comment in AirTrafficHistoryService.java.

This is only for the History/Timelapse right now. Future things or other ideas are not listed because they cannot be executed before this foundation is layed so get this working first, 
and solve future things when you have the architecture for the main things down. 

Current Architecture:
 1. Frontend makes a POST request with JSON payload containing AreaBounds(lat/long min and max), firstTimestamp(start time), lastTimestamp(end time), and the interval of requests.
 2. Backend collects data and makes appropriate requests to Opensky for data
 3. Backend then uses this data to format a JSON response with all data for time period requested
 4. Frontend uses data to create whatever scenerio is appropraite

 
Okay so I realized that the above architecture is not going to work at all. It isn't even my code that is causing the bottleneck, but instead its limited by either
a) getting responses from Opensky for each request
or
b) being limited in my request timeframe as to reduce load on opensky servers

I collected some data on the request times. The original data can be found in a folder named data with a file name RequestResponseTime.txt Based on a sample size of 76 successive requests, I determined that there was a mean time of 643.39 milliseconds with a standard deviation of 232.38 milliseconds. The median was 585.5 and an IQR of 659. The 95% confidence interval turns out to be {590.29, 696.5} (I did not do any checking of conditionals). 

To show the damage that this does I must show what some example requests might look like.

For the sake of example, lets say you just wanted 3 requests. A 5 minute timelapse with 15 second intervals. A 30 minute one with 5 second intervals. And an hour timelapse with 1 second intervals. 

The 5 minute time lapse with 15 second intervals comes out to 20 requests. That means in order from least to sample mean to most conservative with the timing(in milliseconds), {11806, 12868, 13930} which in seconds is {11.806, 12.868, 13.93} respectively, and in minutes is {0.197, 0.214, 0.232} respectively. While this doesn't look terrible lets see how it scales. (This doesn't even mention the timeout, which can be avoided by making multiple small requests from the frontend but this brings the issue of my client-server latency, all of this will be addressed later)

Now am I going to look at the data for the rest of my requests and probably cry a little. 

Same format as above: {least conservative, sample mean, most conservative} in ms, then s, then m.

For 30 min with 5 second:
360 requests to Opensky
{212504, 231620, 250740}, {212.504, 231.62, 250.74}, and {3.54, 3.86, 4.18}
Ouch. This could mean more than 4 minutes to process a request of 30 minutes.

For 1 hour with 1 second requets:
3600 requests
{2125044, 2316204, 2507400}, {2125.044, 2316.204, 2507.4}, and {35.42, 38.6, 41.79}

As you can see, this is really bad. Even with a stream you couldn't run a timelapse of more than like 1.4x(Rough estimate) and definitely not 2x. Needing almost 42 minutes for a single person to generate a timelapse (which occupies a whole thread on my backend). As you can see this doesn't scale at all. In like any meaningful manner or capacity. 

All of this doesn't account for the density of the traffic, since my sample used the same density of traffic, area, or other factors. As you can see this is quite the disaster so I will have to come up with some work arounds and solutions. Right now nothing much comes to mind other than possibly building a stream with the front end but as mentioned that just mihgt not work either.

I am not quite sure how I will solve this but Ill figure it out hopefully.

4/12/21
I came up with a possible solution. I find it likely I will have to implement several fixes simultaneously to make this operational. I also think in the long term my own storage will be more effective for timing and just update my db every second in real time, but that is for the future.

For now, I believe that most effetive method of reducing requests is using 3D linear interpolation. I will take two points/AircraftData objects. Then I will extract new points/AircraftData objects based on the current points. This will include locations based on speed(and change in speed) and altitude based on vertical rate(and change in vertical rate). This could cause issues with regard to turns, which I will have to work out. I will collect data based on how accurate the interpolation is for the different metrics and base how long the interpolation should be to reduce requests. 

My dad also suggested making simultaneous requests which might actually work if the library I have offers the ability to perform multiple HTTP requests simultaneously. This will ensure that each request doesn't have to wait for the data to come in and really just be better so I will look into that.


I have decided to scratch the whole request thing in real time and just store the data in my own databases. It has many advantages described in decisions.md

