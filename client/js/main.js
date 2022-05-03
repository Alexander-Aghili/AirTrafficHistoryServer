/**
 * 
 */
const axios = require('axios');

const { performance } = require('perf_hooks');
  
var firstTimestamp = Math.round((new Date()).getTime() / 1000) - 1800;
const lastTimestamp = Math.round((new Date()).getTime() / 1000);

const baseurl = 'http://localhost:8081/AirTrafficHistory/history/getTrafficHistory/37/38.5/-123/-121.5/';


var baseTime = performance.now();

function getUrl(url) {
    axios.get(url)
        .then(function (response) {
            //console.log(response.data);
        })
        .catch(function (error) {
            console.log(error.status);
        })
        .then(function () {
            console.log(performance.now() - baseTime);
        });
}
const numLength = (lastTimestamp - firstTimestamp);

function doLoop() {
    for (var i = 10; i < numLength; i+=10) {
        url = baseurl + firstTimestamp + '/' + (firstTimestamp + 10);
        getUrl(url);
        firstTimestamp = firstTimestamp + 10;
    }
    
}

function oneRequest() {
    let url = baseurl + firstTimestamp + '/' + 0;
    getUrl(url);
}

for (var k = 0; k < 10; k++) {

    doLoop();
    //oneRequest();
}
