
class AreaBounds {
    constructor(lomin, lomax, lamin, lamax) {
        this.lomin = lomin;
        this.lomax = lomax;
        this.lamin = lamin;
        this.lamax = lamax;
    
    }

    get getLomin() { return this.lomin; }
    get getLomax() { return this.lomax; }
    get getLamin() { return this.lamin; }
    get getLamax() { return this.lamax; }

    set setLomin(lomin) { this.lomin = lomin; }
    set setLomax(lomax) { this.lomax = lomax; }
    set setLamin(lamin) { this.lamin = lamin; }
    set setLamax(lamax) { this.lamax = lamax; }

    get url() { return this.toUrl(); }

    toUrl() {
        return this.lomin + "/" + this.lomax + "/" + this.lamin + "/" + this.lamax + "/";
    }
}



const axios = require('axios');
const { performance } = require('perf_hooks');

const BASE_URL = "http://localhost:8081/AirTrafficHistory/history/getTrafficHistory/";

var data;

function getUrl(url) {
    return axios.get(url)
}


function makeRequest(area, firstTimestamp, secondTimestamp) {
    let url = BASE_URL + area.url + firstTimestamp + "/" + secondTimestamp;
    const data = getUrl(url);

    data.then(function (response) {
        
    })
    .catch(function (error) {
        console.log(error);
    })
}

const areaBounds = new AreaBounds(37.0,38.5,-123.0,-121.5);
const firstTimestamp = Math.round((new Date()).getTime() / 1000) - 1800;
const lastTimestamp = 0;

makeRequest(areaBounds, firstTimestamp, lastTimestamp);