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
            console.log(error);
        })
        .then(function () {
            console.log(performance.now() - baseTime);
        });
}


function oneRequest() {
    let url = baseurl + firstTimestamp + '/' + 0;
    getUrl(url);
}