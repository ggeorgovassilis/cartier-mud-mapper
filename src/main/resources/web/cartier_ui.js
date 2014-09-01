UI = {

	tileSize : 30,
	checkCommands : function() {
		$.ajax({
			url : '/json/queue'
		}).done(function(command) {
			if (command.name == "loadmap")
				UI.showMap(command.payload);
		});
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
			ctx.fillText(tile.text, x, y);
			ctx.fillStyle = "rgb(0,0,0)";
		}
	},
	showMap : function(map) {
		var eMap = $("#map");
		var eContainer = $("#container");
//		eMap.attr("width", (eContainer.width() -50)+ "px");
//		eMap.attr("height", (eContainer.height() -50)+ "px");
		var ctx = eMap.get(0).getContext('2d');
		ctx.fillStyle = "rgb(0,0,0)";
		for (var x=0;x<1000;x++)
		for (var y=0;y<1000;y++){
			ctx.fillRect(x*UI.tileSize, y*UI.tileSize, 1, 1);
		}
		for (var i = 0; i < map.tiles.length; i++) {
			var tile = map.tiles[i];
			UI._drawTile(tile, ctx);
		}
	}
}

window.setInterval(UI.checkCommands, 1000);
