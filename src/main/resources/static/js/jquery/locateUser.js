function getCoords() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(getCity, noLocation);
    } else {
        noLocation();
    }
}

function noLocation() {
    setLocationData("City", "Country")
}

function getCity(position) {
    return new Promise(function (resolve, reject) {
        var request = new XMLHttpRequest();
        var method = 'GET';
        var url = 'http://maps.googleapis.com/maps/api/geocode/json?latlng=' +
            position.coords.latitude + ',' + position.coords.longitude + '&sensor=true';
        var async = true;
        request.open(method, url, async);
        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                if (request.status == 200) {
                    var data = JSON.parse(request.responseText);
                    var address = data.results[0];
                    resolve(address);
                    console.log(address)
                    var city = address.address_components[3].short_name;
                    var country = address.address_components[4].long_name;
                    setLocationData(city, country);
                }
                else {
                    reject(request.status);
                }
            }
        };
        request.send();
    });
}

function setLocationData(city, country) {
    $("#location").val(city + ", " + country).trigger('input');
}

$(document).ready(function () {
    getCoords();
});
