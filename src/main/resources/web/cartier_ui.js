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

UI = {
	map:null,
	tileSize : 30,
	level:0,
	checkCommands : function() {
		$.ajax({
			url : '/json/queue'
		}).done(function(command) {
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
		});
	},
	zoomMapIn:function(){
		UI.tileSize+=2;
		UI.showMap(UI.map);
	},
	zoomMapOut:function(){
		if (UI.tileSize>2){
			UI.tileSize-=2;
			UI.showMap(UI.map);
		}
	},
	zoomMapReset:function(){
		UI.tileSize=20;
		UI.showMap(UI.map);
	},
	drawTile: function(tile){
		var eMap = $("#map");
		var ctx = eMap.get(0).getContext('2d');
		ctx.fillStyle = "rgb(0,0,0)";
		UI._drawTile(tile, ctx);
	},
	_drawTile: function(tile, ctx){
		var x = tile.column * UI.tileSize;
		var y = tile.row * UI.tileSize;
		var m = UI.tileSize/2;
		ctx.fillRect(x, y, 1, 1);
		for (var i = 0; i < tile.exits.length; i++) {
			var e = tile.exits[i];
			if (e.type == "door")
				ctx.fillStyle = "rgb(0,150,0)";
			var exit = {};
			if (e.direction == "north")
				ctx.fillRect(x, y, UI.tileSize, 1);
			else if (e.direction == "east")
				ctx.fillRect(x + UI.tileSize, y, 1, UI.tileSize);
			else if (e.direction == "south")
				ctx.fillRect(x, y + UI.tileSize - 1, UI.tileSize, 1);
			else if (e.direction == "west")
				ctx.fillRect(x, y, 1, UI.tileSize);
			if (e.direction == "up") {
				var m = UI.tileSize/2;
				ctx.beginPath();
				ctx.arc(x+m,y+m,m,0,Math.PI*2,false);
				ctx.moveTo(x+m-3,y+m);
				ctx.lineTo(x+m+3,y+m);
				ctx.moveTo(x+m,y+m-3);
				ctx.lineTo(x+m,y+m+3);
				ctx.stroke();
			} else if (e.direction == "down") {
				var m = UI.tileSize/2;
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
	updateLevelSelection : function(map){
		var levels = {};
		for (var i = 0; i < map.tiles.length; i++) {
			var tile = map.tiles[i];
			levels["l"+tile.level]=true;
		}
		
		var larr=new Array();
		for (var property in levels) {
		    if (levels.hasOwnProperty(property)) {
		    	larr.push(property.substring(1));
		    }
		}
		larr.sort();
		var select = $("#levels");
		select.empty();
		for (var i=0;i<larr.length;i++){
			select.append("<option value="+larr[i]+">Level "+larr[i]+"</option>")
		}
		return larr;
	},
	showMap : function(map) {
		var levels = UI.updateLevelSelection(map);
		UI.level = levels[0];
		UI.redrawMap(map);
	},
	redrawMap : function(map){
		UI.map = map;
		var eMap = $("#map");
		var eContainer = $("#container");
		var width = window.innerWidth;
		var height = window.innerHeight;
		eContainer.width(width);
		eContainer.height(height - $("#levels").height()-15);
		var ctx = eMap.get(0).getContext('2d');
		ctx.fillStyle = "rgb(0,0,0)";
		ctx.clearRect(0,0,eMap.width(),eMap.height());
		var cols = eMap.width()/UI.tileSize;
		var rows = eMap.height()/UI.tileSize;
		
		for (var x=0;x<cols;x++)
			for (var y=0;y<rows;y++){
				ctx.fillRect(x*UI.tileSize, y*UI.tileSize, 1, 1);
			}
		for (var i = 0; i < map.tiles.length; i++) {
			var tile = map.tiles[i];
			if (tile.level == UI.level)
				UI._drawTile(tile, ctx);
		}
		
	},
	onLevelSelectionChanged : function(){
		var level = $('#levels').find(":selected").attr("value");
		UI.level = level;
		UI.redrawMap(UI.map);
	}
}

window.setInterval(UI.checkCommands, 1000);

$("#levels").change(UI.onLevelSelectionChanged);