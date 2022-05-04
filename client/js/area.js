

//AreaBounds constructor
function AreaBounds(lomin, lomax, lamin, lamax) {
    this.lomin = lomin;
    this.lomax = lomax;
    this.lamin = lamin;
    this.lamax = lamax;

    //Idk if this works
    this.getLomin = getLomin;//()?
    this.getLomax = getLomax;
    this.getLamin = getLamin;
    this.getLamax = getLamax;

    this.setLomin = setLomin;
    this.setLomax = setLomax;
    this.setLamin = setLamin;
    this.setLamax = setLamax;

}

function getLomin() { return this.lomin; }
function getLomax() { return this.lomax; }
function getLamin() { return this.lamin; }
function getLamax() { return this.lamax; }

function setLomin(lomin) { this.lomin = lomin; }
function setLomax(lomax) { this.lomax = lomax; }
function setLamin(lamin) { this.lamin = lamin; }
function setLamax(lamax) { this.lamax = lamax; }

function toUrl() {
    return lomin + "/" + lomax + "/" + lamin + "/" + lamax + "/";
}