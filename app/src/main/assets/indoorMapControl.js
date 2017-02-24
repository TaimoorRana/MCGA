var map;

var floormapGroup;
var markerGroup;
var pathGroup;

function initmap() {
	map = new L.Map('map', {
		crs: L.CRS.Simple,
		minZoom: -1,
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

	var bounds = [
		[0, 0],
		[2026, 1644]
	];
	var imageOptions = {
		interactive: true
	};
	floormapGroup.addLayer(L.imageOverlay(path, bounds, imageOptions));
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

		if (!(i == 0)) {
			pathGroup.addLayer(L.polyline([points[i - 1], points[i]]));
		}
	}
};

function generateWalkablePath(pointArray) {
	markerGroup = L.layerGroup().addTo(map);
	pathGroup = L.layerGroup().addTo(map);

	//Test Path
	/*var points = [
        L.latLng([467, 131]),
        L.latLng([467, 158]),
        L.latLng([568, 158]),
        L.latLng([568, 455]),
        L.latLng([672, 453]),
        L.latLng([672, 510]),
        L.latLng([642, 510])
    ]
*/

    var points = [];

	for (var i = 0; i < pointArray.length; i++) {
		var pointRaw = pointArray[i];

		var pointLat = 1644 - pointRaw.lat;
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