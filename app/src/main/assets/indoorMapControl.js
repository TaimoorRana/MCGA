var map;
var markerGroup;
var pathGroup;

function initmap() {
    map = new L.Map('map', {
        crs: L.CRS.Simple,
        minZoom: -1,
        attributionControl: false, //Remove Attribution on bottom right
        zoomControl: false //Remove "+" and "-" button on top left
    });

    loadMapImage('floormaps/H/4.png');

    //Listener Registration
    map.on('click', function(ev) {
        console.log(JSON.stringify(ev.latlng));
    });
};

function loadMapImage(path) {
    console.log("Called: loadMapImage " + path);
    //Indoor Map
    var bounds = [
        [0, 0],
        [1000, 1000]
    ];
    var imageOptions = {
        interactive: true
    };
    //var image = L.imageOverlay('floormaps/H/4.png', bounds, imageOptions).addTo(map);
    var image = L.imageOverlay(path, bounds, imageOptions).addTo(map);
    map.fitBounds(bounds);
};

function generatePath() {
    markerGroup = L.layerGroup().addTo(map);
    pathGroup = L.layerGroup().addTo(map);

    //Test Path
    var points = [
        L.latLng([467, 131]),
        L.latLng([467, 158]),
        L.latLng([568, 158]),
        L.latLng([568, 455]),
        L.latLng([672, 453]),
        L.latLng([672, 510]),
        L.latLng([642, 510])
    ]

    for (var i = 0; i < points.length; i++) {
        console.log("Adding point: " + JSON.stringify(points[i]));
        markerGroup.addLayer(L.marker(points[i], {
                                       opacity: 0
                                   }));


        /*L.marker(points[i], {
            opacity: 0
        }).addTo(map);*/

        if (!(i == 0)) {
            console.log("i is: " + i);
            pathGroup.addLayer(L.polyline([points[i - 1], points[i]]));
        }
    }
};

function clearLayers() {
    markerGroup.clearLayers();
    pathGroup.clearLayers();
};

$(document).ready(function() {
    initmap();
});