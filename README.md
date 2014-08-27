cartier-mud-mapper
==================

A mapper and intelligent assistant for playing MUDs. There's nothing to see here yet.

## Using

### Configuration
Copy ```config.properties.default``` to ```config.properties``` at the same location and edit the self-explaining values.

### Running
 
After starting cartier you can connect with your favourite client to localhost:3000. This will forward a connection to the MUD.
So far cartier will simply forward 1:1 network traffic between the client and the MUD with one exception:

### Using scripts

The syntax: ```cartier:<some javascript command>``` will execute javascript in cartier which have been defined in ```main.js```. Commands currently available:

```cartier:Mud.println("hello word")``` will send text to the MUD as if you typed it in on the keyboard. There's also a ```print``` version that
doesn't send a trailing newline (Return key).

```cartier:Client.println("hello word")``` will send text to the client as if it came directly from the MUD. There's also a ```print``` version that doesn't send a trailing newline (Return key).

Modifying main.js will load the new file as soon as a command is executed.