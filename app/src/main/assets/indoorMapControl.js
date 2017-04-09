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

//Constants
var panZoom = -1;

//Debug Flag
var debug = false;

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
		case "CC1":
			loadMapImage('floormaps/CC/1.png', 1704, 4944);
			break;
		case "H2":
			loadMapImage('floormaps/H/2.png', 2604, 2356);
			break;
		case "H4":
			loadMapImage('floormaps/H/4.png', 1989, 2196);
			break;
	}
}

//Path Drawing
function drawWalkablePath(pointArray) {
    clearPathLayers();

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

function addFloorRooms(roomArray) {

    for (var i = 0; i < roomArray.length; i++) {
        var room = roomArray[i];
        var polygonBounds = [];

        for (var j = 0; j < room.polygonCoords.length; j++) {
            var coord = room.polygonCoords[j];
            var bound = [coord.lat, coord.lng];
            polygonBounds.push(bound);
        }

        //Polygon Options
        var polygonOptions = {
            'roomName': room.roomName
        };

        if (!debug) {
            polygonOptions.fillOpacity = 0;
            polygonOptions.stroke = false;
        }

        //Create Polygon Object
        var polygon = L.polygon(polygonBounds, polygonOptions);

        var hasIcon = false;
        var iconMarker;
        switch (room.roomIcon) {
            case "NONE":
                break;
            case "WASHROOM_MALE":
                hasIcon = true;
                iconMarker = maleWashroomIcon(polygon.getBounds().getCenter(), room.roomName);
                break;
            case "WASHROOM_FEMALE":
                hasIcon = true;
                iconMarker = femaleWashroomIcon(polygon.getBounds().getCenter(), room.roomName);
                break;
            case "ELEVATOR":
                hasIcon = true;
                iconMarker = elevatorIcon(polygon.getBounds().getCenter(), room.roomName);
                break;
            case "STAIRS":
                hasIcon = true;
                iconMarker = stairsIcon(polygon.getBounds().getCenter(), room.roomName);
                break;
            case "WATERFOUNTAIN":
                hasIcon = true;
                iconMarker = waterfountainIcon(polygon.getBounds().getCenter(), room.roomName);
                break;
        }

        if (hasIcon) {
            iconMarker.on('click', function(event) {
                Android.poiClicked(event.target.options.roomName);
            })
            iconMarker.addTo(roomMarkerGroup);
        }

        //Add click listener to alert IndoorMapFragment when a room polygon is clicked
        polygon.on('click', function(event) {
            Android.poiClicked(event.target.options.roomName);
        });

        polygon.addTo(polygonGroup);
    }
}

//Icons
function maleWashroomIcon(point, roomName) {
    var maleWashroom = L.icon({
        iconUrl: 'images/washroom-male.png',

        iconSize: [30, 30],
        iconAnchor: [15, 15],
        popupAnchor: [0, 0]
    });

    return L.marker(point, { icon: maleWashroom, "roomName": roomName });
}

function femaleWashroomIcon(point, roomName) {
    var femaleWashroom = L.icon({
        iconUrl: 'images/washroom-female.png',

        iconSize: [30, 30],
        iconAnchor: [15, 15],
        popupAnchor: [0, 0]
    });

    return L.marker(point, { icon: femaleWashroom, "roomName": roomName });
}

function elevatorIcon(point, roomName) {
    var elevator = L.icon({
        iconUrl: 'images/elevator.png',

        iconSize: [30, 30],
        iconAnchor: [15, 15],
        popupAnchor: [0, 0]
    });

    return L.marker(point, { icon: elevator, "roomName": roomName });
}

function stairsIcon(point, roomName) {
    var stairs = L.icon({
        iconUrl: 'images/stairs.png',

        iconSize: [30, 30],
        iconAnchor: [15, 15],
        popupAnchor: [0, 0]
    });

    return L.marker(point, { icon: stairs, "roomName": roomName });
}

function waterfountainIcon(point, roomName) {
    var waterfountain = L.icon({
        iconUrl: 'images/waterfountain.png',

        iconSize: [30, 30],
        iconAnchor: [15, 15],
        popupAnchor: [0, 0]
    });

    return L.marker(point, { icon: waterfountain, "roomName": roomName });
}

//Utility Functions
function panTo(x, y) {
    map.flyTo(L.latLng(curLatBound - y, x), panZoom);
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