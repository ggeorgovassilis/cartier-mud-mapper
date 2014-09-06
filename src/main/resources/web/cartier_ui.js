Arrows={
	"north_west":{dx:-1, dy:-1, dz: 0},	
	"north"		:{dx: 0, dy:-1, dz: 0},	
	"north_east":{dx: 1, dy:-1, dz: 0},	
	"east"		:{dx: 1, dy: 0, dz: 0},	
	"south_east":{dx: 1, dy: 1, dz: 0},	
	"south"		:{dx: 0, dy: 1, dz: 0},	
	"south_west":{dx:-1, dy: 1, dz: 0},	
	"west"		:{dx:-1, dy: 0, dz: 0}	
};

mapBackgroundColour="rgb(150,150,150)";
mapTileBackgroundColour_default="rgb(255,255,255)";
mapTileSize=30;

UI = {
	map:null,
	level:0,
	checkCommands : function() {
		$.ajax({
			url : '/json/queue'
		}).done(function(command) {
			console.log(command);
			checkForCommands();
			if (!command)
				return;
			if (command.name == "loadmap")
				UI.showMap(command.payload);
			if (command.name == "zoomin")
				UI.zoomMapIn();
			if (command.name == "zoomout")
				UI.zoomMapOut();
			if (command.name == "zoomreset")
				UI.zoomMapReset();
			if (command.name == "showlevel")
				UI.showMapLevel(command.payload);
		});
	},
	zoomMapIn:function(){
		mapTileSize+=2;
		UI.redrawMap(UI.map);
	},
	zoomMapOut:function(){
		if (mapTileSize>2){
			mapTileSize-=2;
			UI.redrawMap(UI.map);
		}
	},
	zoomMapReset:function(){
		mapTileSize=30;
		UI.redrawMap(UI.map);
	},
	drawTile: function(tile){
		var eMap = $("#map");
		var ctx = eMap.get(0).getContext('2d');
		ctx.fillStyle = "rgb(0,0,0)";
		UI._drawTile(tile, ctx);
	},
	_drawTile: function(tile, ctx){
		var x = tile.column * mapTileSize;
		var y = tile.row * mapTileSize;
		var m = mapTileSize/2;
		ctx.fillStyle = mapTileBackgroundColour_default;
		ctx.fillRect(x,y,mapTileSize, mapTileSize);
		ctx.fillStyle = "rgb(0,0,0)";
		ctx.fillRect(x, y, 1, 1);
		for (var i = 0; i < tile.exits.length; i++) {
			var e = tile.exits[i];
			if (e.type == "door")
				ctx.fillStyle = "rgb(0,150,0)";
			var exit = {};
			if (e.direction == "north")
				ctx.fillRect(x, y, mapTileSize, 1);
			else if (e.direction == "east")
				ctx.fillRect(x + mapTileSize, y, 1, mapTileSize);
			else if (e.direction == "south")
				ctx.fillRect(x, y + mapTileSize - 1, mapTileSize, 1);
			else if (e.direction == "west")
				ctx.fillRect(x, y, 1, mapTileSize);
			if (e.direction == "up") {
				var m = mapTileSize/2;
				ctx.beginPath();
				ctx.arc(x+m,y+m,m,0,Math.PI*2,false);
				ctx.moveTo(x+m-3,y+m);
				ctx.lineTo(x+m+3,y+m);
				ctx.moveTo(x+m,y+m-3);
				ctx.lineTo(x+m,y+m+3);
				ctx.stroke();
			} else if (e.direction == "down") {
				var m = mapTileSize/2;
				ctx.beginPath();
				ctx.arc(x+m,y+m,m,0,Math.PI*2,false);
				ctx.moveTo(x+m-3,y+m-3);
				ctx.lineTo(x+m+3,y+m+3);
				ctx.moveTo(x+m-3,y+m+3);
				ctx.lineTo(x+m+3,y+m-3);
				ctx.stroke();
			}
			ctx.fillStyle = "rgb(0,0,0)";
		}
		if (tile.text){
			ctx.fillStyle = "rgb(128,128,128)";
			ctx.fillText(tile.text, x, y+m);
			ctx.fillStyle = "rgb(0,0,0)";
		}
		if (tile.arrows.length>0){
			ctx.setLineDash([3]);
			for (var i=0;i<tile.arrows.length;i++){
				var dir = Arrows[tile.arrows[i]];
				ctx.beginPath();
				ctx.moveTo(x+m,y+m);
				ctx.lineTo(x+m+dir.dx*m,y+m+dir.dy*m);
				ctx.stroke();
			}
			ctx.setLineDash([]);
		}
	},
	showMap : function(map) {
		UI.map = map;
		UI.level = 0;
		UI.showMapLevel(UI.level);
	},
	redrawMap : function(map){
		UI.map = map;
		var eMap = $("#map");
		var eContainer = $("#container");
		var width = window.innerWidth-20;
		var height = window.innerHeight-20;
		eContainer.width(width);
		eContainer.height(height - $("#levels").height()-15);
		var ctx = eMap.get(0).getContext('2d');
		ctx.fillStyle = mapBackgroundColour;
		ctx.fillRect(0,0,eMap.width(),eMap.height());
		ctx.fillStyle = "rgb(0,0,0)";
		var cols = eMap.width()/mapTileSize;
		var rows = eMap.height()/mapTileSize;
		
		for (var x=0;x<cols;x++)
			for (var y=0;y<rows;y++){
				ctx.fillRect(x*mapTileSize, y*mapTileSize, 1, 1);
			}
		for (var i = 0; i < map.tiles.length; i++) {
			var tile = map.tiles[i];
			if (tile.level == UI.level)
				UI._drawTile(tile, ctx);
		}
		
	},
	showMapLevel: function(level){
		UI.level = level;
		UI.redrawMap(UI.map);
		$("#level").html("Level "+level);
	}
}

function checkForCommands(){
	window.setTimeout(UI.checkCommands, 100);
}

checkForCommands();

$(window).resize(function(){
	UI.redrawMap(UI.map);
});

