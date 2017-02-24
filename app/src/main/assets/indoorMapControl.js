var map;

var floormapGroup;
var markerGroup;
var pathGroup;

function initmap() {
	map = new L.Map('map', {
		crs: L.CRS.Simple,
		minZoom: -2,
		maxZoom: 1,
		attributionControl: false, //Remove Attribution on bottom right
		zoomControl: false //Remove "+" and "-" button on top left
	});
	floormapGroup = L.layerGroup().addTo(map);

	//Listener Registration
	map.on('click', function(ev) {
		console.log(JSON.stringify(ev.latlng));
	});
};

function loadMapImage(path) {
	//Indoor Map
	floormapGroup.clearLayers();
    //[2026, 1644]
	var bounds = [
		[0, 0],
		[1989, 2196]
	];
	var imageOptions = {
		interactive: true
	};
	floormapGroup.addLayer(L.imageOverlay(path, bounds, imageOptions));
	map.fitBounds(bounds);
};

function generateWalkablePath(pointArray) {
	markerGroup = L.layerGroup().addTo(map);
	pathGroup = L.layerGroup().addTo(map);

    var points = [];

	for (var i = 0; i < pointArray.length; i++) {
		var pointRaw = pointArray[i];

		var pointLat = 1989 - pointRaw.lat;
		var pointLng = pointRaw.lng;

		var point = L.latLng([pointLat, pointLng]);
		points.push(point);

		console.log("Adding point: " + JSON.stringify(point));

		markerGroup.addLayer(L.marker(points[i], {
			opacity: 0
		}));

		if (!(i == 0)) {
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