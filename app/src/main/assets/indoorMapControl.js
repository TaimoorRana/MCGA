//Leaflet Map Variable
var map;

//Path and Marker Groups
var floormapGroup;
var roomMarkerGroup;
var pathMarkerGroup;
var polygonGroup;
var pathGroup;

//Lat and Lng Bounds For Currently Loaded Map
var curLatBound;
var curLngBound;

function initmap() {
	map = new L.Map('map', {
		crs: L.CRS.Simple,
		minZoom: -2,
		maxZoom: 1,
		attributionControl: false, //Remove Attribution on bottom right
		zoomControl: false //Remove "+" and "-" button on top left
	});
	//Image Groups
	floormapGroup = L.layerGroup().addTo(map);

	//Path Groups
	pathGroup = L.layerGroup().addTo(map);

	//Marker Groups
	roomMarkerGroup = L.layerGroup().addTo(map);
	pathMarkerGroup = L.layerGroup().addTo(map);

	//Polygon Groups
	polygonGroup = L.layerGroup().addTo(map);

	//Listener Registration
	map.on('click', function(ev) {
		console.log(JSON.stringify(ev.latlng));
	});
}

//Map Loaders
function loadMapImage(path, latBound, lngBound) {
	//Indoor Map
	floormapGroup.clearLayers();

	curLatBound = latBound;
	curLngBound = lngBound;

	var bounds = [
		[0, 0],
		[curLatBound, curLngBound]
	];
	var imageOptions = {
		interactive: true
	};
	floormapGroup.addLayer(L.imageOverlay(path, bounds, imageOptions));
	map.fitBounds(bounds);
}

function loadMap(mapId) {
	clearAllLayers();
	switch (mapId) {
		case "H1":
			loadMapImage('floormaps/H/1.png', 2643, 2823);
			break;
		case "H2":
            loadMapImage('floormaps/H/2.png', 2356, 2604);
            break;
		case "H4":
			loadMapImage('floormaps/H/4.png', 1989, 2196);
			break;
	}
}

//Path Drawing
function drawWalkablePath(pointArray) {
	var points = [];

	for (var i = 0; i < pointArray.length; i++) {
		var pointRaw = pointArray[i];

        //Convert coordinates so that origin (0,0) is at the bottom left rather than on the top left
		var pointLat = curLatBound - pointRaw.lat;
		var pointLng = pointRaw.lng;

		var point = L.latLng([pointLat, pointLng]);
		points.push(point);

		console.log("Adding point: " + JSON.stringify(point));

		pathMarkerGroup.addLayer(L.marker(points[i], {
			opacity: 0
		}));

		if (!(i == 0)) {
			pathGroup.addLayer(L.polyline([points[i - 1], points[i]]));
		}
	}
}

//Addition Of Demo Markers
function addH4Markers() {
	var H423 = {
		'name': 'H423',
		'coord': L.latLng([670, 348])
	};
	var H436 = {
		'name': 'H436',
		'coord': L.latLng([1402, 1214])
	};
	var H433 = {
		'name': 'H433',
		'coord': L.latLng([1929, 353])
	};
	var H401 = {
		'name': 'H401',
		'coord': L.latLng([387, 1961])
	};

	var points = [H423, H436, H433, H401];

	for (var i = 0; i < points.length; i++) {
		var point = points[i];
		var marker = L.marker(point.coord, {
			'name': point.name
		}).on('click', function(ev) {
			Android.pushRoom(this.options.name);
		});
		roomMarkerGroup.addLayer(marker);
	}
}

//Clearing Functions
function clearPathLayers() {
	pathGroup.clearLayers();
	pathMarkerGroup.clearLayers();
}

function clearMarkerLayers() {
	pathMarkerGroup.clearLayers();
	roomMarkerGroup.clearLayers();
}

function clearPolygonLayers() {
    polygonGroup.clearLayers();
}

function clearAllLayers() {
	clearPathLayers();
	clearMarkerLayers();
	clearPolygonLayers()
}

//Document Init
$(document).ready(function() {
	initmap();
})