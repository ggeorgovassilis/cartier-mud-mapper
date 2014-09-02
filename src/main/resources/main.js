var Client = {
		print:function (message){
			lib.sendTextToClient(message);
		},
		println:function (message){
			Client.print(message+"\n");
		},
		reloadScripts:function(){
			lib.reloadScripts();
		}
}

var Mud = {
		print:function (message){
			lib.sendTextToMud(message);
		},
	println:function (message){
		Mud.print(message+"\n");
	}
}

var Maps = {
		list: function(){
			Client.println("User maps:");
			var mapNames = lib.getMapNames();
			for (var i=0;i<mapNames.length;i++)
				Client.println(mapNames[i]);
		},
		load: function(fileName){
			Client.println("Loading map "+fileName);
			lib.loadMap(fileName);
		},
		zoomIn: function(){
			lib.mapZoomIn();
		},
		zoomOut: function(){
			lib.mapZoomOut();
		},
		zoomReset: function(){
			lib.mapZoomReset();
		}
}