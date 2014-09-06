cartier-mud-mapper
==================

A mapper and intelligent assistant for playing MUDs. There's nothing to see here yet.

## Using

### Configuration

1. Make sure there is a ```personalization``` directory.
2. Copy ```config.properties.default``` to ```personalization/config.properties``` 
3. You can add you own scripts to ```personalization/scripts```
4. Store any maps you might have in ```personalization/maps```
 
### Running
 
After starting cartier you can connect with your favourite client to localhost:3000. This will forward a connection to the MUD.
So far cartier will simply forward 1:1 network traffic between the client and the MUD with one exception:

### Using scripts

The syntax: ```c:<some javascript command>``` will execute javascript in cartier which have been defined in ```main.js```. Commands currently available:

```c:Mud.println("hello word")``` will send text to the MUD as if you typed it in on the keyboard. There's also a ```print``` version that
doesn't send a trailing newline (Return key).

```c:Client.println("hello word")``` will send text to the client as if it came directly from the MUD. There's also a ```print``` version that doesn't send a trailing newline (Return key).

More commands:

```javascript Client.reloadScripts()``` will reload all scripts.

```javascript Maps.list()``` will show a list of available maps.

```javascript Maps.load('map_name.js')``` will load and show in the browser the map specified by 'map_name.js'.

```javascript Maps.zoomIn()``` will zoom in the map.

```javascript Maps.zoomOut()``` will zoom out the map.

```javascript Maps.zoomReset()``` will reset the map zoom.

```javascript Maps.showLevel(level)``` will show a map level.
