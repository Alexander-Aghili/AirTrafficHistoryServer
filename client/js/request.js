/*
 * Designed for making all requets
 */

const area =  require('./area.js');
const axios = require('axios');

const { performance } = require('perf_hooks');

const BASE_URL = "http://localhost:8081/AirTrafficHistory/history/getTrafficHistory/";

function getUrl(url) {
    axios.get(url)
        .then(function (response) {
            return response.data;
        })
        .catch(function (error) {
            console.log(error);
        })
        .then(function () {

        });
}


function makeRequest(area, firstTimestamp, secondTimestamp) {
    let url = BASE_URL + area.toUrl() + firstTimestamp + "/" + secondTimestamp;
    const data = getUrl(url);
    console.log(data);
}

const areaBounds = new AreaBounds(37.0,38.5,-123.0,-121.5);
const firstTimestamp = 1651612648;
const lastTimestamp = 0;

makeRequest(areaBounds, firstTimestamp, lastTimestamp);