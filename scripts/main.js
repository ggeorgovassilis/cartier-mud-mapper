var Client = {
		print:function (message){
			lib.sendTextToClient(message);
		},
		println:function (message){
			Client.print(message+"\n");
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